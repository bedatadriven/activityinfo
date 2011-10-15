package org.sigmah.client.page.dashboard;

import org.sigmah.client.icon.IconImageBundle;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class WelcomeWidget extends HorizontalPanel {

	public WelcomeWidget() {
		super();
		initializeComponent();
	}
	private void initializeComponent() {
		setSize("auto", "5em");
//		Label labelStartTitle = new Label("Info about your activities.");
//		labelStartTitle.setStyleName("dashboard-header-slogan");
		Label labelBeginName = new Label("Activity");
		Label labelEndName = new Label("Info.");
		labelEndName.setStyleName("dashboard-info-label");
		Image image = IconImageBundle.ICONS.logo48().createImage();
		image.setStyleName("dashboard-header-logo");
		add(image);
//		add(labelStartTitle);
//		setCellVerticalAlignment(labelStartTitle, HasVerticalAlignment.ALIGN_MIDDLE);
//		labelStartTitle.setWidth("auto");
//		setCellWidth(labelStartTitle, "auto");
		add(labelBeginName);
		setCellVerticalAlignment(labelBeginName, HasVerticalAlignment.ALIGN_MIDDLE);
		add(labelEndName);
		setCellVerticalAlignment(labelEndName, HasVerticalAlignment.ALIGN_MIDDLE);
		setCellWidth(labelEndName, "auto");
	}
}