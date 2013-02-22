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
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

public class SectionTabStrip extends Widget implements HasSelectionHandlers<Section> {

	private static SectionTabStripUiBinder uiBinder = GWT.create(SectionTabStripUiBinder.class);

	interface SectionTabStripUiBinder extends UiBinder<Element, SectionTabStrip> {
	}

	interface MyStyle extends CssResource {
		String section();
		String activeSection();
		String hoverSection();
	}

	@UiField
	DivElement sectionDiv;
	
	@UiField
	MyStyle style;
	
//  Not ready yet
//	SectionPopup popup;

	DivElement hoverElement;
		
	public SectionTabStrip() {
		setElement(uiBinder.createAndBindUi(this));
		sinkEvents(Event.MOUSEEVENTS);
		sinkEvents(Event.ONCLICK);
		
		//popup = new SectionPopup();

	}

	public void onBrowserEvent(Event event) {
		El element = El.fly(Element.as(event.getEventTarget()));
		
		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEOVER:
			if(element.hasStyleName(style.section())) {
				unhighlight();
				highlight(element.dom);
			}	
			break;
		case Event.ONMOUSEOUT:
			if(hoverElement!= null && element.dom.isOrHasChild(hoverElement)) {
				unhighlight();
			}
			//popup.delayedHide();
			break;
		case Event.ONCLICK:
			if(element.hasStyleName(style.section())) {
				int index = element.getParent().getChildIndex(element.dom);
				SelectionEvent.fire(this, Section.values()[index]);
			}
			break;
		}
	}

	private void unhighlight() {
		if(hoverElement != null) {
			hoverElement.removeClassName(style.hoverSection());
		}
	}

	private void highlight(Element element) {
		hoverElement = element.cast();
		hoverElement.addClassName(style.hoverSection());
		
//		popup.setPopupPosition(element.getAbsoluteLeft(), element.getAbsoluteBottom());
//		popup.delayedShow();SectionPopup popup;


	}
	
	public void setSelection(Section section) {
		NodeList<com.google.gwt.user.client.Element> tabs = El.fly(sectionDiv).select("." + style.section());
		for(int i=0;i!=tabs.getLength();++i) {
			Element tab = tabs.getItem(i).cast();
			if(section != null && i == section.ordinal()) {
				tab.addClassName(style.activeSection());
			} else {
				tab.removeClassName(style.activeSection());
			}
		}
	}
	
	@Override
	public HandlerRegistration addSelectionHandler(
			SelectionHandler<Section> handler) {
		return addHandler(handler, SelectionEvent.getType());
	}
}
