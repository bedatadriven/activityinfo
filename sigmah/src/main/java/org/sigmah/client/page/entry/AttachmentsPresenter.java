package org.sigmah.client.page.entry;

import java.util.UUID;

import org.sigmah.client.AppEvents;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.event.SiteEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.Shutdownable;
import org.sigmah.client.page.common.dialog.FormDialogCallback;
import org.sigmah.client.page.common.dialog.FormDialogImpl;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.command.CreateSiteAttachment;
import org.sigmah.shared.command.DeleteSiteAttachment;
import org.sigmah.shared.command.GetUploadUrl;
import org.sigmah.shared.command.result.UploadUrlResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class AttachmentsPresenter implements ActionListener, Shutdownable {

	public interface View {
		void setSelectionTitle(String title);

		void setActionEnabled(String id, boolean enabled);

		void setAttachmentStore(int siteId);

		String getSelectedItem();

		void refreshList();
	}

	private final EventBus eventBus;
	private final View view;
	private final Dispatcher dispatcher;
	private AttachmentForm form;
	private CreateSiteAttachment siteAttachment;
	private SiteDTO currentSite;
	private Listener<SiteEvent> siteListener;
	private String blobid;

	@Inject
	public AttachmentsPresenter(EventBus eventBus, Dispatcher service, View view) {
		this.eventBus = eventBus;
		this.dispatcher = service;
		this.view = view;

		siteListener = new Listener<SiteEvent>() {
			public void handleEvent(SiteEvent be) {
				if (be.getType() == AppEvents.SiteSelected
						&& be.getSite() != null) {
					currentSite = be.getSite();
					onSiteSelected();
				}
			}
		};
		eventBus.addListener(AppEvents.SiteSelected, siteListener);
	}

	public void shutdown() {
		eventBus.removeListener(AppEvents.SiteSelected, siteListener);
	}

	private void onSiteSelected() {
		view.setSelectionTitle(currentSite.getLocationName());
		view.setActionEnabled(UIActions.upload, true);
		view.setAttachmentStore(currentSite.getId());
	}

	@Override
	public void onUIAction(String actionId) {
		if (UIActions.delete.equals(actionId)) {
			onDelete();
		} else if (UIActions.upload.equals(actionId)) {
			onUpload();
		}

	}

	public void onUpload() {

		form = new AttachmentForm();
		form.setEncoding(Encoding.MULTIPART);
		form.setMethod(Method.POST);

		final FormDialogImpl dialog = new FormDialogImpl(form);
		dialog.setWidth(400);
		dialog.setHeight(200);
		dialog.setHeading(I18N.CONSTANTS.newAttachment());

		dialog.show(new FormDialogCallback() {
			@Override
			public void onValidated() {
				uploadFile();
			}
		});

	}

	public void uploadFile() {
		blobid = UUID.randomUUID().toString();
		dispatcher.execute(new GetUploadUrl(blobid), null,
				new AsyncCallback<UploadUrlResult>() {
					public void onFailure(Throwable caught) {
						// callback.onFailure(caught);
					}

					@Override
					public void onSuccess(UploadUrlResult result) {
						form.setAction(result.getUrl());

					}
				});
		createSiteAttachment(blobid);
	}

	public void createSiteAttachment(String blobid) {
		siteAttachment = new CreateSiteAttachment();
		siteAttachment.setSiteId(currentSite.getId());
		siteAttachment.setBlobId(blobid);
		siteAttachment.setFileName("file.doc");

		dispatcher.execute(siteAttachment, null,
				new AsyncCallback<VoidResult>() {
					public void onFailure(Throwable caught) {
						// callback.onFailure(caught);
					}

					@Override
					public void onSuccess(VoidResult result) {
						view.setAttachmentStore(currentSite.getId());
					}
				});

	}

	public void onDelete() {

		DeleteSiteAttachment attachment = new DeleteSiteAttachment();
		attachment.setBlobId(view.getSelectedItem());

		dispatcher.execute(attachment, null, new AsyncCallback<VoidResult>() {
			public void onFailure(Throwable caught) {
				// callback.onFailure(caught);
			}

			@Override
			public void onSuccess(VoidResult result) {
				view.setAttachmentStore(currentSite.getId());
			}
		});
	}

}
