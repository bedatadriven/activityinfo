package org.sigmah.client.page.report;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.toolbar.UIActions;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.inject.Inject;

public class ReportDesignPage extends ContentPanel implements ReportDesignPresenter.View {

	private ToolBar toolBar;
	private ReportDesignPresenter presenter;
	private ContentPanel reportListPane;
	private ContentPanel center;
	
	@Inject
	public ReportDesignPage(){
		
		initializeComponent();
		createToolbar();
		reportListPane();
		reportPreviewPane();
	}

	@Override
	public void init() {

	}
	
	private void initializeComponent() {
		setHeaderVisible(false);
		setLayout(new BorderLayout());
	}
	
	public void createToolbar(){
		
		toolBar = new ToolBar();
		
		TextField<String> titleField = new TextField<String>();
		
		toolBar.add(titleField);
		
		SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (presenter != null && ce.getButton().getItemId() != null) {
					presenter.onUIAction(ce.getButton().getItemId());
				}
			}
		};
		
		Button addChart = new Button(I18N.CONSTANTS.addChart(),
				null, listener);
		addChart.setItemId(UIActions.ADDCHART);
		toolBar.add(addChart);

		Button addMap = new Button(I18N.CONSTANTS.addMap(),
				null, listener);
		addMap.setItemId(UIActions.ADDCHART);
		toolBar.add(addMap);

		Button addTable = new Button(I18N.CONSTANTS.addTable(),
				null, listener);
		addTable.setItemId(UIActions.ADDCHART);
		toolBar.add(addTable);
		
		setTopComponent(toolBar);
	}
	
	public void reportListPane(){
		reportListPane = new ContentPanel();
		
		BorderLayoutData west = new BorderLayoutData(Style.LayoutRegion.WEST);
		west.setMinSize(250);
		west.setSize(250);
		west.setCollapsible(true);
		west.setSplit(true);
		west.setMargins(new Margins(0, 0, 0, 0));
		
		add(reportListPane, west);
		
	}
	
	public void reportPreviewPane(){
		
		center = new ContentPanel();
		center.setHeaderVisible(false);
		center.setLayout(new BorderLayout());
		add(center, new BorderLayoutData(Style.LayoutRegion.CENTER));
		
	}

}
