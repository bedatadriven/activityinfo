package org.activityinfo.client.local.capability;

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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.TextResource;

public interface ProfileResources extends ClientBundle {

	public static ProfileResources INSTANCE = GWT.create(ProfileResources.class);
	
	
	@Source("StartupMessageIE.html")
	TextResource startupMessageIE();

	@Source("StartupMessageIE9.html")
	TextResource startupMessageIE9();
	
	@Source("StartupMessageFirefox.html")
	TextResource startupMessageFirefox();
	
	@Source("Startup.css")
	StartupStyle style();
	
	interface StartupStyle extends CssResource {
		String startupDialogBody();
	}
	
}
