package org.activityinfo.client.page.config.link;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.tips.Tip;
import com.google.gwt.user.client.Event;

public class LinkTip extends Tip {

	public static final int WIDTH = 75;
	public static final int HEIGHT = 45;
	private Html html;
	
	public LinkTip() {
		setWidth(WIDTH);
		setHeight(HEIGHT);
		html = new Html("Link");
		html.addStyleName(IndicatorLinkResources.INSTANCE.style().linkToggle());
		html.sinkEvents(Event.ONCLICK);
		html.addListener(Events.BrowserEvent, new Listener<ComponentEvent>() {

			@Override
			public void handleEvent(ComponentEvent be) {
				if(be.getEventTypeInt() == Event.ONCLICK) {
					fireEvent(Events.Select, be);
				}
			}
			
		});
		add(html);
	}

	public void setLinked(boolean link) {
		html.setHtml(link ? "Unlink" : "Link");
	}
	
	public void addSelectListener(SelectionListener<ComponentEvent> listener) {
		addListener(Events.Select, listener);
	}
	
}


