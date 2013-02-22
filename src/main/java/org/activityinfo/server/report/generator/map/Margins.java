

package org.activityinfo.server.report.generator.map;

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

public final class Margins {

    private int left;
    private int top;
    private int bottom;
    private int right;

    public Margins() {
    }

    public Margins(int size) {
        this.setLeft(size);
        this.setTop(size);
        this.setBottom(size);
        this.setRight(size);
    }

    public Margins(int left, int top, int bottom, int right) {
        this.setLeft(left);
        this.setTop(top);
        this.setBottom(bottom);
        this.setRight(right);
    }

    public void grow(Margins other) {
        if(other.getLeft() > this.getLeft()) {
            this.setLeft(other.getLeft());
        }
        if(other.getRight() > this.getRight()) {
            this.setRight(other.getRight());
        }
        if(other.getTop() > this.getTop()) {
            this.setTop(other.getTop());
        }
        if(other.getBottom() > this.getBottom()) {
            this.setBottom(other.getBottom());
        }
    }

	public void setLeft(int left) {
		this.left = left;
	}

	public int getLeft() {
		return left;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getTop() {
		return top;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	public int getBottom() {
		return bottom;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public int getRight() {
		return right;
	}

}
