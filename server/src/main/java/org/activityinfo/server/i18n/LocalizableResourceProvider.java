package org.activityinfo.server.i18n;

import com.google.gwt.i18n.client.LocalizableResource;
import com.google.inject.Provider;
import com.teklabs.gwt.i18n.client.LocaleFactory;
import com.teklabs.gwt.i18n.server.LocaleProxy;

public class LocalizableResourceProvider<T extends LocalizableResource> implements Provider<T> {

	private Class<T> clazz;
	
	public LocalizableResourceProvider(Class<T> clazz) {
		super();
		this.clazz = clazz;
		LocaleProxy.initialize();
	}

	@Override
	public T get() {
		return LocaleFactory.get(clazz);
	}
}
