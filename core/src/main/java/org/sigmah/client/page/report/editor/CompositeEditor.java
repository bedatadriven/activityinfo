package org.sigmah.client.page.report.editor;

import java.util.Arrays;
import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.report.ReportElementModel;
import org.sigmah.shared.command.RenderElement.Format;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class CompositeEditor extends LayoutContainer implements ReportElementEditor<Report> {
	
	private final Dispatcher dispatcher;
	private EditorProvider editorProvider;
	
	
	
	private ElementListPanel elementListPanel;
	
	private Component center;
	private Report model;

	@Inject
	public CompositeEditor(EventBus eventBus, Dispatcher dispatcher, EditorProvider editorProvider) {
		this.dispatcher = dispatcher;
		this.editorProvider = editorProvider;

		setLayout(new BorderLayout());

		BorderLayoutData west = new BorderLayoutData(Style.LayoutRegion.WEST, 0.30f);
		west.setMinSize(250);
		west.setSize(250);
		west.setCollapsible(true);
		west.setSplit(true);
		west.setMargins(new Margins(0, 5, 0, 0));

		elementListPanel = new ElementListPanel(eventBus, dispatcher);
		add(elementListPanel, west);
		
		elementListPanel.addSelectionChangeListener(new SelectionChangedListener<ReportElementModel>() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent<ReportElementModel> se) {
				installEditor(se.getSelectedItem().getReportElement());
			}
		});
		
		elementListPanel.addPreviewButtonSelectListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				installPreview();
			}
		});

	}

	protected void installEditor(ReportElement reportElement) {
		ReportElementEditor editor = editorProvider.create(reportElement);
		editor.bind(reportElement);
		installCenter( editor.getWidget() );
	}

	private void installPreview() {
		PreviewPanel preview = new PreviewPanel(dispatcher);
		preview.loadPreview(model.getId());
		
		installCenter(preview);
	}
	
	private void installCenter(Component component) {
		if(center != null) {
			remove(center);
		}
		
		center = component;
		add(center, new BorderLayoutData(LayoutRegion.CENTER));
		layout();
	}
	
	@Override 
	public void bind(Report model) {
		this.model = model;
		elementListPanel.bind(model);
	}

	@Override
	public Report getModel() {
		return model;
	}

	private void createEditor(ReportElement e) {
		//		Widget w = (Widget) elementEditor.createEditor(e);
		//		addToCenterPanel(w, I18N.CONSTANTS.reportElementEditor());
		//	}
	}

	public void addChart() {
		//	addNewElement(I18N.CONSTANTS.newChart(), (Widget) elementEditor.createChart());	
	}

	public void addTable() {
		//	addNewElement(I18N.CONSTANTS.newTable(), (Widget) elementEditor.createTable());
	}

	public void addMap() {
		//	addNewElement(I18N.CONSTANTS.newMap(), (Widget) elementEditor.createMap());		
	}

	public void onUIAction(String actionId) {
		if (UIActions.ADDCHART.equals(actionId)) {
			addChart();
		} else if (UIActions.ADDMAP.equals(actionId)) {
			addMap();
		} else if (UIActions.ADDTABLE.equals(actionId)) {
			addTable();
		}
	}

	//	private ReportElementModel addReportElement(ReportElement element, boolean edited){
	//		ReportElementModel model = new ReportElementModel();
	//		model.setEdited(edited);
	//		model.setReportElement(element);
	//		model.setId(getNextId());
	//
	//		if(element.getTitle() == null || element.getTitle().equals("")){
	//
	//			if (element instanceof PivotChartReportElement) {
	//				model.setElementTitle(I18N.CONSTANTS.chart() + " " + 1 );
	//			} else if (element instanceof PivotTableReportElement) {
	//				model.setElementTitle(I18N.CONSTANTS.table() + " " + 1 );
	//			} else if (element instanceof MapReportElement) {
	//				model.setElementTitle(I18N.CONSTANTS.map()  + " " + 1);
	//			} 
	//		} else {
	//			model.setElementTitle(element.getTitle());
	//		}
	//
	//		return model;
	//	}


	private void addReportChangeListener(){
		//		eventBus.addListener(AppEvents.REPORT_CHANGED, new Listener<BaseEvent>() {
		//			@Override
		//			public void handleEvent(BaseEvent be) {
		//				updateCurrentElementInStore();
		//			}
		//		});
	}

	@Override
	public Component getWidget() {
		return this;
	}

	@Override
	public List<Format> getExportFormats() {
		return Arrays.asList(Format.Word, Format.PDF);
	} 



}
