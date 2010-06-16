package org.activityinfo.client.page.common.widget;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.PropertyEditor;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.i18n.client.NumberFormat;
import org.activityinfo.client.Application;
import org.activityinfo.shared.map.AbstractCoordinateEditor;
import org.activityinfo.shared.map.CoordinateFormatException;

/**
 *
 * The CoordinateEditor implements the GXT interface {@link com.extjs.gxt.ui.client.widget.form.PropertyEditor}
 * and converts between text and lat/lng coordinates.
 *
 * See {@link org.activityinfo.shared.map.AbstractCoordinateEditor}
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class CoordinateEditor extends AbstractCoordinateEditor
        implements PropertyEditor<Double>, Validator {

    private NumberFormat dddFormat;
    private NumberFormat shortFracFormat;
    private NumberFormat intFormat;

    private String outOfBoundsMessage;

    public CoordinateEditor(String negHemiChars, String posHemiChars) {
        this.negHemiChars = negHemiChars;
        this.posHemiChars = posHemiChars;
        this.decimalSeperators = ".,";
        this.noHemisphereMessage = Application.CONSTANTS.noHemisphere();
        this.tooManyNumbersErrorMessage = Application.CONSTANTS.tooManyNumbers();
        this.invalidMinutesMessage = Application.CONSTANTS.invalidMinutes();
        this.invalidSecondsMessage = Application.CONSTANTS.invalidSeconds();
        this.noNumberErrorMessage = Application.CONSTANTS.noNumber();

        dddFormat = NumberFormat.getFormat("+0.000000;-0.000000");
        shortFracFormat = NumberFormat.getFormat("0.00");
        intFormat = NumberFormat.getFormat("0");
    }

    @Override
    protected Double parseDouble(String s) {
        return NumberFormat.getDecimalFormat().parse(s);
    }

    @Override
    protected String formatDDd(double value) {
        return dddFormat.format(value);
    }

    @Override
    protected String formatShortFrac(double value) {
        return shortFracFormat.format(value);
    }

    @Override
    protected String formatInt(double value) {
        return intFormat.format(value);
    }

    @Override
    public String getStringValue(Double value) {
        String s = format(value);
        Log.debug("CoordinateEditor: " + value + " -> " + s);
        return s;
    }

    @Override
    public Double convertStringValue(String value) {
        if(value == null) {
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
        if(value == null) {
            return null;
        }

        try {
            double coord = parse(value);

            if(coord < minValue || coord > maxValue) {
                return outOfBoundsMessage;
            }

            return null;
        } catch(CoordinateFormatException ex) {
            return ex.getMessage();
        } catch(NumberFormatException ex) {
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
