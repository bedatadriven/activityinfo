package org.activityinfo.client.page.entry.column;

import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.entry.LockedPeriodSet;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;

public class LockedColumnRenderer implements
		GridCellRenderer<ModelData> {
	private final LockedPeriodSet lockSet;

	public LockedColumnRenderer(LockedPeriodSet lockSet) {
		super();
		this.lockSet = lockSet;
	}

	@Override
	public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore listStore, Grid grid) {
		if (model instanceof SiteDTO) {
			SiteDTO siteModel = (SiteDTO) model;
	    	StringBuilder builder = new StringBuilder();
	    	if (siteModel.isLinked()) {
	    		return IconImageBundle.ICONS.link().getHTML();
	    	} else if (lockSet.isLocked(siteModel)) {
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