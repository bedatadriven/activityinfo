package org.sigmah.client.page.dashboard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class WoeiBoei extends UIObject {

	private static WoeiBoeiUiBinder uiBinder = GWT
			.create(WoeiBoeiUiBinder.class);

	interface WoeiBoeiUiBinder extends UiBinder<Element, WoeiBoei> {
	}

	@UiField
	SpanElement nameSpan;

	public WoeiBoei(String firstName) {
		setElement(uiBinder.createAndBindUi(this));
		nameSpan.setInnerText(firstName);
	}

}
