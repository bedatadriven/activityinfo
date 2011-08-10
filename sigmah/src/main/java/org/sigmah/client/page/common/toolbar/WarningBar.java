package org.sigmah.client.page.common.toolbar;

import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.gwt.user.client.ui.Image;

public class WarningBar extends LayoutContainer {
	private LabelField labelMessage;
	private Image imageLocked;
	private HorizontalPanel panelMain;

	public WarningBar() {
		super();
		
		initializeComponent();
	}

	private void initializeComponent() {
		panelMain = new HorizontalPanel();
		add(panelMain);
		
		imageLocked = new Image(WarningBarResources.INSTANCE.lockedPeriod());
		panelMain.add(imageLocked);

		labelMessage = new LabelField();
		panelMain.add(labelMessage);

		setStyleName(WarningBarResources.INSTANCE.warningStyle().toString());
		setStyleAttribute("padding", "0.25em");
		setStyleAttribute("background-color", "#F5A9A9");
	}

	public String getWarning() {
		return labelMessage.getText();
	}

	public void setWarning(String message) {
		labelMessage.setText(message);
	}
	
}
