package org.activityinfo.client.page.config.link;

import java.util.List;
import java.util.Set;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.google.common.collect.Lists;
import com.google.gwt.dom.client.Element;

class HighlightingGridView extends GridView {

		
	public static final EventType RowMouseOver = new EventType();

	private List<Element> highlighted = Lists.newArrayList();

	
	@Override
	protected void onRowOut(Element row) {
		fly(row).removeStyleName(IndicatorLinkResources.INSTANCE.style().highlight());
	}

	@Override
	protected void onRowOver(Element row) {
		int index = findRowIndex(row);
		if(index != -1) {
			ModelData model = grid.getStore().getAt( index );
			if(isHighlightable(model)) {
				
				fly(row).addStyleName(IndicatorLinkResources.INSTANCE.style().highlight());
				
				GridEvent event = new GridEvent( grid );
				event.setModel( model );
				
				grid.fireEvent(HighlightingGridView.RowMouseOver, event);
			}
		}
		overRow = row;
	}
	

	public void highlight(Set<Integer> ids) {
		clearHighlight();
		if(!ids.isEmpty()) {
			for(int row=0;row!=grid.getStore().getCount();++row) {
				Element element = grid.getView().getRow(row);
				El el = El.fly(element);
				if(ids.contains( grid.getStore().getAt(row).get("id") )) {
					el.addStyleName(IndicatorLinkResources.INSTANCE.style().highlight());
					highlighted.add(element);
				}
			}
		}
	}
	
	public void clearHighlight() {
		for(Element element : highlighted) {
			El.fly(element).removeStyleName(IndicatorLinkResources.INSTANCE.style().highlight());
		}
		highlighted.clear();
	}

	protected boolean isHighlightable(ModelData model) {
		return true;
	}
	
	public void refreshAllRows() {
		for(int i=0;i!=getRows().getLength();++i) {
			refreshRow(i);
		}
	}
}