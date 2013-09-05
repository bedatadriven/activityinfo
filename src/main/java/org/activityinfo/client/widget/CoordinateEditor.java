package org.activityinfo.client.widget;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.Log;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.widget.CoordinateField.Axis;
import org.activityinfo.shared.map.CoordinateFormatException;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.PropertyEditor;
import com.extjs.gxt.ui.client.widget.form.Validator;

public class CoordinateEditor implements PropertyEditor<Double>, Validator {

    /**
     * Provides number formatting & parsing. Extracted from the class to allow
     * for testing.
     */
    public interface NumberFormats {
        double parseDouble(String s);

        String formatDDd(double value);

        String formatShortFrac(double value);

        String formatInt(double value);
    }

    public enum Notation {
        DDd,
        DMd,
        DMS
    }

    private static final double MINUTES_PER_DEGREE = 60;
    private static final double SECONDS_PER_DEGREE = 3600;
    private static final double MIN_MINUTES = 0;
    private static final double MAX_MINUTES = 60;
    private static final double MIN_SECONDS = 0;
    private static final double MAX_SECONDS = 60;

    private static final double MAX_LATITUDE = 90;
    private static final double MAX_LONGITUDE = 180;

    private static final String DECIMAL_SEPERATORS = ".,";

    private final String posHemiChars;
    private final String negHemiChars;

    private final String noNumberErrorMessage = I18N.CONSTANTS.noNumber();
    private final String tooManyNumbersErrorMessage = I18N.CONSTANTS
        .tooManyNumbers();
    private final String noHemisphereMessage = I18N.CONSTANTS.noHemisphere();
    private final String invalidSecondsMessage = I18N.CONSTANTS
        .invalidMinutes();
    private final String invalidMinutesMessage = I18N.CONSTANTS
        .invalidMinutes();

    private String outOfBoundsMessage;

    private double minValue;
    private double maxValue;

    private Notation notation = Notation.DMS;

    private final NumberFormats formats;

    private boolean requireSign = true;
    
    CoordinateEditor(Axis axis, NumberFormats format) {
        this.formats = format;
        switch (axis) {
        case LATITUDE:
            posHemiChars = I18N.CONSTANTS.northHemiChars();
            negHemiChars = I18N.CONSTANTS.southHemiChars();
            minValue = -MAX_LATITUDE;
            maxValue = +MAX_LATITUDE;
            break;
        case LONGITUDE:
            posHemiChars = I18N.CONSTANTS.eastHemiChars();
            negHemiChars = I18N.CONSTANTS.westHemiChars();
            minValue = -MAX_LONGITUDE;
            maxValue = +MAX_LONGITUDE;
            break;
        default:
            throw new IllegalArgumentException("Axis: " + axis);
        }
    }

    public CoordinateEditor(Axis axis) {
        this(axis, new CoordinateFormatter());
    }
    
    public void setRequireSign(boolean requireSign) {
        this.requireSign = requireSign;
    }

    public Double parse(String value) throws CoordinateFormatException {

        if (value == null) {
            return null;
        }

        StringBuffer[] numbers = new StringBuffer[] {
            new StringBuffer(),
            new StringBuffer(),
            new StringBuffer() };
        int numberIndex = 0;
        int i;

        /*
         * To assure correctness, we're going to insist that the user explicitly
         * enter the hemisphere (+/-/N/S).
         * 
         * However, if the bounds dictate that the coordinate is in one
         * hemisphere, then we can assume the sign.
         */
        double sign = maybeInferSignFromBounds();

        for (i = 0; i != value.length(); ++i) {
            char c = value.charAt(i);

            if (isNegHemiChar(c)) {
                sign = -1;
            } else if (isPosHemiChar(c)) {
                sign = 1;
            } else if (isNumberPart(c)) {
                if (numberIndex > 2) {
                    throw new CoordinateFormatException(
                        tooManyNumbersErrorMessage);
                }
                numbers[numberIndex].append(c);
            } else if (numberIndex != 2 && numbers[numberIndex].length() > 0) {
                // advance to the next token on anything else-- whitespace,
                // symbols like ' " ° -- we won't insist that they are used
                // in the right place
                numberIndex++;
            }
        }

        if (sign == 0) {
            if(requireSign) {
                throw new CoordinateFormatException(noHemisphereMessage);
            } else {
                sign = 1;
            }
        }

        return parseCoordinate(numbers) * sign;
    }

    private double maybeInferSignFromBounds() {
        double sign = 0;
        if (maxValue < 0) {
            sign = -1;
        } else if (minValue > 0) {
            sign = +1;
        }
        return sign;
    }

    private boolean isNumberPart(char c) {
        return Character.isDigit(c) || DECIMAL_SEPERATORS.indexOf(c) != -1;
    }

    private boolean isPosHemiChar(char c) {
        return c == '+' || posHemiChars.indexOf(c) != -1;
    }

    private boolean isNegHemiChar(char c) {
        return c == '-' || negHemiChars.indexOf(c) != -1;
    }

    private double parseCoordinate(StringBuffer[] tokens)
        throws CoordinateFormatException {
        if (tokens[0].length() == 0) {
            throw new CoordinateFormatException(noNumberErrorMessage);
        }

        double coordinate = Double.parseDouble(tokens[0].toString());
        notation = Notation.DDd;

        if (tokens[1].length() != 0) {
            double minutes = formats.parseDouble(tokens[1].toString());
            if (minutes < MIN_MINUTES || minutes > MAX_MINUTES) {
                throw new CoordinateFormatException(invalidMinutesMessage);
            }
            coordinate += minutes / MINUTES_PER_DEGREE;
            notation = Notation.DMd;

        }
        if (tokens[2].length() != 0) {
            double seconds = formats.parseDouble(tokens[2].toString());
            if (seconds < MIN_SECONDS || seconds > MAX_SECONDS) {
                throw new CoordinateFormatException(invalidSecondsMessage);
            }
            notation = Notation.DMS;
            coordinate += seconds / SECONDS_PER_DEGREE;
        }
        return coordinate;
    }

    /**
     * Formats coordinate value into Degree-Minute-decimal notation
     * 
     */
    public String formatAsDMd(double value) {

        double degrees = Math.floor(Math.abs(value));
        double minutes = (Math.abs(value) - degrees);

        StringBuilder sb = new StringBuilder();
        sb.append(formats.formatInt(Math.abs(degrees))).append("° ");
        sb.append(formats.formatShortFrac(minutes)).append("' ");
        sb.append(hemisphereChar(value));

        return sb.toString();
    }

    /**
     * Formats coordinate value into Degree-Minute-Second notation
     * 
     */
    public String formatAsDMS(double value) {
        double absv = Math.abs(value);

        double degrees = Math.floor(absv);
        double minutes = ((absv - degrees) * 60.0);
        double seconds = ((minutes - Math.floor(minutes)) * 60.0);
        minutes = Math.floor(minutes);

        StringBuilder sb = new StringBuilder();
        sb.append(formats.formatInt(Math.abs(degrees))).append("° ");
        sb.append(formats.formatInt(minutes)).append("' ");
        sb.append(formats.formatShortFrac(seconds)).append("\" ");
        sb.append(hemisphereChar(value));

        return sb.toString();
    }

    private char hemisphereChar(double value) {
        if (Math.signum(value) < 0) {
            return negHemiChars.charAt(0);
        } else {
            return posHemiChars.charAt(0);
        }
    }

    public String format(double coordinate) {
        switch (notation) {
        case DDd:
            return formats.formatDDd(coordinate);
        case DMd:
            return formatAsDMd(coordinate);
        default:
        case DMS:
            return formatAsDMS(coordinate);
        }
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public Notation getNotation() {
        return notation;
    }

    public void setNotation(Notation notation) {
        this.notation = notation;
    }

    @Override
    public String getStringValue(Double value) {
        String s = format(value);
        Log.debug("CoordinateEditor: " + value + " -> " + s);
        return s;
    }

    @Override
    public Double convertStringValue(String value) {
        if (value == null) {
            return null;
        }

        try {
            double d = parse(value);
            Log.debug("CoordinateEditor: '" + value + "' -> " + d);
            return d;
        } catch (CoordinateFormatException e) {
            return null;
        }
    }

    @Override
    public String validate(Field<?> field, String value) {
        if (value == null) {
            return null;
        }

        try {
            double coord = parse(value);

            if (coord < minValue || coord > maxValue) {
                return outOfBoundsMessage;
            }

            return null;
        } catch (CoordinateFormatException ex) {
            return ex.getMessage();
        } catch (NumberFormatException ex) {
            return ex.getMessage();
        }
    }

    public String getOutOfBoundsMessage() {
        return outOfBoundsMessage;
    }

    public void setOutOfBoundsMessage(String outOfBoundsMessage) {
        this.outOfBoundsMessage = outOfBoundsMessage;
    }
}
