package org.sigmah.client.page.common;

import org.sigmah.shared.dto.ProvidesKey;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;

/*
 * Helper class for GXT TreePanels and TreeGrids to provide a key based on unicity over
 * multiple entities
 */
public class ProvidesKeyProvider<M extends ModelData> implements ModelKeyProvider<M> {
	@Override
	public String getKey(M model) {
		if (model instanceof ProvidesKey) {
			return ((ProvidesKey)model).getKey();
		}
		
		throw new RuntimeException("Expected ModelData instance to implement ProvidesKey");
	}
}
