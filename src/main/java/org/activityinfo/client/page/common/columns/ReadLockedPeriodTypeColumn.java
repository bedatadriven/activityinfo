package org.activityinfo.client.page.common.columns;

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

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.LockedPeriodDTO;
import org.activityinfo.shared.dto.ProjectDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;

/*
 * A column displaying an icon of the parent type of a LockedPeriod;
 * this can be a database, activity or a project
 */
public class ReadLockedPeriodTypeColumn extends ColumnConfig {

    public ReadLockedPeriodTypeColumn() {
        super();

        GridCellRenderer<LockedPeriodDTO> iconRenderer = new GridCellRenderer<LockedPeriodDTO>() {
            @Override
            public Object render(LockedPeriodDTO model, String property,
                ColumnData config, int rowIndex, int colIndex,
                ListStore<LockedPeriodDTO> store, Grid<LockedPeriodDTO> grid) {

                if (model.getParent() instanceof ActivityDTO) {
                    return IconImageBundle.ICONS.activity().getHTML();
                }

                if (model.getParent() instanceof UserDatabaseDTO) {
                    return IconImageBundle.ICONS.database().getHTML();
                }

                if (model.getParent() instanceof ProjectDTO) {
                    return IconImageBundle.ICONS.project().getHTML();
                }

                return null;
            }
        };
        setHeader(I18N.CONSTANTS.type());
        setWidth(48);
        setRowHeader(true);
        setRenderer(iconRenderer);
    }
}
