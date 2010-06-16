/**
 * Created on Jun 4, 2008
 */
package com.ebessette.maps.core.client.overlay;

import com.ebessette.maps.core.client.ZoomLevelConstants;
import com.google.gwt.maps.client.geom.Size;

/**
 * Optional arguments to the {@link OverlayManager} class.
 * @author Eric Bessette <dev@ebessette.com>
 */
public class OverlayManagerOptions {

	/**
	 * The default maximum zoom level
	 */
	protected static final int	DEFAULT_MAX_ZOOM		= ZoomLevelConstants.ZOOM_MAXIMUM;

	/**
	 * The default border padding value, in pixels
	 */
	protected static final int	DEFAULT_BORDER_PADDING	= 100;

	/**
	 * Specifies, in pixels, the extra padding outside the map's current
	 * viewport monitored by a manager. Markers that fall within this padding
	 * are added to the map, even if they are not fully visible.
	 */
	protected int				borderPadding;

	/**
	 * Sets the maximum zoom level monitored by a marker manager. If not given,
	 * the manager assumes the maximum map zoom level. This value is also used
	 * when markers are added to the manager without the optional maxZoom
	 * parameter.
	 */
	protected int				maxZoom;

	/**
	 * Indicates whether or not a marker manager should track markers'
	 * movements. If you wish to move managed markers using the setPoint method,
	 * this option should be set to true. The default value is false.
	 */
	protected boolean			trackMarkers;

	/**
	 * The SouthWest padding
	 */
	protected Size				swPad;

	/**
	 * The NorthEast padding
	 */
	protected Size				nePad;

	/**
	 * Creates a new Marker Manager Options instance
	 */
	public OverlayManagerOptions() {

		setBorderPadding( DEFAULT_BORDER_PADDING );
		setMaxZoom( DEFAULT_MAX_ZOOM );
		setTrackMarkers( false );
	}

	/**
	 * Creates a new Marker Manager Options instance
	 * @param borderPadding
	 * @param maxZoom
	 * @param trackMarkers
	 */
	public OverlayManagerOptions( int borderPadding, int maxZoom, boolean trackMarkers ) {

		this();

		setBorderPadding( borderPadding );
		setMaxZoom( maxZoom );
		setTrackMarkers( trackMarkers );
	}

	/**
	 * Gets the borderPadding
	 * @return Returns the borderPadding.
	 */
	public int getBorderPadding() {

		return this.borderPadding;
	}

	/**
	 * Sets the borderPadding
	 * @param borderPadding The borderPadding to set.
	 */
	public void setBorderPadding( int borderPadding ) {

		if ( borderPadding < 0 ) {
			return;
		}

		this.borderPadding = borderPadding;
		this.swPad = Size.newInstance( -1 * this.borderPadding, this.borderPadding );
		this.nePad = Size.newInstance( this.borderPadding, -1 * this.borderPadding );
	}

	/**
	 * Gets the maxZoom
	 * @return Returns the maxZoom.
	 */
	public int getMaxZoom() {

		return this.maxZoom;
	}

	/**
	 * Sets the maxZoom
	 * @param maxZoom The maxZoom to set.
	 */
	public void setMaxZoom( int maxZoom ) {

		if ( maxZoom < 0 ) {
			return;
		}

		this.maxZoom = maxZoom;
	}

	/**
	 * Gets the trackMarkers
	 * @return Returns the trackMarkers.
	 */
	public boolean isTrackMarkers() {

		return this.trackMarkers;
	}

	/**
	 * Sets the trackMarkers
	 * @param trackMarkers The trackMarkers to set.
	 */
	public void setTrackMarkers( boolean trackMarkers ) {

		this.trackMarkers = trackMarkers;
	}

	/**
	 * Gets the swPad
	 * @return Returns the swPad.
	 */
	public Size getSwPad() {

		return this.swPad;
	}

	/**
	 * Gets the nePad
	 * @return Returns the nePad.
	 */
	public Size getNePad() {

		return this.nePad;
	}

}
