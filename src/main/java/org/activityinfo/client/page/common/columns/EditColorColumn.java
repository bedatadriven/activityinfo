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
import org.activityinfo.client.report.editor.map.layerOptions.PiechartLayerOptions;
import org.activityinfo.client.report.editor.map.layerOptions.PiechartLayerOptions.NamedSlice;
import org.activityinfo.client.widget.ColorField;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;

public class EditColorColumn extends ColumnConfig {

    /**
     * 
     * Assumes the model has a property String getColor()
     */
    public EditColorColumn() {
        super("color", I18N.CONSTANTS.color(), 50);
        final ColorField colorField = new ColorField();

        GridCellRenderer<NamedSlice> colorRenderer = new GridCellRenderer<PiechartLayerOptions.NamedSlice>() {
            @Override
            public Object render(NamedSlice model, String property,
                ColumnData config,
                int rowIndex, int colIndex, ListStore<NamedSlice> store,
                Grid<NamedSlice> grid) {
                String color = model.get("color");
                return "<span style='background:#" + color + "'>" + color
                    + "</span>";
            }
        };

        setRenderer(colorRenderer);
        setEditor(new CellEditor(colorField));
    }

}
