package org.sigmah.client.page.entry.column;

import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;

public class LocationColumnRenderer implements GridCellRenderer<ModelData> {

	@Override
	public Object render(ModelData model, String property, ColumnData config,
			int rowIndex, int colIndex, ListStore<ModelData> store,
			Grid<ModelData> grid) {

		if(model instanceof SiteDTO) {
			return render((SiteDTO)model);
		} else {
			return "";
		}
	}
	
	public Object render(SiteDTO model) {
		
		String name = model.getLocationName();
		String axe = model.getLocationAxe();
		
		if(axe == null) {
			return name;
		} else {
			return ColumnTemplates.INSTANCE.locationCell(model.getLocationName(), model.getLocationAxe()).asString();
		}
	}

}
