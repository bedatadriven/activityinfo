package org.activityinfo.embed.client;

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

import org.activityinfo.client.dispatch.remote.IncompatibleRemoteHandler;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;

/**
 * Simple handler for {@link IncompatibleRemoteServiceException} that simply refreshes the 
 * page (or iframe) to obtain the new version of the code. Since we're not using appcache and
 * no data is being changed, we don't really need a UI like we do in the full app.
 */
public class SimpleIncompatibleRemoteHandler implements IncompatibleRemoteHandler {

	@Override
	public void handle() {
		Window.Location.reload();
	}

}
