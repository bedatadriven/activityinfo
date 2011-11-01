package org.sigmah.client.page.dashboard;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;

class AiPortlet extends LayoutContainer {
	protected Dispatcher service;
	protected AsyncMonitor loadingMonitor = new MaskingAsyncMonitor(this, I18N.CONSTANTS.loading());
	private Label labelTitle;

	public AiPortlet(Dispatcher service, String title) {
		super();

		this.service = service;
		
		setBorders(true);
		
		labelTitle = new Label(title);
		labelTitle.setStyleAttribute("margin", "5px");
		labelTitle.setStyleAttribute("font-size", "14px");
		labelTitle.setStyleAttribute("font-weight", "bold");
		labelTitle.setStyleAttribute("color", "#0B0B61");
		add(labelTitle);
	}
	
	public void setTitle(String title) {
		labelTitle.setText(title);
	}
}