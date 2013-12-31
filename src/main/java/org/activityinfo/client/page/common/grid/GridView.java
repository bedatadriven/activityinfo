package org.activityinfo.client.page.common.grid;

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
import org.activityinfo.client.page.common.grid.GridPresenter.SiteGridPresenter;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.data.ModelData;

public interface GridView<P extends GridPresenter, M extends ModelData> {
    public void setActionEnabled(String actionId, boolean enabled);

    public void confirmDeleteSelected(ConfirmCallback callback);

    public M getSelection();

    public AsyncMonitor getDeletingMonitor();

    public AsyncMonitor getSavingMonitor();

    public void refresh();
    
    public interface SiteGridView extends GridView<SiteGridPresenter, SiteDTO> {

    }
}
