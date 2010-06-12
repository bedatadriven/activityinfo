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

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.activityinfo.client.dispatch.remote.Authentication;

import java.util.Date;

public class CacheUserDetails implements Step {

    private final Authentication authentication;

    @Inject
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
        Date now = new Date();
        Date expires = new Date(now.getTime() * 1000 * 60 * 60 * 24 * 365);
        Cookies.setCookie("userId", Integer.toString(authentication.getUserId()), expires);
        Cookies.setCookie("email", authentication.getEmail(), expires);

        callback.onSuccess(null);
    }
}
