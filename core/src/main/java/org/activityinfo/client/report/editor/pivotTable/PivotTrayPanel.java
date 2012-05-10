package org.activityinfo.client.report.editor.pivotTable;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.report.HasReportElement;
import org.activityinfo.client.report.editor.pivotTable.DimensionSelectionListView.Axis;
import org.activityinfo.shared.report.model.PivotTableReportElement;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;

/**
 * User interface for selecting row / column dimensions
 *
 */
public class PivotTrayPanel extends ContentPanel implements HasReportElement<PivotTableReportElement> {

	private DimensionTree tree;
	private DimensionSelectionListView rowList;
	private DimensionSelectionListView colList;
	
	private PivotTableReportElement model;
	
	public PivotTrayPanel(EventBus eventBus, Dispatcher dispatcher) {
	
		this.tree = new DimensionTree(eventBus, dispatcher);
		this.rowList = new DimensionSelectionListView(eventBus, dispatcher, Axis.ROW);
		this.colList = new DimensionSelectionListView(eventBus, dispatcher, Axis.COLUMN);
		
		setHeading(I18N.CONSTANTS.dimensions());
		setScrollMode(Style.Scroll.NONE);
		setIcon(null);
		
		VBoxLayout layout = new VBoxLayout();
		layout.setPadding(new Padding(5));
		layout.setVBoxLayoutAlign(VBoxLayout.VBoxLayoutAlign.STRETCH);
		setLayout(layout);

		VBoxLayoutData labelLayout = new VBoxLayoutData();
		VBoxLayoutData listLayout = new VBoxLayoutData();
		listLayout.setFlex(1.0);

		tree = new DimensionTree(eventBus, dispatcher);
		
		add(tree.asComponent(), listLayout);
		
		add(new Text(I18N.CONSTANTS.rows()), labelLayout);
		add(rowList.asComponent(), listLayout);

		add(new Text(I18N.CONSTANTS.columns()), labelLayout);
		add(colList.asComponent(), listLayout);	
	}

	@Override
	public void bind(PivotTableReportElement model) {
		this.model = model;
		tree.bind(model);
		rowList.bind(model);
		colList.bind(model);
	}
	
	@Override
	public PivotTableReportElement getModel() {
		return model;
	}

	@Override
	public void disconnect() {
		tree.disconnect();
		rowList.disconnect();
		colList.disconnect();
	}

}
