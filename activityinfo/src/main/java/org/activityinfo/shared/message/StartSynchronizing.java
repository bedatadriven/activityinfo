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
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.shared.message;

/**
 * @author Alex Bertram
 */
public final class StartSynchronizing extends Message {

    protected StartSynchronizing() {

    }

    public static native StartSynchronizing newInstance() /*-{
        return {authToken:null, userDb:null, endPoint:null };
    }-*/;

    public native void setAuthToken(String authToken) /*-{
        this.authToken = authToken;
    }-*/;

    public native String getAuthToken() /*-{
        return this.authToken;
    }-*/;

    public native void setUserDb(String name) /*-{
        this.userDb = name;
    }-*/;

    public native String getUserDb() /*-{
        return this.userDb;
    }-*/;

    public native void setEndPoint(String url) /*-{
        this.endPoint = url;
    }-*/;

    public native String getEndPoint() /*-{
        return this.url;
    }-*/;



}


