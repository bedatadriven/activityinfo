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


import org.activityinfo.client.Log;
import org.activityinfo.client.authentication.ClientSideAuthProvider;
import org.activityinfo.client.offline.capability.FFPermissionsDialog;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.bedatadriven.rebar.appcache.client.AppCache;
import com.bedatadriven.rebar.appcache.client.AppCache.Status;
import com.bedatadriven.rebar.appcache.client.AppCacheFactory;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * FireFox offline capability profile.
 * 
 * <p>Though Mozilla has decided not to implement WebSQL, we use
 * a add-on to expose the sqlite database service Mozilla provides
 * for extensions.
 */
public class FFCapabilityProfile extends LocalCapabilityProfile {

	
	private boolean hasPlugin;
	
	public FFCapabilityProfile() {
		Log.debug("FireFox version: " + Window.Navigator.getUserAgent());
		hasPlugin = hasPlugin();
	}

	@Override
	public boolean isOfflineModeSupported() {
		return hasPlugin;
	}


	@Override
	public void acquirePermission(final AsyncCallback<Void> callback) {
		
		final AppCache appCache = AppCacheFactory.get();
		if(appCache.getStatus() != Status.UNCACHED) {
			callback.onSuccess(null);
		} else {
			FFPermissionsDialog dialog = new FFPermissionsDialog(callback);
			dialog.show();
		}
	}

	private String createReceipt() {
		AuthenticatedUser user = new ClientSideAuthProvider().get();
		StringBuilder receipt = new StringBuilder();
		receipt.append(user.getUserId()).append("|")
			.append(user.getAuthToken()).append("|")
			.append(user.getEmail());
		return receipt.toString();
	}

	@Override
	public String getInstallInstructions() {
		return ProfileResources.INSTANCE.startupMessageFirefox().getText();
	}
	
	private static native boolean hasPlugin() /*-{
		return !!$wnd.openDatabase;
	}-*/;
	
	

}
