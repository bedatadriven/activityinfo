/**
 * The set of pages comprising the configuration rubrique, including definition and creation
 * of databases, user settings, etc.
 */
package org.activityinfo.client.page.config;

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

import org.activityinfo.client.page.config.LockedPeriodsPresenter.LockedPeriodListEditor;
import org.activityinfo.client.page.config.design.DesignPresenter;
import org.activityinfo.client.page.config.design.DesignView;

import com.google.gwt.inject.client.AbstractGinModule;

/**
 * @author Alex Bertram
 */
public class ConfigModule extends AbstractGinModule {

    @Override
    protected void configure() {

        // binds the view components
        bind(DbListPresenter.View.class).to(DbListPage.class);
        bind(DbPartnerEditor.View.class).to(DbPartnerGrid.class);
        bind(DbProjectEditor.View.class).to(DbProjectGrid.class);
        bind(DesignPresenter.View.class).to(DesignView.class);
        bind(LockedPeriodListEditor.class).to(LockedPeriodGrid.class);
        bind(DbTargetEditor.View.class).to(DbTargetGrid.class);
    }
}