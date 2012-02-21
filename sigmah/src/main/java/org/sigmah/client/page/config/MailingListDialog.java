package org.sigmah.client.page.config;

import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.command.GetUsers;
import org.sigmah.shared.command.result.UserResult;
import org.sigmah.shared.dto.UserPermissionDTO;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MailingListDialog extends Window {

	private final EventBus eventBus;
	private final Dispatcher service;
	private final int dbId;

	private Button CloseButton;
	private TextArea emailsTextField ;

	public MailingListDialog(EventBus eventBus, Dispatcher service, int dbId) {

		this.eventBus =eventBus;
		this.service = service;
		this.dbId = dbId;
		
		initializeComponent();
		hide();
		
		createTextField();
		createCloseButton();
		loadUsers();
	}

	private void initializeComponent() {
		setHeading(I18N.CONSTANTS.mailingList());
		setModal(true);
		setLayout(new FitLayout());
		setModal(true);
		setClosable(false);
		setBodyStyle("padding: 5px;");
		setLayout(new FitLayout());
	}

	private void createCloseButton() {
		CloseButton = new Button(I18N.CONSTANTS.close());
		addButton(CloseButton);
		CloseButton.addListener(Events.Select, new Listener<ButtonEvent>() {
			public void handleEvent(ButtonEvent be) {
				hide();
			}
		});
	}

		
	private void createTextField() {
		emailsTextField = new TextArea();
		emailsTextField.setHeight("175px");
		emailsTextField.setWidth("250px");
		emailsTextField.selectAll();
		add(emailsTextField);
	}

	public void show() {
		super.show();
	}

	private void loadUsers() {
		service.execute(new GetUsers(dbId), null, new AsyncCallback<UserResult>() {

			public void onFailure(Throwable caught) {
			}

			public void onSuccess(UserResult result) {
				String emails = CreateMailingList(result.getData());
				emailsTextField.setValue(emails);
				show();
			}
		});
	}

	private String CreateMailingList(List<UserPermissionDTO> users ) {
		StringBuilder emails = new StringBuilder();

		for (UserPermissionDTO dto : users) {
			emails.append("\"" + dto.getName() + "\"");
			emails.append(" ");
			emails.append("<" + dto.getEmail() + ">,");
			emails.append(" ");
		}

		return emails.toString();

	}

}
