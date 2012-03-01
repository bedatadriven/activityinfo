package org.sigmah.client.page.report;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.report.template.ChartTemplate;
import org.sigmah.client.page.report.template.CompositeTemplate;
import org.sigmah.client.page.report.template.PivotTableTemplate;
import org.sigmah.client.page.report.template.ReportTemplate;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class NewReportPanel extends ContentPanel {

	private ListStore<ReportTemplate> store;
	private EventBus eventBus;

	public NewReportPanel(EventBus eventBus, Dispatcher dispatcher) {
		this.eventBus = eventBus;

		setHeading("Create new report");
		setLayout(new FitLayout());
		
		store = new ListStore<ReportTemplate>();
		store.add(new ChartTemplate(dispatcher));
		store.add(new PivotTableTemplate(dispatcher));
		store.add(new CompositeTemplate(dispatcher));
		
//		store.add(createReportModel("Map", "Create tables crossing any variables", "map.png"));
//		store.add(createReportModel("Chart", "Create pivot charts", "time.png"));
//		store.add(createReportModel("Custom Report", "Create a report definition that includes a combination of pivot tables, maps, and charts.", "time.png"));
				
		ListView<ReportTemplate> view = new ListView<ReportTemplate>();
        view.setStyleName("gallery");
		view.setTemplate(getTemplate(GWT.getModuleBaseURL() + "image/"));
		view.setBorders(false);
		view.setStore(store);
		view.setItemSelector("dd");
		view.setOverStyle("over");
		view.setSelectStyle("over");

		view.addListener(Events.Select,
				new Listener<ListViewEvent<ReportTemplate>>() {

			@Override
			public void handleEvent(ListViewEvent<ReportTemplate> event) {
				createNew(event.getModel());
			}
		});
		add(view);
	}
	 
	private void createNew(ReportTemplate model) {
		this.mask();
		model.create(new AsyncCallback<Integer>() {
			
			@Override
			public void onSuccess(Integer reportId) {
				eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, 
						new ReportDesignPageState(reportId)));
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}

	private ModelData createReportModel(String name, String desc, String image) {
		ModelData model = new BaseModelData();
		model.set("name", name);
		model.set("desc", desc);
		model.set("path", image);
		return model;
	}
	
	private native String getTemplate(String base) /*-{
      return ['<dl><tpl for=".">',
      '<dd>',
      '<img src="' + base + 'reports/{path}" title="{name}">',
      '<div>',
      '<h4>{name}</h4><p>{description}</p></div>',
      '</tpl>',
      '<div style="clear:left;"></div></dl>'].join("");

      }-*/;

}
