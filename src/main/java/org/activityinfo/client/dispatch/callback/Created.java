

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

import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.shared.command.result.CreateResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Convenience callback for commands that return CreateResult
 *
 * @author Alex Bertram
 */
public abstract class Created implements AsyncCallback<CreateResult> {

    /**
     * By default, this method does nothing on failure
     * <p/>
     * The provided {@link AsyncMonitor} should take care of any
     * UI stuff that needs to happen; you probably only need to
     * provide a substantive implementation here if something
     * needs to be rolled back.
     */
    @Override
    public void onFailure(Throwable caught) {

    }

    @Override
    public void onSuccess(CreateResult result) {
        created(result.getNewId());
    }

    public abstract void created(int newId);

}
