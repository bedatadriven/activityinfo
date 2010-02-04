/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.clientjre.place.entry.editor;

import org.activityinfo.client.mock.JreCoordinateEditor;
import org.activityinfo.shared.map.AbstractCoordinateEditor;
import org.activityinfo.shared.map.CoordinateFormatException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class CoordinateEditorTest {
    private static final double DELTA = 0.00001;


    @Test
    public void testDDdParse() throws CoordinateFormatException {
        JreCoordinateEditor ed = new JreCoordinateEditor("S", "N");
        Assert.assertEquals(6.325, ed.parse("+6.325"), DELTA);
        Assert.assertEquals(-6.325, ed.parse("6.325 S"), DELTA);
        Assert.assertEquals(-2.45, ed.parse("S 2.45"), DELTA);
        Assert.assertEquals(+2.0, ed.parse("2N"), DELTA);
    }

    @Test(expected = CoordinateFormatException.class)
    public void testNoHemiError() throws CoordinateFormatException {

        JreCoordinateEditor ed = new JreCoordinateEditor("S", "N");
        ed.parse("2.345");

    }

    public void testNoHemiOK() throws CoordinateFormatException {

        JreCoordinateEditor ed = new JreCoordinateEditor("W", "E");
        ed.setMinValue(-20);
        ed.setMaxValue(-21);
        Assert.assertEquals(-20.5, ed.parse("20.5"), 0.001);

        ed.setMinValue(30);
        ed.setMaxValue(35);
        Assert.assertEquals(33.3, ed.parse("33.3"), 0.001);

    }

    @Test
    public void testDMd() throws CoordinateFormatException {
        JreCoordinateEditor ed = new JreCoordinateEditor("S", "N");

        Assert.assertEquals(30.25, ed.parse("30 15.00\"  N"), DELTA);
        Assert.assertEquals(-30.75, ed.parse("30 45.0000\" S"), DELTA);
        Assert.assertEquals(-25.25, ed.parse("S   25 15 "), DELTA);

    }

    @Test
    public void testDMS() throws CoordinateFormatException {
        JreCoordinateEditor ed = new JreCoordinateEditor("S", "N");

        Assert.assertEquals(25.18173056, ed.parse("25 10 54.23\"  N"), DELTA);
        Assert.assertEquals(-176.8397222, ed.parse("176 50' 23\" S"), DELTA);
    }

    @Test
    public void formatDDd() {

        JreCoordinateEditor ed = new JreCoordinateEditor("S", "N");
        ed.setNotation(AbstractCoordinateEditor.Notation.DDd);
        Assert.assertEquals("+2.405000", ed.format(2.405));
    }

    @Test
    public void testNearEquator() {
        JreCoordinateEditor ed = new JreCoordinateEditor("S", "N");

        Assert.assertEquals(ed.format(-0.9392889738082886), "0° 56' 21.44\" S");
    }


}
