package org.sigmah.client.page.dashboard;

import java.util.Date;

import org.sigmah.client.dispatch.Dispatcher;

import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.gwt.user.client.ui.DockPanel;

class NewNews extends AiPortlet {
	public NewNews(Dispatcher service) {
		super(service, "News from BeDataDriven");
		
		setAutoWidth(true);

		StringBuilder text = new StringBuilder();
		for (int i = 0; i < 40; i++) {
			text.append("Hey! Cool new Stuff?!?! \n");
		}
		DockPanel dp = new DockPanel();
		dp.add(new LabelField("Title of blogpost"), DockPanel.WEST);
		dp.add(new LabelField(new Date().toString()), DockPanel.EAST);
		//dp.setHeight(Integer.toString(getHeight()) + "px");
		add(dp);
		add(new LabelField(text.toString()));
		Html l = new Html("<a href=" + "http://nu.nl" + ">" + "Read more..." + "</a>");
		l.setStyleAttribute("text-align", "right");
		add(l);
	}
}