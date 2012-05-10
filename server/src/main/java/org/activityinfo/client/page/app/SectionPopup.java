package org.activityinfo.client.page.app;

import com.extjs.gxt.ui.client.core.El;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class SectionPopup extends PopupPanel {

	private static SectionPopupUiBinder uiBinder = GWT
			.create(SectionPopupUiBinder.class);

	interface SectionPopupUiBinder extends UiBinder<Widget, SectionPopup> {
	}



	private Timer popupTimer;
	
	

	public SectionPopup() {
		setWidget(uiBinder.createAndBindUi(this));
	
		popupTimer = new Timer() {
			
			@Override
			public void run() {
				if(isVisible()) {
					hide();
				} else {
					show();
				}
			}
		};
		sinkEvents(Event.MOUSEEVENTS);
	}
	
	public void delayedHide() {
		clearExistingTimer();
		popupTimer = new Timer() {
			
			@Override
			public void run() {
				hide();
			}
		};
		popupTimer.schedule(250);
	}
	
	public void delayedShow() {
		clearExistingTimer();
		popupTimer = new Timer() {
			
			@Override
			public void run() {
				show();
			}
		};
		popupTimer.schedule(250);
	}
	
	private void clearExistingTimer() {
		if(popupTimer != null) {
			popupTimer.cancel();
		}
	}
	
	public void onBrowserEvent(Event event) {
		El element = El.fly(Element.as(event.getEventTarget()));
		
		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEOVER:
			clearExistingTimer();
			break;
		case Event.ONMOUSEOUT:
			delayedHide();
			break;
		}
	}

}
