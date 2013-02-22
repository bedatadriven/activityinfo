package org.activityinfo.client.local.sync;

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

import java.util.Date;

import org.activityinfo.shared.command.Command;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Decouples the actual offline implementation from the manager so we can stick
 * all the offline code in a separate js split.
 */
public interface Synchronizer {
    void install(AsyncCallback<Void> callback);

    /**
     * @return the date of the last successful synchronization to the client
     */
    void getLastSyncTime(AsyncCallback<Date> callback);

    /**
     * Conducts sanity checks to be sure that we are really prepared to go
     * offline. (It's possible that the Offline "state" flag was mis-set in the
     * past.)
     * 
     * @return true if we are ready
     */
    void validateOfflineInstalled(AsyncCallback<Void> callback);

    void synchronize();

    // TODO: move to separate interface
    void execute(Command command, AsyncCallback callback);

}
