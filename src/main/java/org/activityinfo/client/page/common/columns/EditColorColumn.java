package org.activityinfo.client.page.common.columns;

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
			public Object render(NamedSlice model, String property, ColumnData config,
					int rowIndex, int colIndex, ListStore<NamedSlice> store,
					Grid<NamedSlice> grid) {
				String color = model.get("color");
				return "<span style='background:#" + color + "'>" + color + "</span>";
			}
		};
	    
		setRenderer(colorRenderer);
	    setEditor(new CellEditor(colorField));
	}

}
