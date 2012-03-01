package org.sigmah.client.page.report.editor;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.report.HasReportElement;
import org.sigmah.client.page.report.ReportElementModel;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.google.gwt.core.client.GWT;

public class ElementListPanel extends ContentPanel implements HasReportElement<Report> {

	private Report model;
	
	private ListStore<ReportElementModel> store;
	private ListView<ReportElementModel> listView;
	private Button reportPreview;
	
	public ElementListPanel(EventBus eventBus, Dispatcher dispatcher) {

		setHeading(I18N.CONSTANTS.reportElements());
		setLayout(createVBoxLayout());

		createReportPreviewButton();
		createNewElementButtons();
		createListView();

	}

	private void createNewElementButtons() {
		
		// TODO: this needs to be elsewhere
		
		
		SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (ce.getButton().getItemId() != null) {
					addElement(ce.getButton().getItemId());
				}
			}
		};

		Button addChart = new Button(I18N.CONSTANTS.addChart(), IconImageBundle.ICONS.barChart(), listener);
		addChart.setItemId(UIActions.ADDCHART);
		add(addChart);

		Button addMap = new Button(I18N.CONSTANTS.addMap(),  IconImageBundle.ICONS.map(), listener);
		addMap.setItemId(UIActions.ADDMAP);
		add(addMap);

		Button addTable = new Button(I18N.CONSTANTS.addTable(), IconImageBundle.ICONS.table(), listener);
		addTable.setItemId(UIActions.ADDTABLE);
		add(addTable);
	}
	
	private int getNextIndex(Class newElementClass) {
		int index = 1;
		for(ReportElement element : model.getElements()) {
			if(element.getClass().equals(newElementClass)) {
				index ++;
			}
		}
		return index;
	}
	
	private void addElement(String itemId) {
		ReportElement element;	
		if(UIActions.ADDCHART.equals(itemId)) {
			element = new PivotChartReportElement();
			element.setTitle(I18N.CONSTANTS.chart() + " " + getNextIndex(PivotChartReportElement.class));
		} else if(UIActions.ADDTABLE.equals(itemId)) {
			element = new PivotTableReportElement();
			element.setTitle(I18N.CONSTANTS.table() + " " + getNextIndex(PivotTableReportElement.class));
		} else if(UIActions.ADDMAP.equals(itemId)) {
			element = new MapReportElement();
			element.setTitle(I18N.CONSTANTS.map() + " " + getNextIndex(MapReportElement.class));
		} else {
			throw new IllegalArgumentException();
		}
		model.getElements().add(element);
		store.add(new ReportElementModel(element));
	}

	private VBoxLayout createVBoxLayout(){
		VBoxLayout layout = new VBoxLayout();
		layout.setPadding(new Padding(5));
		layout.setVBoxLayoutAlign(VBoxLayout.VBoxLayoutAlign.STRETCH);

		return layout;
	}
	

	private void createListView() {

		store = new ListStore<ReportElementModel>();

		listView = new ListView<ReportElementModel>();
		listView.setTemplate(getTemplate(GWT.getModuleBaseURL() + "image/"));
		listView.setBorders(false);
		listView.setStore(store);
		listView.setItemSelector("dd");
		listView.setOverStyle("over");

		listView.addListener(Events.Select,
				new Listener<ListViewEvent<ReportElementModel>>() {

			@Override
			public void handleEvent(ListViewEvent<ReportElementModel> event) {
//				if(event.getModel().getId() != getElementInViewId()){
//					loadElementEditor(event.getModel());	
//				}	
			}
		});

		VBoxLayoutData listLayout = new VBoxLayoutData();
		add(listView, listLayout);
	}

	
	@Override
	public void bind(Report model) {
		this.model = model;
		populateStore();
	}
	
	@Override
	public Report getModel() {
		return this.model;
	}
	
	private void populateStore() {
		store.removeAll();
		for(ReportElement element : this.model.getElements()) {
			store.add(new ReportElementModel(element));
		}
	}
	
	private void createReportPreviewButton(){
		reportPreview = new Button(I18N.CONSTANTS.preview());
		reportPreview.setWidth(100);

		VBoxLayoutData buttonLayout = new VBoxLayoutData();
		buttonLayout.setMargins(new Margins(0, 0, 5, 0));

		add(reportPreview, buttonLayout);
	}

	private native String getTemplate(String base) /*-{
		return [ '<dl><tpl for=".">', '<dd>',
				'<img src="' + base + 'report.png" title="{elementTitle}">',
				'<span>{elementTitle}</span>', '</tpl>',
				'<div style="clear:left;"></div></dl>' ].join("");

	}-*/;
	
	public void addSelectionChangeListener(SelectionChangedListener<ReportElementModel> listener) {
		listView.getSelectionModel().addSelectionChangedListener(listener);
	}

	public void addPreviewButtonSelectListener(SelectionListener<ButtonEvent> listener) {
		reportPreview.addSelectionListener(listener);
	}

}
