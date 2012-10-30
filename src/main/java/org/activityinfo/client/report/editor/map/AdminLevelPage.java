package org.activityinfo.client.report.editor.map;

import java.util.Collection;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.widget.wizard.WizardPage;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;

public class AdminLevelPage extends WizardPage {

	private Dispatcher dispatcher;
	private AdminLevelPanel checkPanel;
	
	public AdminLevelPage(Dispatcher dispatcher) {
		super();
		this.dispatcher = dispatcher;
		
		FlowLayout layout = new FlowLayout(15);
		setLayout(layout);
		
		Text header = new Text(I18N.CONSTANTS.chooseAdminLevelToMap());
		header.setTagName("h1");
		add(header, new FlowData(new Margins(0, 0, 15, 0)));
		
		this.checkPanel = new AdminLevelPanel(dispatcher);
		add(checkPanel);
				
		checkPanel.addListener(Events.Change, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				AdminLevelPage.this.fireEvent(Events.Change, new BaseEvent(Events.Change));
			}
		});
	}


	public void setIndicators(Collection<Integer> list) {
		this.checkPanel.setIndicators(list);
	}


	public Integer getSelectedLevelId() {
		return checkPanel.getSelectedLevelId();
	}
}
