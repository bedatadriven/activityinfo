package org.activityinfo.clientjre.place.entry.editor;

import org.activityinfo.clientjre.mock.JreCoordinateEditor;
import org.activityinfo.shared.map.AbstractCoordinateEditor;
import org.activityinfo.shared.map.CoordinateFormatException;
import org.junit.Test;
import org.junit.Assert;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class CoordinateEditorTest {


    @Test
    public void testDDdParse() throws CoordinateFormatException {

        JreCoordinateEditor ed = new JreCoordinateEditor("S", "N");
        Assert.assertEquals(6.325, ed.parse("+6.325"));
        Assert.assertEquals(-6.325, ed.parse("6.325 S"));
        Assert.assertEquals(-2.45, ed.parse("S 2.45"));
        Assert.assertEquals(+2.0, ed.parse("2N"));



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
        Assert.assertEquals(-20.5, ed.parse("20.5"));

        ed.setMinValue(30);
        ed.setMaxValue(35);
        Assert.assertEquals(33.3, ed.parse("33.3"));

    }

    @Test
    public void testDMd() throws CoordinateFormatException {
        JreCoordinateEditor ed = new JreCoordinateEditor("S", "N");

        Assert.assertEquals(30.25, ed.parse("30 15.00\"  N"));
        Assert.assertEquals(-30.75, ed.parse("30 45.0000\" S"));
        Assert.assertEquals(-25.25, ed.parse("S   25 15 "));

    }

    @Test
    public void testDMS() throws CoordinateFormatException {
        JreCoordinateEditor ed = new JreCoordinateEditor("S", "N");

        Assert.assertEquals(25.18173056, ed.parse("25 10 54.23\"  N"),  0.00000001);
        Assert.assertEquals(-176.8397222, ed.parse("176 50' 23\" S"), 0.0000001);
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
