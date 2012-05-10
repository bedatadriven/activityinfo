package org.activityinfo.client.page.app;

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
