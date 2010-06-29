package org.sigmah.server.report.generator;

import org.junit.Assert;
import org.junit.Test;
import org.sigmah.server.report.generator.map.Grid;
import org.sigmah.shared.report.content.Point;

import java.util.List;
/*
 * @author Alex Bertram
 */

public class GridTest {

    @Test
    public void testRetrieveCell() {

        Grid<Integer, String> grid = new Grid<Integer, String>(25);
        Assert.assertNull(grid.getCell(1, new Point(0, 0)));

        grid.setCell(1, new Point(0, 0), "foobar");

        Assert.assertEquals("foobar", grid.getCell(1, new Point(0,0)));

    }

    @Test
    public void testPointToCell() {


        Grid<Integer, String> grid = new Grid<Integer, String>(25);

        Assert.assertEquals(new Point(0, 0), grid.pointToGridCoord(new Point(0, 0)));
        Assert.assertEquals(new Point(0, 1), grid.pointToGridCoord(new Point(5, 26)));
        Assert.assertEquals(new Point(0, 1), grid.pointToGridCoord(new Point(24, 49)));

    }

    @Test
    public void testGetAll() {

        Grid<Integer, String> grid = new Grid<Integer, String>(25);
        grid.setCell(1, new Point(0, 0), "foo");
        grid.setCell(2, new Point(30, 45), "bar");

        List<String> cells = grid.allCells();
        Assert.assertEquals("length", 2, cells.size());
    }


}
