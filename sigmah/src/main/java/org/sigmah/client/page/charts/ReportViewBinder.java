package org.sigmah.client.page.charts;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.report.HasReportElement;
import org.sigmah.client.page.report.ReportChangeHandler;
import org.sigmah.client.page.report.ReportEventHelper;
import org.sigmah.shared.command.GenerateElement;
import org.sigmah.shared.report.content.Content;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ReportViewBinder<C extends Content, R extends ReportElement<C>> implements HasReportElement<R> {
	
	
	private final ReportEventHelper events;
	private final Dispatcher dispatcher;
	private final ReportView<R> view;
	
	private R elementModel;
	
	public static <C extends Content, R extends ReportElement<C>> ReportViewBinder<C, R> 
			create(EventBus eventBus, Dispatcher dispatcher, ReportView<R> view) {
		return new ReportViewBinder<C,R>(eventBus, dispatcher, view);
	}
	
	
	public ReportViewBinder(EventBus eventBus, Dispatcher dispatcher, ReportView<R> view) {
		this.events = new ReportEventHelper(eventBus, this);
		this.dispatcher = dispatcher;
		this.view = view;
		
		events.listen(new ReportChangeHandler() {
			
			@Override
			public void onChanged() {
				load();
			}
		});
	}

	@Override
	public void bind(R model) {
		this.elementModel = model;
		load();
	}
	
	@Override
	public R getModel() {
		return elementModel;
	}

	public Component getComponent() {
		return (Component)view;
	}
	
	private void load() {
		if(!elementModel.getFilter().getRestrictions(DimensionType.Indicator).isEmpty()) {
			dispatcher.execute(new GenerateElement<C>(elementModel), null,
					new AsyncCallback<C>() {
	
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
	
				}
	
				@Override
				public void onSuccess(C result) {
					elementModel.setContent(result);
					view.show(elementModel);
				}
			});
		}
	}
}
