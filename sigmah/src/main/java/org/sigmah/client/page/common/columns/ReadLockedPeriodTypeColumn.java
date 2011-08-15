package org.sigmah.client.page.common.columns;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.dto.LockedPeriodDTO;

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
				
				if (model.getActivity() != null) {
					return IconImageBundle.ICONS.activity().getHTML();
				}
				
				if (model.getUserDatabase() != null) {
					return IconImageBundle.ICONS.database().getHTML();
				}
				
				if (model.getProject() != null) {
					return IconImageBundle.ICONS.project().getHTML();
				}
				
				return null;
			}
		}; 
		setHeader(I18N.CONSTANTS.type());  
	    setWidth(48);
	    setRowHeader(true);
	    setRenderer(iconRenderer);	}
}
