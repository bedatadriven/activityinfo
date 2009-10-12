/**
 * Created on Jun 7, 2008
 */
package com.ebessette.maps.core.client.overlay;

import com.google.gwt.maps.client.event.MapMoveEndHandler;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.user.client.Command;

import java.util.List;

/**
 * Manage the visibility of overlays on a map, based on the map's current view
 * port and zoom level
 * @author Eric Bessette <dev@ebessette.com>
 * @param <O> The type of overlay being managed
 */
public interface OverlayManager<O extends Overlay> extends MapMoveEndHandler, Command {

	/**
	 * The default tile size
	 */
	public static final int	DEFAULT_TILE_SIZE				= 1024;

	/**
	 * The Mercator zoom level zero range
	 */
	public static final int	MERCATOR_ZOOM_LEVEL_ZERO_RANGE	= 256;

	/**
	 * Add a single overlay to the map.
	 * @param overlay The overlay to add.
	 * @param minZoom The minimum zoom level to display the marker.
	 */
	public void addOverlay( O overlay, int minZoom );

	/**
	 * Add a single overlay to the map.
	 * @param overlay The overlay to add.
	 * @param minZoom The minimum zoom level to display the marker.
	 * @param maxZoom The maximum zoom level to display the marker.
	 */
	public void addOverlay( O overlay, int minZoom, int maxZoom );

	/**
	 * Add many overlays at once. Does not actually update the map, just the
	 * internal grid.
	 * @param overlays The overlays to add.
	 * @param minZoom The minimum zoom level to display the markers.
	 */
	public void addOverlays( List<O> overlays, int minZoom );

	/**
	 * Add many overlays at once. Does not actually update the map, just the
	 * internal grid.
	 * @param overlays The overlays to add.
	 * @param minZoom The minimum zoom level to display the markers.
	 * @param maxZoom The maximum zoom level to display the markers.
	 */
	public void addOverlays( List<O> overlays, int minZoom, int maxZoom );

	/**
	 * Calculates the total number of markers potentially visible at a given
	 * zoom level.
	 * @param zoom The zoom level to check.
	 * @return The total number of markers potentially visible
	 */
	public int getOverlayCount( int zoom );

	/**
	 * Refresh forces the marker-manager into a good state.
	 * <ol>
	 * <li>If never before initialized, shows all the markers.</li>
	 * <li>If previously initialized, removes and re-adds all markers.</li>
	 * </ol>
	 */
	public void refresh();
}
