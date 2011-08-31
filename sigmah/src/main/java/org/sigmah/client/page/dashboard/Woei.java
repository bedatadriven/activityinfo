/**
 * 
 */
package org.sigmah.client.page.dashboard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ruud
 *
 */
public class Woei extends Composite implements HasText {

	private static WoeiUiBinder uiBinder = GWT.create(WoeiUiBinder.class);
	@UiField Label title;
	@UiField Label date;
	@UiField Label newsContent;
	@UiField Hyperlink readMoreLink;

	interface WoeiUiBinder extends UiBinder<Widget, Woei> {
	}

	/**
	 * Because this class has a default constructor, it can
	 * be used as a binder template. In other words, it can be used in other
	 * *.ui.xml files as follows:
	 * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 *   xmlns:g="urn:import:**user's package**">
	 *  <g:**UserClassName**>Hello!</g:**UserClassName>
	 * </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
	public Woei() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public Woei(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

		// Can access @UiField after calling createAndBindUi
	}

	public void setText(String text) {
	}

	/**
	 * Gets invoked when the default constructor is called
	 * and a string is provided in the ui.xml file.
	 */
	public String getText() {
		return "";
	}

}
