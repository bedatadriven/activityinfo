package org.sigmah.client.page.report.editor;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.report.editor.AbstractEditor.FilterPanelHandler;
import org.sigmah.client.page.table.AbstractPivot;
import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.util.DelayedTask;
import com.google.inject.Inject;

public class PivotEditor extends AbstractPivot implements AbstractEditor {

	private PivotTableReportElement table;
	
	@Inject
	public PivotEditor(EventBus eventBus, Dispatcher service, StateProvider stateMgr) {
		super(eventBus, service, stateMgr);
	}

	
	
	public void bindReportElement(final PivotTableReportElement table) {

		this.table = table;
				
		List<Dimension> colDim = table.getColumnDimensions();
		getColStore().add(colDim);

		List<Dimension> rowDim = table.getRowDimensions();
		getRowStore().add(rowDim);

		FilterPanelHandler filtesUpdater = new FilterPanelHandler(table);
		filtesUpdater.addAdminPanelListener(adminPanel);
		filtesUpdater.addIndicatorPanelListener(indicatorPanel);
		filtesUpdater.addPartnerPanelListener(partnerPanel);
		filtesUpdater.updateDate(datePanel);
		
		onUIAction(UIActions.REFRESH);

	}

	@Override
	public Object getWidget() {
		return this;
	}

	@Override
	public void bindReportElement(ReportElement element) {
		bindReportElement(element);
	}

	@Override
	public ReportElement getReportElement() {
		return createElement();
	}

}
