package org.sigmah.client.page.common.dialog;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class MailingListDialog extends Window {

	protected Button CloseButton;
	private final String mailingList;

	public MailingListDialog(String mailingList) {

		this.mailingList = mailingList;
		initializeComponent();

		createTextField();
		createCloseButton();
	}

	private void initializeComponent() {
		setModal(true);
		setLayout(new FitLayout());
		setModal(true);
		setClosable(false);
		setBodyStyle("padding: 5px;");
		setLayout(new FitLayout());
	}

	private void createCloseButton() {
		CloseButton = new Button(I18N.CONSTANTS.close());
		CloseButton.setIcon(IconImageBundle.ICONS.close());
		addButton(CloseButton);
		CloseButton.addListener(Events.Select, new Listener<ButtonEvent>() {
			public void handleEvent(ButtonEvent be) {
				hide();
			}
		});
	}

	private void createTextField() {
		TextArea emailsTextField = new TextArea();
		emailsTextField.setHeight("175px");
		emailsTextField.setWidth("250px");
		emailsTextField.setValue(mailingList);
		emailsTextField.selectAll();
		
		add(emailsTextField);
	}

	public void show() {
		super.show();
	}
}
