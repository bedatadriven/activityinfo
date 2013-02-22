package org.activityinfo.server.i18n;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Locale;

import org.activityinfo.client.i18n.UIMessages;
import org.activityinfo.client.i18n.UIConstants;

import com.google.inject.AbstractModule;
import com.teklabs.gwt.i18n.server.LocaleProxy;

public class LocaleModule extends AbstractModule {

	public LocaleModule() {
		LocaleProxy.initialize();
	}
	
	@Override
	protected void configure() {
		bind(UIConstants.class).toProvider(new LocalizableResourceProvider<UIConstants>(UIConstants.class));
		bind(UIMessages.class).toProvider(new LocalizableResourceProvider<UIMessages>(UIMessages.class));
		bind(Locale.class).toProvider(LocaleProvider.class);
	}
	
}
