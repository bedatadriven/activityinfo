/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.generator.map;

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
