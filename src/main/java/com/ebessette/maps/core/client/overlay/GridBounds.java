package com.ebessette.maps.core.client.overlay;

import com.google.gwt.maps.client.geom.Bounds;
import com.google.gwt.maps.client.geom.Point;

/**
 * @author Eric Bessette <dev@ebessette.com>
 */
class GridBounds {

	/**
	 * The bounding box
	 */
	private Bounds bounds;

	/**
	 * The zoom level
	 */
	private int		zoom;

	/**
	 * Creates a new Grid Bounds
	 */
	public GridBounds() {

		this.bounds = null;
		this.zoom = 0;
	}

	/**
	 * Creates a new Grid Bounds
	 * @param b The bounding box
	 * @param z The zoom level
	 */
	public GridBounds( Bounds b, int z ) {

		this();

		this.bounds = b;
		this.zoom = z;
	}

	/**
	 * Creates a new Grid Bounds
	 * @param sw The SouthWest corner
	 * @param ne The NorthEast corner
	 * @param z The zoom level
	 */
	public GridBounds( Point sw, Point ne, int z ) {

		this( Bounds.newInstance( sw.getX(), sw.getY(), ne.getX(), ne.getY() ), z );
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object obj ) {

		if ( !( obj instanceof GridBounds ) ) {
			return false;
		}

		GridBounds other = (GridBounds) obj;
		return ( this.bounds.equals( other.bounds ) && this.zoom == other.zoom );
	}

	/**
	 * @return
	 */
	public int getMaxY() {

		return this.bounds.getMaxY();
	}

	/**
	 * @return
	 */
	public int getMaxX() {

		return this.bounds.getMaxX();
	}

	/**
	 * @return
	 */
	public int getMinY() {

		return this.bounds.getMinY();
	}

	/**
	 * @return
	 */
	public int getMinX() {

		return this.bounds.getMinX();
	}

	/**
	 * Gets the bounds
	 * @return Returns the bounds.
	 */
	public Bounds getBounds() {

		return this.bounds;
	}

	/**
	 * Gets the zoom
	 * @return Returns the zoom.
	 */
	public int getZoom() {

		return this.zoom;
	}

}
