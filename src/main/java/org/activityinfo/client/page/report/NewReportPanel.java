package org.activityinfo.client.page.report;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.NavigationHandler;
import org.activityinfo.client.page.report.json.ReportSerializer;
import org.activityinfo.client.page.report.template.ChartTemplate;
import org.activityinfo.client.page.report.template.CompositeTemplate;
import org.activityinfo.client.page.report.template.MapTemplate;
import org.activityinfo.client.page.report.template.PivotTableTemplate;
import org.activityinfo.client.page.report.template.ReportTemplate;
import org.activityinfo.shared.command.CreateReport;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.report.model.Report;

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
	private ReportSerializer reportSerializer;
	private Dispatcher dispatcher;

	public NewReportPanel(EventBus eventBus, Dispatcher dispatcher, ReportSerializer reportSerializer) {
		this.eventBus = eventBus;
		this.reportSerializer = reportSerializer;
		this.dispatcher = dispatcher;
		
		setHeading(I18N.CONSTANTS.createNewReport());
		setLayout(new FitLayout());
		
		store = new ListStore<ReportTemplate>();
		store.add(new ChartTemplate(dispatcher));
		store.add(new PivotTableTemplate(dispatcher));
		store.add(new MapTemplate(dispatcher));
		store.add(new CompositeTemplate(dispatcher));
				
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
		model.createReport(new AsyncCallback<Report>() {
			
			@Override
			public void onSuccess(Report report) {
				dispatcher.execute(new CreateReport(report), new AsyncCallback<CreateResult>() {

					@Override
					public void onFailure(Throwable caught) {
						
					}

					@Override
					public void onSuccess(CreateResult created) {
						eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, 
								new ReportDesignPageState(created.getNewId())));
					}
				});
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
