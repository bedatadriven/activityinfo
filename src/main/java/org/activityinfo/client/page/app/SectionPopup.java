package org.activityinfo.client.page.app;

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
