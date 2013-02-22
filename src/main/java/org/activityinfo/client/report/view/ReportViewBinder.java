package org.activityinfo.client.report.view;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.page.report.HasReportElement;
import org.activityinfo.client.page.report.ReportChangeHandler;
import org.activityinfo.client.page.report.ReportEventHelper;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.report.content.Content;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Keeps a report view in sync with a ReportElement being edited. 
 */
public class ReportViewBinder<C extends Content, R extends ReportElement<C>> implements HasReportElement<R> {
	
	private static final int UPDATE_DELAY = 100;
	
	private final ReportEventHelper events;
	private final Dispatcher dispatcher;
	private final ReportView<R> view;
	
	private Timer updateTimer;
	
	private R elementModel;
	
	private GenerateElement<C> lastRequest;
	
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
				updateTimer.schedule(UPDATE_DELAY);
			}
		});
		
		updateTimer = new Timer() {
			
			@Override
			public void run() {
				load();
			}
		};
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
			GenerateElement<C> request = new GenerateElement<C>(elementModel);
			
			dispatcher.execute(request, 
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


	@Override
	public void disconnect() {
		events.disconnect();
	}
}
