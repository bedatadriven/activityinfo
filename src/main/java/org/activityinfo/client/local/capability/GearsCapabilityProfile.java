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
import com.google.gwt.core.client.GWT;
import com.google.gwt.gears.client.Factory;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Utilities for Gears-based offline capability profiles.
 *
 */
public abstract class GearsCapabilityProfile extends LocalCapabilityProfile {
	
	
	protected final boolean isGearsInstalled() {
		return Factory.getInstance() != null;
	}
	
	@Override
	public final boolean isOfflineModeSupported() {
		return isGearsInstalled();
	}


	@Override
	public final void acquirePermission(AsyncCallback<Void> callback) {
		Log.trace("GearsCapabilityProfile: acquiring permissions...");
		try {
			if(!isGearsInstalled()) {
				callback.onFailure(new UnsupportedOperationException("Gears is not installed"));
			} else if(acquirePermissions()) {
				callback.onSuccess(null);
			} else {
				callback.onFailure(new PermissionRefusedException());
			}
		} catch(Exception e) {
			callback.onFailure(e);
		}
	}


	@Override
	public final boolean hasPermission() {
		if(!isGearsInstalled()) {
			throw new UnsupportedOperationException("Gears is not installed");
		}
		return Factory.getInstance().hasPermission();
	}


	private boolean acquirePermissions() {
		return Factory.getInstance().getPermission("ActivityInfo", 
				GWT.getModuleBaseURL() + "desktopicons/64x64.png");
	}
}
