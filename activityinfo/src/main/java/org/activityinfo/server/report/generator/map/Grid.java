package org.activityinfo.server.report.generator.map;

import org.activityinfo.shared.report.content.Point;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
/*
 * @author Alex Bertram
 */

public class Grid<KeyT, T> {

    private int gridSize;

    private Map<KeyT, Map<Integer, Map<Integer, T>>> grid = new HashMap<KeyT, Map<Integer, Map<Integer, T>>>();

    public Grid(int gridSize) {
        this.gridSize = gridSize;
    }

    public T getCell(KeyT key, Point gridCoords) {

        Map<Integer, Map<Integer, T>> gridx = grid.get(key);
        if(gridx == null) {
            gridx = new HashMap<Integer, Map<Integer, T>>();
            grid.put(key, gridx);
        }

        Map<Integer, T> gridy = gridx.get(gridCoords.getY());
        if(gridy == null) {
            gridy = new HashMap<Integer, T>();
            gridx.put(gridCoords.getY(), gridy);
        }

        return gridy.get(gridCoords.getY());
    }

    public T setCell(KeyT key, Point gridCoords, T cell) {


        Map<Integer, Map<Integer, T>> gridx = grid.get(key);
        if(gridx == null) {
            gridx = new HashMap<Integer, Map<Integer, T>>();
            grid.put(key, gridx);
        }

        Map<Integer, T> gridy = gridx.get(gridCoords.getX());
        if(gridy == null) {
            gridy = new HashMap<Integer, T>();
            gridx.put(gridCoords.getX(), gridy);
        }

        gridy.put(gridCoords.getY(), cell);

        return cell;
    }

    private int toCell(int x) {
        return (int)Math.floor( x / gridSize );
    }

    public Point pointToGridCoord(Point px) {
        return new Point(toCell(px.getX()), toCell(px.getY()));
    }


    public Point gridCellCenter(Point gridCoord) {
        int x = (gridCoord.getX() * gridSize) + (gridSize / 2);
        int y = (gridCoord.getY() * gridSize) + (gridSize / 2);

        return new Point(x,y);
    }

    public List<T> allCells() {
        List<T> list = new ArrayList<T>();
        for(Map<Integer, Map<Integer, T>> gridx : grid.values()) {
            for(Map<Integer, T> gridy : gridx.values()) {
                list.addAll(gridy.values());
            }
        }
        return list;
    }

}
