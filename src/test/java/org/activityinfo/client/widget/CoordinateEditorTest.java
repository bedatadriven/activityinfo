

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

import java.text.NumberFormat;
import java.util.Locale;

import org.activityinfo.client.widget.CoordinateEditor;
import org.activityinfo.client.widget.CoordinateField.Axis;
import org.activityinfo.shared.map.CoordinateFormatException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.teklabs.gwt.i18n.server.LocaleProxy;


public class CoordinateEditorTest {
    private static final double DELTA = 0.00001;
	private CoordinateEditor editor;
    
    @Before
    public void before() {
		LocaleProxy.initialize();
        LocaleProxy.setLocale(Locale.ENGLISH);
    }

    @Test
    public void testDDdParse() throws CoordinateFormatException {
        createLatitudeEditor();
        Assert.assertEquals(6.325, editor.parse("+6.325"), DELTA);
        Assert.assertEquals(-6.325, editor.parse("6.325 S"), DELTA);
        Assert.assertEquals(-2.45, editor.parse("S 2.45"), DELTA);
        Assert.assertEquals(+2.0, editor.parse("2N"), DELTA);
    }


	private void createLatitudeEditor() {
		editor = new CoordinateEditor(Axis.LATITUDE, new JreNumberFormats());
	}

    @Test(expected = CoordinateFormatException.class)
    public void testNoHemiError() throws CoordinateFormatException {

    	createLatitudeEditor();
        editor.parse("2.345");

    }

    public void testNoHemiOK() throws CoordinateFormatException {

        createLongitudeEditor();
        editor.setMinValue(-20);
        editor.setMaxValue(-21);
        Assert.assertEquals(-20.5, editor.parse("20.5"), 0.001);

        editor.setMinValue(30);
        editor.setMaxValue(35);
        Assert.assertEquals(33.3, editor.parse("33.3"), 0.001);

    }


	private void createLongitudeEditor() {
		editor = new CoordinateEditor(Axis.LONGITUDE, new JreNumberFormats());
	}

    @Test
    public void testDMd() throws CoordinateFormatException {
    	createLatitudeEditor();
    	
        Assert.assertEquals(30.25, editor.parse("30 15.00\"  N"), DELTA);
        Assert.assertEquals(-30.75, editor.parse("30 45.0000\" S"), DELTA);
        Assert.assertEquals(-25.25, editor.parse("S   25 15 "), DELTA);

    }

    @Test
    public void testDMS() throws CoordinateFormatException {
    	createLatitudeEditor();
    	
        Assert.assertEquals(25.18173056, editor.parse("25 10 54.23\"  N"), DELTA);
        Assert.assertEquals(-176.8397222, editor.parse("176 50' 23\" S"), DELTA);
    }

    @Test
    public void formatDDd() {

    	createLatitudeEditor();
    	
        editor.setNotation(CoordinateEditor.Notation.DDd);
        Assert.assertEquals("+2.405000", editor.format(2.405));
    }

    @Test
    public void testNearEquator() {
        createLatitudeEditor();

        Assert.assertEquals(editor.format(-0.9392889738082886), "0° 56' 21.44\" S");
    }
    
    
    public class JreNumberFormats implements CoordinateEditor.NumberFormats {


    	@Override
        public double parseDouble(String s) {
            return Double.parseDouble(s);
        }

        @Override
        public String formatDDd(double value) {
            NumberFormat fmt =  NumberFormat.getInstance();
            fmt.setMinimumFractionDigits(6);
            fmt.setMaximumFractionDigits(6);
            fmt.setMinimumIntegerDigits(0);

            if(value > 0) {
                return "+" + fmt.format(value);
            } else {
                return fmt.format(value);
            }
        }

        @Override
        public String formatShortFrac(double value) {
            NumberFormat fmt =  NumberFormat.getInstance();
            fmt.setMinimumFractionDigits(2);
            fmt.setMaximumFractionDigits(2);
            fmt.setMinimumIntegerDigits(0);

            return fmt.format(value);
        }

        @Override
        public String formatInt(double value) {
            return NumberFormat.getIntegerInstance().format(value);
        }
    }
}
