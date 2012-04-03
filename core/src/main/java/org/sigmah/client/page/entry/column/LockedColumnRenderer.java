package org.sigmah.client.page.entry.column;

import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;

public class LockedColumnRenderer implements
		GridCellRenderer<ModelData> {
	private final ActivityDTO activity;

	public LockedColumnRenderer(ActivityDTO activity) {
		this.activity = activity;
	}

	@Override
	public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore listStore, Grid grid) {
		if (model instanceof SiteDTO) {
			SiteDTO siteModel = (SiteDTO) model;
	    	StringBuilder builder = new StringBuilder();
	    	if (siteModel.isLinked()) {
	    		return IconImageBundle.ICONS.link().getHTML();
	    	} else if (siteModel.fallsWithinLockedPeriod(activity)) {
	    		//String tooltip = buildTooltip(model, activity);
	    		
	    		//builder.append("<span qtip='");
	    		//builder.append(tooltip);
	    		//builder.append("'>");
	    		builder.append(IconImageBundle.ICONS.lockedPeriod().getHTML());
	    		//builder.append("</span>");
	    		return builder.toString();
	    	} 
	    } 
	    return "";
	}
}