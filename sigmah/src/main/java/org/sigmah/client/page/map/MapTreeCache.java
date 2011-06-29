package org.sigmah.client.page.map;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.ComputeMapTree;
import org.sigmah.shared.command.result.MapTree;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;

import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Maintains an incrementally loaded map tree.
 * @author alexander
 *
 */
public class MapTreeCache {

	private MapTree.Node root;
	private MapWidget mapWidget;
	
	
	public MapTreeCache(Dispatcher dispatcher) {
		dispatcher.execute(new ComputeMapTree(), null, new AsyncCallback<MapTree>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(MapTree result) {
				root = result.getRoot();
			}
		});
	}
	
	/**
	 * Called when 
	 * @param bounds
	 * @param zoom
	 */
	public void onMapViewChanged(BoundingBoxDTO bounds, int zoom) {
		
		
		
	}
	
	
}
