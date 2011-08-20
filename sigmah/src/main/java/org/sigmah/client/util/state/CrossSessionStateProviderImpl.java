package org.sigmah.client.util.state;

import java.util.Date;
import java.util.Map;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.state.CookieProvider;
import com.extjs.gxt.ui.client.state.Provider;
import com.google.gwt.storage.client.Storage;

public class CrossSessionStateProviderImpl implements CrossSessionStateProvider {

	private Provider provider;
	
	public CrossSessionStateProviderImpl() {
		if(Storage.isLocalStorageSupported()) {
			provider = new SafeStateProvider();
		} else {
			provider = new CookieProvider("/", null, null, GXT.isSecure);
		}
	}
	
	@Override
	public Object get(String name) {
		return provider.get(name);
	}

	@Override
	public Date getDate(String name) {
		return provider.getDate(name);
	}

	@Override
	public Integer getInteger(String name) {
		return provider.getInteger(name);
	}

	@Override
	public Map<String, Object> getMap(String name) {
		return provider.getMap(name);
	}

	@Override
	public String getString(String name) {
		return provider.getString(name);
	}

	@Override
	public void set(String name, Object value) {
		provider.set(name, value);
	}

}
