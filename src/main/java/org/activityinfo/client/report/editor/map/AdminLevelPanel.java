package org.activityinfo.client.report.editor.map;

import java.util.Collection;
import java.util.List;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.command.GetAdminLevels;
import org.activityinfo.shared.command.result.AdminLevelResult;
import org.activityinfo.shared.dto.AdminLevelDTO;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.google.common.collect.Sets;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AdminLevelPanel extends LayoutContainer {
	
	private final Dispatcher dispatcher;
	private RadioGroup radioGroup = new RadioGroup();
	
	public AdminLevelPanel(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}
	
	public void setIndicators(Collection<Integer> indicatorIds) {
		
		showLoading();
		
		GetAdminLevels query = new GetAdminLevels();
		query.setIndicatorIds(Sets.newHashSet(indicatorIds));
		
		dispatcher.execute(query, new AsyncCallback<AdminLevelResult>() {

			@Override
			public void onFailure(Throwable caught) {
				showLoadingFailed();
			}


			@Override
			public void onSuccess(AdminLevelResult result) {
				showOptions(result.getData());
			}
		});
	}

	protected void showOptions(List<AdminLevelDTO> levels) {
		removeAll();
		if(radioGroup != null) {
			radioGroup.removeAllListeners();
		}
		radioGroup = new RadioGroup();
		boolean missingPolygons = false;
		for(AdminLevelDTO level : levels) {
			Radio radio = new Radio();
			radio.setBoxLabel(level.getName());
			radio.setEnabled(level.getPolygons());
			radio.setData("adminLevelId", level.getId());
			radioGroup.add(radio);
			add(radio);
			
			if(!level.getPolygons()) {
				missingPolygons = true;
			}
		}
		
		if(missingPolygons) {
			addMissingPolygonMessage();
		}
		
		radioGroup.addListener(Events.Change, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				AdminLevelPanel.this.fireEvent(Events.Change, new BaseEvent(Events.Change));
			}
		});
		layout();
	}

	private void addMissingPolygonMessage() {
		Text text = new Text(I18N.CONSTANTS.noPolygonsWarning());
		Margins margin = new Margins(15, 0, 15, 0);
		add(text, new FlowData(margin));
	}

	private void showLoading() {
		removeAll();
		add(new Text(I18N.CONSTANTS.loading()));
		layout();
	}


	private void showLoadingFailed() {
		removeAll();
		add(new Text(I18N.CONSTANTS.connectionProblem()));	
		layout();
	}

	public Integer getSelectedLevelId() {
		if(radioGroup.getValue() == null) {
			return null;
		}
		return (Integer)radioGroup.getValue().getData("adminLevelId");
	}
	
}
