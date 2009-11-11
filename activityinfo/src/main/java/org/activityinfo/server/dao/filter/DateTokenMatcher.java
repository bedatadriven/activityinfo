package org.activityinfo.server.dao.filter;

import org.activityinfo.shared.report.model.DateRange;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class DateTokenMatcher implements TokenMatcher {

    private final Locale locale;

    protected List<String> monthNames;
    protected List<String> shortMonthNames;
    protected List<String> beforeTokens;
    protected List<String> afterTokens;
    protected List<String> rangeSeperatorTokens;
    protected List<DateFormat> dateFormats;
    protected Calendar today;

    protected Pattern intPattern;

    public static class PartialDate {
        public int day;
        public int month;
        public int year;

        public PartialDate() {
            day = -1;
            month = -1;
            year = -1;
        }

        public PartialDate(Date point) {
            Calendar c = Calendar.getInstance();
            c.setTime(point);
            year = -1;
            year = c.get(Calendar.YEAR);
            month = -1;
            month = c.get(Calendar.MONTH)+1;
            day = -1;
            day = c.get(Calendar.DATE);
        }

        public boolean hasDay() {
            return day>0;
        }

        public boolean hasMonth() {
            return month >0;
        }

        public boolean hasYear() {
            return year > 0;
        }

        public Date getDate() {
            assert day >0 && month > 0 && year > 0;
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DATE, day);
            return c.getTime();
        }

        public boolean hasOnlyYear() {
            return day <= 0 && month <=0 && year > 0;
        }
    }


    public DateTokenMatcher(Locale locale) {
        this.locale = locale;

        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        monthNames = toLowerList(symbols.getMonths());
        shortMonthNames = toLowerList(symbols.getShortMonths());
        beforeTokens = makeList("avant", "jusqu'à", "jusqua", "jusqu'a");
        afterTokens = makeList("apres", "après", "aprés");
        rangeSeperatorTokens = makeList("-", "à");

        dateFormats = new ArrayList<DateFormat>();
        dateFormats.add(DateFormat.getDateInstance(DateFormat.SHORT, locale));
        dateFormats.add(DateFormat.getDateInstance(DateFormat.MEDIUM, locale));
        dateFormats.add(DateFormat.getDateInstance(DateFormat.LONG, locale));
        dateFormats.add(DateFormat.getDateInstance(DateFormat.FULL, locale));
        intPattern = Pattern.compile("\\d+");

        today = Calendar.getInstance();
    }

    public void setToday(int year, int month, int day) {
        today.set(Calendar.YEAR, year);
        today.set(Calendar.MONTH, month-1);
        today.set(Calendar.DATE, day);
    }

    private List<String> makeList(String... items) {
        List<String> list = new ArrayList<String>(items.length);
        for(String s : items) {
            list.add(s);
        }
        return list;
    }

    private List<String> toLowerList(String[] stringArray) {
        List<String> list = new ArrayList<String>(stringArray.length);
        for(String s : stringArray) {
            list.add(s.toLowerCase(locale));
        }
        return list;
    }

    private int maximumEnd(List<String> tokens, int startIndex) {
        for(int i = startIndex; i!=tokens.size(); ++i) {
            if(!isPotentialDateToken(tokens.get(i))) {
                return i;
            }
        }
        return tokens.size();
    }

    private boolean isPotentialDateToken(String token) {
        if(intPattern.matcher(token).matches())
            return true;
        if(monthNames.contains(token))
            return true;
        if(shortMonthNames.contains(token))
            return true;
        if(beforeTokens.contains(token))
            return true;
        if(afterTokens.contains(token))
            return true;
        if(rangeSeperatorTokens.contains(token))
            return true;

        if(tryParseDate(token) != null)
            return true;

        return false;
    }


    private Date tryParseDate(String s) {
        for(DateFormat format : dateFormats) {

            try {
                return format.parse(s);
            } catch (ParseException e) {

            }
        }
        return null;
    }

    @Override
    public void init(List<String> tokens) {

    }

    @Override
    public MatchResult match(List<String> tokens, int startIndex) {

        int maxEnd = maximumEnd(tokens, startIndex);

        for(int j = maxEnd; j>startIndex; --j) {
            DateRange range = tryParse(tokens.subList(startIndex, j));
            if(range != null) {
                return new MatchResult(j-startIndex, createCriterion(range));    
            }
        }

        return null;
    }

    private Criterion createCriterion(DateRange range) {

        if(range.getMinDate().getTime() > range.getMaxDate().getTime()) {
            Date tmp = range.getMinDate();
            range.setMinDate(range.getMaxDate());
            range.setMaxDate(tmp);
        }

        if(range.getMinDate() != null && range.getMaxDate() !=null) {

            return Restrictions.not(
                Restrictions.or(
                   Restrictions.lt("site.date2", range.getMinDate()),
                   Restrictions.gt("site.date1", range.getMaxDate())));
        } else if(range.getMinDate() != null) {
            return Restrictions.ge("site.date2", range.getMinDate());

        } else {
            return Restrictions.le("site.date1", range.getMinDate());
        }
    }

    public DateRange tryParse(String expression) {
        String[] tokens = expression.toLowerCase(locale).split("\\s+");
        return tryParse(Arrays.asList(tokens));
    }

    public DateRange tryParse(List<String> tokens) {

        // are we dealing with date a range?
        int rsIndex = findRangeSeperatorIndex(tokens);
        if(rsIndex == -2)
            return null;  // too many range tokens here

        if(rsIndex == -1)
            return tryParseSingle(tokens);
        else
            return tryParseRange(tokens, rsIndex);
    }

    private DateRange tryParseRange(List<String> tokens, int rsIndex) {

        if(rsIndex == 0 || rsIndex+1 == tokens.size()) {
            // maybe some day will parse this but for the
            // moment we'll consider it an error
            return null;
        }

        PartialDate pd1 = parsePartialDateOrFormmatedDate(tokens, 0, rsIndex);
        if(pd1==null)
            return null;

        PartialDate pd2 = parsePartialDateOrFormmatedDate(tokens, rsIndex+1, tokens.size());
        if(pd2==null)
            return null;


        // e.g. 1 - 3 may
        // e.g. 1 - 7 april 2009
        if(pd1.hasDay() && pd2.hasDay() && pd2.hasMonth()) {
            if(pd2.hasMonth() && !pd1.hasMonth()) {
                pd1.month = pd2.month;
            }
            if(!pd2.hasYear()) {
                pd2.year = today.get(Calendar.YEAR);
            }
            if(!pd1.hasYear()) {
                pd1.year = pd2.year;
            }
            return new DateRange(pd1.getDate(), pd2.getDate());

        } else if(!pd1.hasDay() && !pd2.hasDay() && pd1.hasMonth() && pd2.hasMonth()) {

            // e.g.  april - mai 2009
            // e.g.  april - mai
            // e.g.  april 2009 - may 2010

            if(!pd2.hasYear()) {
                pd2.year = today.get(Calendar.YEAR);
            }
            if(!pd1.hasYear()) {
                pd1.year = pd2.year;
            }

            DateRange range = new DateRange();

            Calendar c = Calendar.getInstance();
            c.set(Calendar.DATE, 1);
            c.set(Calendar.MONTH, pd1.month-1);
            c.set(Calendar.YEAR, pd1.year);

            range.setMinDate(c.getTime());

            c.set(Calendar.MONTH, pd2.month-1);
            c.set(Calendar.YEAR, pd2.year);
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));

            range.setMaxDate(c.getTime());

            return range;

        } else if(pd1.hasOnlyYear() && pd2.hasOnlyYear()) {

            // e.g.. 2007 - 2009

            DateRange range = new DateRange();

            Calendar c = Calendar.getInstance();
            c.set(Calendar.DATE, 1);
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.YEAR, pd1.year);

            range.setMinDate(c.getTime());

            c.set(Calendar.DATE, 31);
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.YEAR, pd2.year);

            range.setMaxDate(c.getTime());

            return range;

        }

        return null;

    }

    private DateRange tryParseSingle(List<String> tokens) {

        int qualifier = 0 ; // before, after
        int startIndex = 0;

        if(beforeTokens.contains(tokens.get(0))) {
            qualifier = 1;
            startIndex = 1;
        } else if(afterTokens.contains(tokens.get(0))) {
            qualifier = -1;
            startIndex = 1;
        }
        if(startIndex == tokens.size()) {
            return null;
        }

        PartialDate pd = parsePartialDateOrFormmatedDate(tokens, startIndex, tokens.size());
        if (pd == null)
            return null;

        DateRange range = new DateRange();

        if(pd.day > 0 && pd.month > 0) {

            // user has provided a full date

            if(pd.year == -1)
                pd.year = today.get(Calendar.YEAR);

            Calendar c = Calendar.getInstance();
            c.set(Calendar.DATE, pd.day);
            c.set(Calendar.MONTH, pd.month-1);
            c.set(Calendar.YEAR, pd.year);

            if(qualifier == 1) { // before the given date
                c.add(Calendar.DATE, -1);
                range.setMaxDate(c.getTime());
            } else if(qualifier == -1) {
                c.add(Calendar.DATE, 1);
                range.setMinDate(c.getTime());
            } else {
                range.setMinDate(c.getTime());
                range.setMaxDate(range.getMinDate());
            }
            return range;

        } else if(pd.month > 0) {

            // user has provided only a month

            if(pd.year == -1)

                // if the year is not specified, assume this year

                pd.year = today.get(Calendar.YEAR);

                Calendar c = Calendar.getInstance();
                c.set(Calendar.DATE, 1);
                c.set(Calendar.MONTH, pd.month-1);
                c.set(Calendar.YEAR, pd.year);

            if(qualifier == 1) { // before the given month
                c.add(Calendar.DATE, -1);
                range.setMaxDate(c.getTime());
            } else if(qualifier == -1) { // after the given month
                c.add(Calendar.MONTH, +1);
                range.setMinDate(c.getTime());
            } else {                     // durin the given month
                range.setMinDate(c.getTime());
                c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
                range.setMaxDate(c.getTime());
            }
            return range;
        } else {

            // user has provided only a year

            Calendar c = Calendar.getInstance();
            c.set(Calendar.DATE, 1);
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.YEAR, pd.year);
            if(qualifier == 1) { // before the given year
                c.add(Calendar.DATE, -1);
                range.setMaxDate(c.getTime());
            } else if(qualifier == -1) { // after the given year
                c.set(Calendar.YEAR, pd.year+1);
                range.setMinDate(c.getTime());
            } else {  // during
                range.setMinDate(c.getTime());

                c.set(Calendar.MONTH, c.getActualMaximum(Calendar.MONTH));
                c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
                range.setMaxDate(c.getTime());
            }
            return range;
        }
    }

    private PartialDate parsePartialDateOrFormmatedDate(List<String> tokens, int startIndex, int endIndex) {
        PartialDate pd;
        Date point = tryParseDate(FilterHelper.join(tokens, startIndex, endIndex));
        if(point != null) {
           pd = new PartialDate(point);

        } else {
           pd = parsePartialDate(tokens, startIndex, endIndex);

            if(pd == null)
                return null;
            if(pd.year == -1 && pd.month == -1 && pd.day == -1)
                return null;
        }
        return pd;
    }

    private PartialDate parsePartialDate(List<String> tokens, int startIndex, int endIndex) {
        PartialDate pd = new PartialDate();

        for(int ti=startIndex; ti!=endIndex; ++ti) {
            if(intPattern.matcher(tokens.get(ti)).matches()) {
                int i;
                try {
                   i = NumberFormat.getIntegerInstance(locale).parse(tokens.get(ti)).intValue();
                } catch (ParseException e) {
                    throw new Error("programming logic error");
                }
                if(i > 1900) {
                    pd.year = i;
                } else {
                    pd.day = i;
                }
            } else {
                pd.month = parseMonth(tokens.get(ti));
                if(pd.month == 0)
                    return null;
            }
        }

        if(pd.month == -1 && pd.day > 0 && pd.day <= 12) {
            pd.month = pd.day;
            pd.day = -1;
        }

        return pd;
    }

    private int parseMonth(String token) {
        int i = monthNames.indexOf(token);
        if(i >= 0) {
            return i+1;
        } else {
            i = shortMonthNames.indexOf(token);
            if(i >= 0) {
                return i+1;
            } else {
                return -1;
            }
        }
    }

    /*
     * Returns the index of the range seperator, if there
     * is exactly one range seperate in the token list
     */
    private int findRangeSeperatorIndex(List<String> tokens) {
        int index = -1;
        for(int i = 0; i!=tokens.size(); ++i) {
            if(rangeSeperatorTokens.contains(tokens.get(i))) {
                if(index != -1) {
                    // we've found multiple range tokens, doesn't work
                    return -2;
                }
                index = i;
            }
        }
        return index;
    }

}
