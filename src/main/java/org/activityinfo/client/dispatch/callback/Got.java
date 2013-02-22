package org.activityinfo.client.dispatch.callback;

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

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Convenience callback class for commands that do not expect error. (Connection
 * failures are handled at higher levels)
 * 
 * @param <T>
 */
public abstract class Got<T> implements AsyncCallback<T> {

    @Override
    public void onFailure(Throwable arg0) {
        /* No op. Errors are handled at higher levels */
    }

    @Override
    public void onSuccess(T result) {
        got(result);
    }

    public abstract void got(T result);

}
