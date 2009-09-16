package org.activityinfo.server.report.generator.map;
/*
 * @author Alex Bertram
 */

public class Margins {

    public int left;
    public int top;
    public int bottom;
    public int right;

    public Margins() {
    }

    public Margins(int size) {
        this.left = size;
        this.top = size;
        this.bottom = size;
        this.right = size;
    }

    public Margins(int left, int top, int bottom, int right) {
        this.left = left;
        this.top = top;
        this.bottom = bottom;
        this.right = right;
    }

    public void grow(Margins other) {
        if(other.left > this.left) this.left = other.left;
        if(other.right > this.right) this.right = other.right;
        if(other.top > this.top) this.top = other.top;
        if(other.bottom > this.bottom) this.bottom = other.bottom;
    }

}
