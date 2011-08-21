package org.sigmah.client.util.state;

import java.util.HashMap;
import java.util.Map;

import com.extjs.gxt.ui.client.state.Provider;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;
import com.google.inject.Singleton;

/**
 * GXT state provider that either takes advantage of the HTML5 LocalStorage or
 * saves stage to an in-memory HashMap.
 * 
 * This is used in place of the default CookieProvider because with TreePanels 
 * the amount of state stored in cookies can quickly explode.
 */
@Singleton
public final class SafeStateProvider extends Provider {
	Map<String, String> stateMap = new HashMap<String, String>();

	public SafeStateProvider() {
		if(Storage.isLocalStorageSupported()) {
			stateMap = new StorageMap(Storage.getLocalStorageIfSupported());
		} else {
			stateMap = new HashMap<String, String>();
		}
	}
	
	@Override	
	protected void setValue(String name, String value) {
		stateMap.put(name, value);
	}

	@Override
	protected String getValue(String name) {
		return stateMap.get(name);
	}

	@Override
	protected void clearKey(String name) {
		stateMap.remove(name);
	}
}