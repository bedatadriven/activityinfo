/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.client.offline.sync;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.client.dispatch.remote.Authentication;

public class CacheUserDetails implements Step {

    private Authentication authentication;

    public CacheUserDetails(Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Caching user information";
    }

    @Override
    public void execute(AsyncCallback<Void> callback) {
        
    }
}
