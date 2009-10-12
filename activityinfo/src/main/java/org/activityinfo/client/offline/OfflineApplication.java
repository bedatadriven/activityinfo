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

package org.activityinfo.client.offline;

import org.activityinfo.client.Application;
import org.activityinfo.client.inject.AppInjector;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.state.StateManager;
/*
 * @author Alex Bertram
 */

public class OfflineApplication extends Application {

    @Override
    protected void createCaches(AppInjector injector) {

        StateManager.get().setProvider(injector.getDatabaseStateProvider());

        injector.createOfflineManager();        
        injector.createOfflineSchemaCache();
        injector.createAdminCache();

    }
}
