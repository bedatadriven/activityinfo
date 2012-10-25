package org.activityinfo.client.report.editor.map;

import java.util.Collection;

import org.activityinfo.client.dispatch.Dispatcher;

import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class AdminLevelPage extends WizardPage {

	private Dispatcher dispatcher;
	private AdminLevelPanel checkPanel;
	
	public AdminLevelPage(Dispatcher dispatcher) {
		super();
		this.dispatcher = dispatcher;
		this.checkPanel = new AdminLevelPanel(dispatcher);
		add(checkPanel);
		
		setLayout(new FitLayout());
	}


	public void setIndicators(Collection<Integer> list) {
		this.checkPanel.setIndicators(list);
	}


	public int getSelectedLevelId() {
		return checkPanel.getSelectedLevelId();
	}
	
}
