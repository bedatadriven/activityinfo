package org.sigmah.server.util;

import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.i18n.UIMessages;

import com.google.inject.AbstractModule;

public class LocaleModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UIConstants.class).toProvider(new LocaleProvider<UIConstants>(UIConstants.class));
		bind(UIMessages.class).toProvider(new LocaleProvider<UIMessages>(UIMessages.class));
	}
	
}
