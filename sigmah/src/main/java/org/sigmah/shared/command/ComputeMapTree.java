package org.sigmah.shared.command;

import org.sigmah.shared.command.result.MapTree;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;

/**
 * Computes a heirarchy of points visible to the user that can be used
 * for incremental construction of multi-resolution maps.
 * 
 * @author alexander
 *
 */
public class ComputeMapTree implements Command<MapTree> {

	
	private BoundingBoxDTO bounds;

	/**
	 * 
	 * @return the geographic bounds of the current map view.
	 */
	public BoundingBoxDTO getBounds() {
		return bounds;
	}


	public void setBounds(BoundingBoxDTO bounds) {
		this.bounds = bounds;
	}
	
	
	
}
