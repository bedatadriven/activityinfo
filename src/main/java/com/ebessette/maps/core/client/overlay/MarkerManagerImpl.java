/**
 * Created on May 28, 2008
 */
package com.ebessette.maps.core.client.overlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.geom.Projection;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.DeferredCommand;

/**
 * Manage the visibility of markers on a map, based on the map's current view
 * port and zoom level<br>
 * Note, this is a direct port of markermanger.js found at
 * http://code.google.com/p/gmaps-utility-library/.
 * @author Eric Bessette <dev@ebessette.com>
 */
public class MarkerManagerImpl implements OverlayManager<Marker> {

	/**
	 * A size with no width or height
	 */
	private static final Size SIZE_ZERO	= Size.newInstance( 0, 0 );

	/**
	 * The Google Map instance
	 */
	protected MapWidget map;

	/**
	 * The options for this manager
	 */
	protected OverlayManagerOptions	opts;

	/**
	 * The map's current zoom level
	 */
	private int	mapZoom;

	/**
	 * The map's current projection type
	 */
	private Projection projection;

	/**
	 * The tile size
	 */
	private int	tileSize;

	/**
	 * A hash of grid widths by zoom level
	 */
	private HashMap<Integer, Integer> gridWidth;

	/**
	 * The structure that holds the markers
	 */
	private HashMap<Integer, HashMap<Integer, HashMap<Integer, List<Marker>>>>	grid;

	/**
	 * A hash of marker counts by zoom level
	 */
	private HashMap<Integer, Integer> numMarkers;

	/**
	 * The number of marker currently being displayed
	 */
	private int  shownMarkers;

	/**
	 * The current bounds and zoom level being shown
	 */
	private GridBounds shownBounds;

	/**
	 * Creates a new Marker Manager
	 * @param map The map widget
	 */
	public MarkerManagerImpl( MapWidget map ) {

		// var me = this;
		// me.map_ = map;
		this.map = map;

		initialize();
	}

	/**
	 * Creates a new Marker Manager
	 * @param map The map widget
	 * @param opts The marker manager options
	 */
	public MarkerManagerImpl( MapWidget map, OverlayManagerOptions opts ) {

		this( map );

		this.opts = opts;

		initialize();
	}

	/**
	 * 
	 */
	private void initialize() {

		// me.mapZoom_ = map.getZoom();
		this.mapZoom = map.getZoomLevel();
		// me.projection_ = map.getCurrentMapType().getProjection();
		this.projection = map.getCurrentMapType().getProjection();
		// opt_opts = opt_opts || {};
		if ( this.opts == null ) {
			this.opts = new OverlayManagerOptions();
		}
		// me.tileSize_ = MarkerManager.DEFAULT_TILE_SIZE_;
		this.tileSize = DEFAULT_TILE_SIZE;
		// var maxZoom = MarkerManager.DEFAULT_MAX_ZOOM_;
		// if(opt_opts.maxZoom != undefined) {
		// maxZoom = opt_opts.maxZoom;
		// }
		// me.maxZoom_ = maxZoom;
		//
		// me.trackMarkers_ = opt_opts.trackMarkers;
		//
		// var padding;
		// if (typeof opt_opts.borderPadding == "number") {
		// padding = opt_opts.borderPadding;
		// } else {
		// padding = MarkerManager.DEFAULT_BORDER_PADDING_;
		// }
		// // The padding in pixels beyond the viewport, where we will pre-load
		// markers.
		// me.swPadding_ = new GSize(-padding, padding);
		// me.nePadding_ = new GSize(padding, -padding);
		// me.borderPadding_ = padding;
		//
		// me.gridWidth_ = [];
		this.gridWidth = new HashMap<Integer, Integer>();
		// me.grid_ = [];
		this.grid = new HashMap<Integer, HashMap<Integer, HashMap<Integer, List<Marker>>>>();
		// me.grid_[maxZoom] = [];
		this.grid.put( this.opts.getMaxZoom(), new HashMap<Integer, HashMap<Integer, List<Marker>>>() );
		// me.numMarkers_ = [];
		this.numMarkers = new HashMap<Integer, Integer>();
		// me.numMarkers_[maxZoom] = 0;
		this.numMarkers.put( this.opts.getMaxZoom(), 0 );
		// GEvent.bind(map, "moveend", me, me.onMapMoveEnd_);
		this.map.addMapMoveEndHandler( this );
		// me.resetManager_();
		resetManager();
		// me.shownMarkers_ = 0;
		this.shownMarkers = 0;
		// me.shownBounds_ = me.getMapGridBounds_();
		this.shownBounds = getMapGridBounds();
	}

	/**
	 * @param o The overlay to remove
	 */
	protected void removeMarkerInternal( Marker o ) {

		// // NOTE: These two closures provide easy access to the map.
		// // They are used as callbacks, not as methods.
		// me.removeOverlay_ = function(marker) {
		// map.removeOverlay(marker);
		// me.shownMarkers_--;
		// };
		this.map.removeOverlay( o );
		this.shownMarkers--;
	}

	/**
	 * @param o The overlay to add
	 */
	protected void addMarkerInternal( Marker o ) {

		// // NOTE: These two closures provide easy access to the map.
		// // They are used as callbacks, not as methods.
		// me.addOverlay_ = function(marker) {
		// map.addOverlay(marker);
		// me.shownMarkers_++;
		// };

		this.map.addOverlay( o );
		this.shownMarkers++;
	}

	/**
	 * Initializes the internal structures for all zoom levels
	 */
	protected void resetManager() {

		// var me = this;
		// var mapWidth = MarkerManager.MERCATOR_ZOOM_LEVEL_ZERO_RANGE;
		int mapWidth = MERCATOR_ZOOM_LEVEL_ZERO_RANGE;
		// for (var zoom = 0; zoom <= me.maxZoom_; ++zoom) {
		for ( int zoom = 0; zoom <= this.opts.getMaxZoom(); zoom++ ) {
			// me.grid_[zoom] = [];
			this.grid.put( zoom, new HashMap<Integer, HashMap<Integer, List<Marker>>>() );
			// me.numMarkers_[zoom] = 0;
			this.numMarkers.put( zoom, 0 );
			// me.gridWidth_[zoom] = Math.ceil(mapWidth/me.tileSize_);
			this.gridWidth.put( zoom, (int) Math.ceil( mapWidth / this.tileSize ) );
			// mapWidth <<= 1;
			mapWidth <<= 1;
			// }
		}
	}

	/**
	 * Removes all currently displayed markers and resets the manager
	 */
	protected void clearMarkers() {

		// var me = this;
		// me.processAll_(me.shownBounds_, me.removeOverlay_);
		processAll( this.shownBounds, "removeMarkerInternal" );
		// me.resetManager_();
		resetManager();
	}

	/**
	 * Gets the tile coordinate for a given a latitude/longitude coordinate.
	 * @param latlng The geographical point.
	 * @param zoom The zoom level.
	 * @param padding The padding used to shift the pixel coordinate. Used for
	 *        expanding a bounds to include an extra padding of pixels
	 *        surrounding the bounds.
	 * @return The point in tile coordinates.
	 */
	protected Point getTilePoint( LatLng latlng, int zoom, Size padding ) {

		// var pixelPoint = this.projection_.fromLatLngToPixel(latlng, zoom);
		Point p = this.projection.fromLatLngToPixel( latlng, zoom );
        if(p == null) {
            return null;
        }
        // return new GPoint(
        // Math.floor((pixelPoint.x + padding.width) / this.tileSize_),
        // Math.floor((pixelPoint.y + padding.height) / this.tileSize_));
        int x = (int) Math.floor( ( p.getX() + padding.getWidth() ) / this.tileSize );
        int y = (int) Math.floor( ( p.getY() + padding.getHeight() ) / this.tileSize );
        return Point.newInstance( x, y );

	}

	/**
	 * Finds the appropriate place to add the marker to the grid. Optimized for
	 * speed; does not actually add the marker to the map. Designed for
	 * batch-processing thousands of markers.
	 * @param marker The overlay to add.
	 * @param minZoom The minimum zoom for displaying the marker.
	 * @param maxZoom The maximum zoom for displaying the marker.
	 */
	protected void addMarkerBatch( Marker marker, int minZoom, int maxZoom ) {

		maxZoom = ( maxZoom <= this.opts.getMaxZoom() ) ? maxZoom : this.opts.getMaxZoom();
		// var mPoint = marker.getPoint();
		LatLng mPoint = marker.getLatLng();
		// // Tracking markers is expensive, so we do this only if the
		// // user explicitly requested it when creating marker manager.
		// if (this.trackMarkers_) {
		if ( this.opts.isTrackMarkers() ) {
			// GEvent.bind(marker, "changed", this, this.onMarkerMoved_);
			// TODO will have to implement this later
			// }
		}
		// var gridPoint = this.getTilePoint_(mPoint, maxZoom, GSize.ZERO);
		Point gridPoint = this.getTilePoint( mPoint, maxZoom, SIZE_ZERO );
		// for (var zoom = maxZoom; zoom >= minZoom; zoom--) {
		for ( int zoom = maxZoom; zoom >= minZoom; zoom-- ) {
			// var cell = this.getGridCellCreate_(gridPoint.x, gridPoint.y,
			// zoom);
			List<Marker> cell = this.getGridCellCreate( gridPoint.getX(), gridPoint.getY(), zoom );
			// cell.push(marker);
			cell.add( marker );
			// gridPoint.x = gridPoint.x >> 1;
			// gridPoint.y = gridPoint.y >> 1;
			gridPoint = Point.newInstance( gridPoint.getX() >> 1, gridPoint.getY() >> 1 );
			// }
		}
	}

	/**
	 * Returns whether or not the given point is visible in the shown bounds.
	 * This is a helper method that takes care of the corner case, when
	 * shownBounds have negative minX value.
	 * @param point a point on a grid.
	 * @return Whether or not the given point is visible in the currently shown
	 *         bounds.
	 */
	protected boolean isGridPointVisible( Point point ) {

		// var me = this;
		// var vertical = me.shownBounds_.minY <= point.y &&
		// point.y <= me.shownBounds_.maxY;
		boolean vertical = ( this.shownBounds.getMinY() <= point.getY() && point.getY() <= this.shownBounds.getMaxY() );
		// var minX = me.shownBounds_.minX;
		int minX = this.shownBounds.getMinX();
		// var horizontal = minX <= point.x && point.x <= me.shownBounds_.maxX;
		boolean horizontal = ( minX <= point.getX() && point.getX() <= this.shownBounds.getMaxX() );
		// if (!horizontal && minX < 0) {
		if ( !horizontal && minX < 0 ) {
			// // Shifts the negative part of the rectangle. As point.x is
			// always
			// less
			// // than grid width, only test shifted minX .. 0 part of the shown
			// bounds.
			// var width = me.gridWidth_[me.shownBounds_.z];
			int width = this.gridWidth.get( this.shownBounds.getZoom() );
			// horizontal = minX + width <= point.x && point.x <= width - 1;
			horizontal = ( minX + width <= point.getX() && point.getX() <= width - 1 );
			// }
		}
		// return vertical && horizontal;
		return vertical && horizontal;
	}

	/**
	 * Reacts to a notification from a marker that it has moved to a new
	 * location. It scans the grid all all zoom levels and moves the marker from
	 * the old grid location to a new grid location.
	 * @param marker The marker that moved.
	 * @param oldPoint The old position of the marker.
	 * @param newPoint The new position of the marker.
	 */
	protected void onMarkerMoved( Marker marker, LatLng oldPoint, LatLng newPoint ) {

		// TODO not sure how this is used

		// // NOTE: We do not know the minimum or maximum zoom the marker was
		// // added at, so we start at the absolute maximum. Whenever we
		// successfully
		// // remove a marker at a given zoom, we add it at the new grid
		// coordinates.
		// var me = this;
		// var zoom = me.maxZoom_;
		int zoom = this.opts.getMaxZoom();
		// var changed = false;
		boolean changed = false;
		// var oldGrid = me.getTilePoint_(oldPoint, zoom, GSize.ZERO);
		Point oldGrid = getTilePoint( oldPoint, zoom, SIZE_ZERO );
		// var newGrid = me.getTilePoint_(newPoint, zoom, GSize.ZERO);
		Point newGrid = getTilePoint( newPoint, zoom, SIZE_ZERO );
		// while (zoom >= 0 && (oldGrid.x != newGrid.x || oldGrid.y !=
		// newGrid.y)) {
		while ( zoom >= 0 && ( oldGrid.getX() != newGrid.getX() || oldGrid.getY() != newGrid.getY() ) ) {
			// var cell = me.getGridCellNoCreate_(oldGrid.x, oldGrid.y, zoom);
			List<Marker> cell = getGridCellNoCreate( oldGrid.getX(), oldGrid.getY(), zoom );
			// if (cell) {
			if ( cell != null ) {
				// if (me.removeFromArray(cell, marker)) {
				if ( cell.remove( marker ) ) {
					// me.getGridCellCreate_(newGrid.x, newGrid.y,
					// zoom).push(marker);
					getGridCellCreate( newGrid.getX(), newGrid.getY(), zoom ).add( marker );
					// }
				}
				// }
			}
			// // For the current zoom we also need to update the map. Markers
			// that
			// no
			// // longer are visible are removed from the map. Markers that
			// moved
			// into
			// // the shown bounds are added to the map. This also lets us keep
			// the
			// count
			// // of visible markers up to date.
			// if (zoom == me.mapZoom_) {
			if ( zoom == this.mapZoom ) {
				// if (me.isGridPointVisible_(oldGrid)) {
				if ( isGridPointVisible( oldGrid ) ) {
					// if (!me.isGridPointVisible_(newGrid)) {
					if ( !isGridPointVisible( newGrid ) ) {
						// me.removeOverlay_(marker);
						this.removeMarkerInternal( marker );
						// changed = true;
						changed = true;
						// }
					}
					// } else {
				}
				else {
					// if (me.isGridPointVisible_(newGrid)) {
					if ( isGridPointVisible( newGrid ) ) {
						// me.addOverlay_(marker);
						this.addMarkerInternal( marker );
						// changed = true;
						changed = true;
						// }
					}
					// }
				}
				// }
			}
			// oldGrid.x = oldGrid.x >> 1;
			// oldGrid.y = oldGrid.y >> 1;
			oldGrid = Point.newInstance( oldGrid.getX() >> 1, oldGrid.getY() >> 1 );
			// newGrid.x = newGrid.x >> 1;
			// newGrid.y = newGrid.y >> 1;
			oldGrid = Point.newInstance(newGrid.getX() >> 1, newGrid.getY() >> 1 );
			// --zoom;
			zoom--;
			// }
		}
		// if (changed) {
		if ( changed ) {
			// me.notifyListeners_();
			notifyListeners();
			// }
		}
	}

	/**
	 * Searches at every zoom level to find grid cell that marker would be in,
	 * removes from that array if found. Also removes marker with removeOverlay
	 * if visible.
	 * @param marker The marker to delete.
	 */
	public void removeMarker( Marker marker ) {

		// var me = this;
		// var zoom = me.maxZoom_;
		int zoom = this.opts.getMaxZoom();
		// var changed = false;
		boolean changed = false;
		// var point = marker.getPoint();
		LatLng point = marker.getLatLng();
		// var grid = me.getTilePoint_(point, zoom, GSize.ZERO);
		Point grid = getTilePoint( point, zoom, SIZE_ZERO );
		// while (zoom >= 0) {
		while ( zoom >= 0 ) {
			// var cell = me.getGridCellNoCreate_(grid.x, grid.y, zoom);
			List<Marker> cell = getGridCellNoCreate( grid.getX(), grid.getY(), zoom );
			// if (cell) {
			if ( cell != null ) {
				// me.removeFromArray(cell, marker);
				cell.remove( marker );
				// }
			}
			// // For the current zoom we also need to update the map. Markers
			// that
			// no
			// // longer are visible are removed from the map. This also lets us
			// keep the count
			// // of visible markers up to date.
			// if (zoom == me.mapZoom_) {
			if ( zoom == this.mapZoom ) {
				// if (me.isGridPointVisible_(grid)) {
				if ( isGridPointVisible( grid ) ) {
					// me.removeOverlay_(marker);
					removeMarkerInternal( marker );
					// changed = true;
					changed = true;
					// }
				}
				// }
			}
			// grid.x = grid.x >> 1;
			// grid.y = grid.y >> 1;
			grid = Point.newInstance( grid.getX() >> 1, grid.getY() >> 1 );
			// --zoom;
			zoom--;
			// }
		}
		// if (changed) {
		if ( changed ) {
			// me.notifyListeners_();
			notifyListeners();
			// }
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebessette.maps.core.client.overlay.OverlayManager#addOverlays(java.util.List,
	 *      int)
	 */
	public void addOverlays( List<Marker> markers, int minZoom ) {

		addOverlays( markers, minZoom, this.opts.getMaxZoom() );
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebessette.maps.core.client.overlay.OverlayManager#addOverlays(java.util.List,
	 *      int, int)
	 */
	public void addOverlays( List<Marker> markers, int minZoom, int maxZoom ) {

		// var maxZoom = this.getOptMaxZoom_(opt_maxZoom);
		// for (var i = markers.length - 1; i >= 0; i--) {
		for ( int i = markers.size() - 1; i >= 0; i-- ) {
			// this.addMarkerBatch_(markers[i], minZoom, maxZoom);
			addMarkerBatch( markers.get( i ), minZoom, maxZoom );
			// }
		}
		// this.numMarkers_[minZoom] += markers.length;
		this.numMarkers.put( minZoom, markers.size() );
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebessette.maps.core.client.overlay.OverlayManager#getOverlayCount(int)
	 */
	public int getOverlayCount( int zoom ) {

		// var total = 0;
		int total = 0;
		// for (var z = 0; z <= zoom; z++) {
		for ( int z = 0; z <= zoom; z++ ) {
			// total += this.numMarkers_[z];
			total += this.numMarkers.get( z );
			// }
		}
		// return total;
		return total;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebessette.maps.core.client.overlay.OverlayManager#addOverlay(com.google.gwt.maps.client.overlay.Overlay,
	 *      int)
	 */
	public void addOverlay( Marker overlay, int minZoom ) {

		addOverlay( overlay, minZoom, this.opts.getMaxZoom() );

	}

	/*
	 * (non-Javadoc)
	 * @see com.ebessette.maps.core.client.overlay.OverlayManager#addOverlay(com.google.gwt.maps.client.overlay.Overlay,
	 *      int, int)
	 */
	public void addOverlay( Marker overlay, int minZoom, int maxZoom ) {

		// var me = this;
		// var maxZoom = this.getOptMaxZoom_(opt_maxZoom);
		// me.addMarkerBatch_(marker, minZoom, maxZoom);
		addMarkerBatch( overlay, minZoom, maxZoom );
		// var gridPoint = me.getTilePoint_(marker.getPoint(), me.mapZoom_,
		// GSize.ZERO);
		Point gridPoint = getTilePoint( overlay.getLatLng(), this.mapZoom, SIZE_ZERO );
		// if(me.isGridPointVisible_(gridPoint) &&
		// minZoom <= me.shownBounds_.z &&
		// me.shownBounds_.z <= maxZoom ) {
		if ( isGridPointVisible( gridPoint ) && minZoom < this.shownBounds.getZoom() && this.shownBounds.getZoom() <= maxZoom ) {
			// me.addOverlay_(marker);
			addMarkerInternal( overlay );
			// me.notifyListeners_();
			notifyListeners();
			// }
		}
		// this.numMarkers_[minZoom]++;
		this.numMarkers.put( minZoom, this.numMarkers.get( minZoom ) + 1 );
	}

	/**
	 * Get a cell in the grid, creating it first if necessary. Optimization
	 * candidate
	 * @param x The x coordinate of the cell.
	 * @param y The y coordinate of the cell.
	 * @param z The z coordinate of the cell.
	 * @return The cell in the array.
	 */
	protected List<Marker> getGridCellCreate( int x, int y, int z ) {

		// var grid = this.grid_[z];
		HashMap<Integer, HashMap<Integer, List<Marker>>> grid = this.grid.get( z );
		// if (x < 0) {
		if ( x < 0 ) {
			// x += this.gridWidth_[z];
			x += this.gridWidth.get( z );
			// }
		}
		// var gridCol = grid[x];
		HashMap<Integer, List<Marker>> gridCol = grid.get( x );
		// if (!gridCol) {
		if ( gridCol == null ) {
			// gridCol = grid[x] = [];
			gridCol = new HashMap<Integer, List<Marker>>();
			grid.put( x, gridCol );
			// return gridCol[y] = [];
			List<Marker> gridColY = new ArrayList<Marker>();
			gridCol.put( y, gridColY );
			return gridColY;
			// }
		}
		// var gridCell = gridCol[y];
		List<Marker> gridCell = gridCol.get( y );
		// if (!gridCell) {
		if ( gridCell == null ) {
			// return gridCol[y] = [];
			List<Marker> gridColY = new ArrayList<Marker>();
			gridCol.put( y, gridColY );
			return gridColY;
			// }
		}
		// return gridCell;
		return gridCell;
	}

	/**
	 * Get a cell in the grid, returning undefined if it does not exist.<br>
	 * NOTE: Optimized for speed -- otherwise could combine with
	 * MarkerManager.getGridCellCreate.
	 * @param x The x coordinate of the cell.
	 * @param y The y coordinate of the cell.
	 * @param z The z coordinate of the cell.
	 * @return The cell in the array.
	 */
	protected List<Marker> getGridCellNoCreate( int x, int y, int z ) {

		// var grid = this.grid_[ z ];
		HashMap<Integer, HashMap<Integer, List<Marker>>> grid = this.grid.get( z );
		// if ( x < 0 ) {
		if ( x < 0 ) {
			// x += this.gridWidth_[ z ];
			x += this.gridWidth.get( z );
			// }
		}
		// var gridCol = grid[ x ];
		HashMap<Integer, List<Marker>> gridCol = grid.get( x );
		// return gridCol ? gridCol[ y ] : undefined;
		return ( ( gridCol != null ) ? gridCol.get( y ) : null );
	}

	/**
	 * Turns at geographical bounds into a grid-space bounds.
	 * @param bounds The geographical bounds.
	 * @param zoom The zoom level of the bounds.
	 * @param swPadding The padding in pixels to extend beyond the given bounds.
	 * @param nePadding The padding in pixels to extend beyond the given bounds.
	 * @return The bounds in grid space.
	 */
	protected GridBounds getGridBounds( LatLngBounds bounds, int zoom, Size swPadding, Size nePadding ) {

		// zoom = Math.min(zoom, this.maxZoom_);
		zoom = Math.min( zoom, this.opts.getMaxZoom() );
		// var bl = bounds.getSouthWest();
		LatLng bl = bounds.getSouthWest();
		// var tr = bounds.getNorthEast();
		LatLng tr = bounds.getNorthEast();
		// var sw = this.getTilePoint_(bl, zoom, swPadding);
		Point sw = getTilePoint( bl, zoom, swPadding );
		// var ne = this.getTilePoint_(tr, zoom, nePadding);
		Point ne = getTilePoint( tr, zoom, nePadding );
		// var gw = this.gridWidth_[zoom];
		int gw = this.gridWidth.get( zoom );
		// // Crossing the prime meridian requires correction of bounds.
		// if (tr.lng() < bl.lng() || ne.x < sw.x) {
		if ( tr.getLongitude() < bl.getLongitude() || ne.getX() < sw.getX() ) {
			// sw.x -= gw;
			sw = Point.newInstance( sw.getX() - gw, sw.getY() );
			// }
		}
		// if (ne.x - sw.x + 1 >= gw) {
		if ( ne.getX() - sw.getX() + 1 >= gw ) {
			// // Computed grid bounds are larger than the world; truncate.
			// sw.x = 0;
			sw = Point.newInstance( 0, sw.getY() );
			// ne.x = gw - 1;
			ne = Point.newInstance( gw - 1, ne.getY() );
			// }
		}
		// var gridBounds = new GBounds([sw, ne]);
		// gridBounds.z = zoom;
		GridBounds gridBounds = new GridBounds( sw, ne, zoom );
		// return gridBounds;
		return gridBounds;
	}

	/**
	 * Gets the grid-space bounds for the current map viewport.
	 * @return The bounds in grid space.
	 */
	protected GridBounds getMapGridBounds() {

		// var me = this;
		// return me.getGridBounds_(me.map_.getBounds(), me.mapZoom_,
		// me.swPadding_, me.nePadding_);
		return getGridBounds( this.map.getBounds(), this.map.getZoomLevel(), this.opts.getSwPad(), this.opts.getNePad() );
	};

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.maps.client.event.MapMoveEndHandler#onMoveEnd(com.google.gwt.maps.client.event.MapMoveEndHandler.MapMoveEndEvent)
	 */
	public void onMoveEnd( MapMoveEndEvent event ) {

		// var me = this;
		// me.objectSetTimeout_(this, this.updateMarkers_, 0);
		DeferredCommand.addCommand( this );

	}

	/*
	 * (non-Javadoc)
	 * @see com.ebessette.maps.core.client.overlay.OverlayManager#refresh()
	 */
	public void refresh() {

		// var me = this;
		// if (me.shownMarkers_ > 0) {
		if ( this.shownMarkers > 0 ) {
			// me.processAll_(me.shownBounds_, me.removeOverlay_);
			processAll( this.shownBounds, "removeMarkerInternal" );
			// }
		}
		// me.processAll_(me.shownBounds_, me.addOverlay_);
		processAll( this.shownBounds, "addMarkerInternal" );
		// me.notifyListeners_();
		notifyListeners();
	}

	/**
	 * After the viewport may have changed, add or remove markers as needed.<br>
	 * Note, should ALWAYS be called as a {@link DeferredCommand}
	 */
	protected void updateMarkers() {

		// var me = this;
		// me.mapZoom_ = this.map_.getZoom();
		this.mapZoom = this.map.getZoomLevel();
		// var newBounds = me.getMapGridBounds_();
		GridBounds newBounds = getMapGridBounds();
		// // If the move does not include new grid sections,
		// // we have no work to do:
		// if (newBounds.equals(me.shownBounds_) && newBounds.z ==
		// me.shownBounds_.z) {
		if ( newBounds.equals( this.shownBounds ) ) {
			// return;
			return;
			// }
		}
		// if (newBounds.z != me.shownBounds_.z) {
		if ( newBounds.getZoom() != this.shownBounds.getZoom() ) {
			// me.processAll_(me.shownBounds_, me.removeOverlay_);
			processAll( this.shownBounds, "removeMarkerInternal" );
			// me.processAll_(newBounds, me.addOverlay_);
			processAll( newBounds, "addMarkerInternal" );
			// } else {
		}
		else {
			// // Remove markers:
			// me.rectangleDiff_(me.shownBounds_, newBounds,
			// me.removeCellMarkers_);
			rectangleDiff( this.shownBounds, newBounds, "removeCellMarkers" );
			// // Add markers:
			// me.rectangleDiff_(newBounds, me.shownBounds_,
			// me.addCellMarkers_);
			rectangleDiff( this.shownBounds, newBounds, "addCellMarkers" );
			// }
		}
		// me.shownBounds_ = newBounds;
		this.shownBounds = newBounds;
		// me.notifyListeners_();
		notifyListeners();
	}

	/**
	 * Notify listeners when the state of what is displayed changes.
	 */
	private void notifyListeners() {

		// TODO implement this

		// GEvent.trigger(this, "changed", this.shownBounds_,
		// this.shownMarkers_);

	}

	/**
	 * Process all markers in the bounds provided, using a callback.
	 * @param bounds The bounds in grid space.
	 * @param callback The function to call for each marker.
	 */
	protected void processAll( GridBounds bounds, String callback ) {

		// for (var x = bounds.minX; x <= bounds.maxX; x++) {
		for ( int x = bounds.getMinX(); x <= bounds.getMaxX(); x++ ) {
			// for (var y = bounds.minY; y <= bounds.maxY; y++) {
			for ( int y = bounds.getMinY(); y <= bounds.getMaxY(); y++ ) {
				// this.processCellMarkers_(x, y, bounds.z, callback);
				processCellMarkers( x, y, bounds.getZoom(), callback );
				// }
			}
			// }
		}
	}

	/**
	 * Process all markers in the grid cell, using a callback.
	 * @param x The x coordinate of the cell.
	 * @param y The y coordinate of the cell.
	 * @param z The z coordinate of the cell.
	 * @param callback The function to call for each marker.
	 */
	protected void processCellMarkers( int x, int y, int z, String callback ) {

		// var cell = this.getGridCellNoCreate_(x, y, z);
		List<Marker> cell = getGridCellNoCreate( x, y, z );
		// if (cell) {
		if ( cell != null ) {
			// for (var i = cell.length - 1; i >= 0; i--) {
			for ( Iterator<Marker> im = cell.iterator(); im.hasNext(); ) {
				// callback(cell[i]);
				if ( callback.equals( "removeMarkerInternal" ) ) {
					removeMarkerInternal( im.next() );
				}
				if ( callback.equals( "addMarkerInternal" ) ) {
					addMarkerInternal( im.next() );
				}
				// }
			}
			// }
		}
	}

	/**
	 * Remove all markers in a grid cell.
	 * @param x The x coordinate of the cell.
	 * @param y The y coordinate of the cell.
	 * @param z The z coordinate of the cell.
	 */
	protected void removeCellMarkers( int x, int y, int z ) {

		// this.processCellMarkers_(x, y, z, this.removeOverlay_);
		processCellMarkers( x, y, z, "removeMarkerInternal" );
	}

	/**
	 * Add all markers in a grid cell.
	 * @param x The x coordinate of the cell.
	 * @param y The y coordinate of the cell.
	 * @param z The z coordinate of the cell.
	 */
	protected void addCellMarkers( int x, int y, int z ) {

		// this.processCellMarkers_(x, y, z, this.addOverlay_);
		processCellMarkers( x, y, z, "addMarkerInternal" );
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.Command#execute()
	 */
	public void execute() {

		updateMarkers();
	}

	/**
	 * Use the rectangleDiffCoords function to process all grid cells that are
	 * in bounds1 but not bounds2, using a callback, and using the current
	 * MarkerManager object as the instance. Pass the z parameter to the
	 * callback in addition to x and y.
	 * @param bounds1 The bounds of all points we may process.
	 * @param bounds2 The bounds of points to exclude.
	 * @param callback The callback function to call for each grid coordinate
	 *        (x, y, z).
	 */
	protected void rectangleDiff( GridBounds bounds1, GridBounds bounds2, String callback ) {

		// var me = this;
		// me.rectangleDiffCoords(bounds1, bounds2, function(x, y) {
		// callback.apply(me, [x, y, bounds1.z]);
		// });
		rectangleDiffCoords( bounds1, bounds2, bounds1.getZoom(), callback );
	}

	/**
	 * Calls the function for all points in bounds1, not in bounds2
	 * @param bounds1 The bounds of all points we may process.
	 * @param bounds2 The bounds of points to exclude.
	 * @param zoom The zoom to use
	 * @param callback The callback function to call for each grid coordinate.
	 */
	protected void rectangleDiffCoords( GridBounds bounds1, GridBounds bounds2, int zoom, String callback ) {

		// var minX1 = bounds1.minX;
		int minX1 = bounds1.getMinX();
		// var minY1 = bounds1.minY;
		int minY1 = bounds1.getMinY();
		// var maxX1 = bounds1.maxX;
		int maxX1 = bounds1.getMaxX();
		// var maxY1 = bounds1.maxY;
		int maxY1 = bounds1.getMaxY();
		// var minX2 = bounds2.minX;
		int minX2 = bounds2.getMinX();
		// var minY2 = bounds2.minY;
		int minY2 = bounds2.getMinY();
		// var maxX2 = bounds2.maxX;
		int maxX2 = bounds2.getMaxX();
		// var maxY2 = bounds2.maxY;
		int maxY2 = bounds2.getMaxY();
		// for (var x = minX1; x <= maxX1; x++) { // All x in R1
		for ( int x = minX1; x <= maxX1; x++ ) {
			// // All above:
			// for (var y = minY1; y <= maxY1 && y < minY2; y++) { // y in R1
			// above
			// R2
			for ( int y = minY1; y <= maxY1 && y < minY2; y++ ) {
				// callback(x, y);
				if ( callback.equals( "removeCellMarkers" ) ) {
					removeCellMarkers( x, y, zoom );
				}
				if ( callback.equals( "addCellMarkers" ) ) {
					addCellMarkers( x, y, zoom );
				}
				// }
			}
			// // All below:
			// for (var y = Math.max(maxY2 + 1, minY1); // y in R1 below R2
			// y <= maxY1; y++) {
			for ( int y = Math.max( maxY2 + 1, minY1 ); y < maxY1; y++ ) {
				// callback(x, y);
				if ( callback.equals( "removeCellMarkers" ) ) {
					removeCellMarkers( x, y, zoom );
				}
				if ( callback.equals( "addCellMarkers" ) ) {
					addCellMarkers( x, y, zoom );
				}
				// }
			}
			// }
		}
		// for (var y = Math.max(minY1, minY2);
		// y <= Math.min(maxY1, maxY2); y++) { // All y in R2 and in R1
		for ( int y = Math.max( minY1, minY2 ); y <= Math.min( maxY1, maxY2 ); y++ ) {
			// // Strictly left:
			// for (var x = Math.min(maxX1 + 1, minX2) - 1;
			// x >= minX1; x--) { // x in R1 left of R2
			for ( int x = Math.min( maxX1 + 1, minX2 ) - 1; x >= minX1; x-- ) {
				// callback(x, y);
				if ( callback.equals( "removeCellMarkers" ) ) {
					removeCellMarkers( x, y, zoom );
				}
				if ( callback.equals( "addCellMarkers" ) ) {
					addCellMarkers( x, y, zoom );
				}
				// }
			}
			// // Strictly right:
			// for (var x = Math.max(minX1, maxX2 + 1); // x in R1 right of R2
			// x <= maxX1; x++) {
			for ( int x = Math.max( minX1, maxX2 + 1 ); x <= maxX1; x++ ) {
				// callback(x, y);
				if ( callback.equals( "removeCellMarkers" ) ) {
					removeCellMarkers( x, y, zoom );
				}
				if ( callback.equals( "addCellMarkers" ) ) {
					addCellMarkers( x, y, zoom );
				}
				// }
			}
			// }
		}
	}
}

