/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.widget;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.PropertyEditor;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.i18n.client.NumberFormat;
import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.map.AbstractCoordinateEditor;
import org.sigmah.shared.map.CoordinateFormatException;

/**
 *
 * The CoordinateEditor implements the GXT interface {@link com.extjs.gxt.ui.client.widget.form.PropertyEditor}
 * and converts between text and lat/lng coordinates.
 *
 * See {@link org.sigmah.shared.map.AbstractCoordinateEditor}
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
class CoordinateEditor extends AbstractCoordinateEditor
        implements PropertyEditor<Double>, Validator {

    private NumberFormat dddFormat;
    private NumberFormat shortFracFormat;
    private NumberFormat intFormat;

    private String outOfBoundsMessage;

    public CoordinateEditor(String negHemiChars, String posHemiChars) {
        this.negHemiChars = negHemiChars;
        this.posHemiChars = posHemiChars;
        this.decimalSeperators = ".,";
        this.noHemisphereMessage = I18N.CONSTANTS.noHemisphere();
        this.tooManyNumbersErrorMessage = I18N.CONSTANTS.tooManyNumbers();
        this.invalidMinutesMessage = I18N.CONSTANTS.invalidMinutes();
        this.invalidSecondsMessage = I18N.CONSTANTS.invalidSeconds();
        this.noNumberErrorMessage = I18N.CONSTANTS.noNumber();

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
