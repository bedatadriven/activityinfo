package org.sigmah.server.util;

import com.google.gwt.i18n.client.LocalizableResource;
import com.google.inject.Provider;
import com.teklabs.gwt.i18n.client.LocaleFactory;
import com.teklabs.gwt.i18n.server.LocaleProxy;

public class LocaleProvider<T extends LocalizableResource> implements Provider<T> {

	private Class<T> clazz;
	
	public LocaleProvider(Class<T> clazz) {
		super();
		this.clazz = clazz;
		LocaleProxy.initialize();
	}

	@Override
	public T get() {
		return LocaleFactory.get(clazz);
	}
}
