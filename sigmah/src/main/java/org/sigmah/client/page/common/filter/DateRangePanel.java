package org.sigmah.client.page.common.filter;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.form.DateField;
import org.sigmah.client.Application;
import org.sigmah.shared.report.model.Filter;

import java.util.Date;

/**
 * @author Alex Bertram
 */
public class DateRangePanel extends ContentPanel {
    private DateField date1;
    private DateField date2;

    public DateRangePanel() {

        setHeading(Application.CONSTANTS.filterByDate());
        setIcon(Application.ICONS.filter());
        setBodyStyle("padding:5px");

        add(new Html(Application.CONSTANTS.fromDate()));

        date1 = new DateField();
        //date1.setFildLabel(Application.CONSTANTS.fromDate());
        add(date1);

        add(new Html(Application.CONSTANTS.toDate()));
        date2 = new DateField();
        //date2.setFieldLabel(Application.CONSTANTS.toDate());
        add(date2);
    }

    public Date getMinDate() {
        return date1.getValue();
    }

    public Date getMaxDate() {
        return date2.getValue();
    }

    public void updateFilter(Filter filter) {
        filter.setMinDate(date1.getValue());
        filter.setMaxDate(date2.getValue());
    }
}
