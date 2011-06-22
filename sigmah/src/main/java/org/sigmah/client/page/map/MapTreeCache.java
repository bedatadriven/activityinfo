package org.sigmah.client.page.map;

import org.sigmah.shared.command.result.MapTree;
import org.sigmah.shared.dto.BoundingBoxDTO;

/**
 * Maintains an incrementally loaded map tree.
 * @author alexander
 *
 */
public class MapTreeCache {

	private MapTree.Node root;
	
	
	public MapTreeCache() {
		root = new MapTree.Node();
		root.setBounds(new BoundingBoxDTO(-180, -90, 180, 90));
	}
	
	/**
	 * Called when 
	 * @param bounds
	 * @param zoom
	 */
	public void onMapViewChanged(BoundingBoxDTO bounds, int zoom) {
		
		
		
	}
	
	
}
