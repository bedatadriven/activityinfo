package org.sigmah.client.page.entry;

import org.sigmah.client.AppEvents;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.event.SiteEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.page.common.Shutdownable;
import org.sigmah.client.page.common.dialog.FormDialogCallback;
import org.sigmah.client.page.common.dialog.FormDialogImpl;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.entry.editor.SiteRenderer;
import org.sigmah.shared.command.CreateSiteAttachment;
import org.sigmah.shared.command.DeleteSiteAttachment;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.GetSiteAttachments;
import org.sigmah.shared.command.GetUploadUrl;
import org.sigmah.shared.command.result.SiteAttachmentResult;
import org.sigmah.shared.command.result.UploadUrlResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.SiteAttachmentDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.inject.Inject;

public class AttachmentsPresenter implements ActionListener, Shutdownable {

	public interface View {
		void setSelectionTitle(String title);

		void setActionEnabled(String id, boolean enabled);

		void setAttachmentStore(int siteId);
		
		int getSelectedItem();
	}

	private final EventBus eventBus;
	private final View view;
	private final NumberFormat indicatorFormat;
	private final Dispatcher dispatcher;
	private AttachmentForm form;
	private CreateSiteAttachment siteAttachment;

	private SiteDTO currentSite;

	private Listener<SiteEvent> siteListener;

	@Inject
	public AttachmentsPresenter(EventBus eventBus, Dispatcher service, View view) {
		this.eventBus = eventBus;
		this.dispatcher = service;
		this.view = view;

		indicatorFormat = NumberFormat.getFormat("#,###");

		siteListener = new Listener<SiteEvent>() {
			public void handleEvent(SiteEvent be) {
				if (be.getType() == AppEvents.SiteSelected
						&& be.getSite() != null) {
					onSiteSelected(be.getSite());
				} 
			}
		};
		eventBus.addListener(AppEvents.SiteSelected, siteListener);
	}

	public void shutdown() {
		eventBus.removeListener(AppEvents.SiteSelected, siteListener);
	}

	private void onSiteSelected(SiteDTO site) {
		this.currentSite = site;
		view.setSelectionTitle(site.getLocationName());
		view.setActionEnabled(UIActions.upload, true);
		view.setAttachmentStore(site.getId());
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
				createSiteAttachment();

			}
		});

	}

	public void uploadFile() {
		dispatcher.execute(new GetUploadUrl(), null,
				new AsyncCallback<UploadUrlResult>() {
					public void onFailure(Throwable caught) {
						// callback.onFailure(caught);
					}

					@Override
					public void onSuccess(UploadUrlResult result) {
						// TODO Auto-generated method stub
						form.setAction(result.getUrl());

					}
				});
	}

	public void createSiteAttachment() {
		siteAttachment = new CreateSiteAttachment();
		siteAttachment.setSiteId(currentSite.getId());
		siteAttachment.setBlobId("12abc6543");
		siteAttachment.setFileName("file.doc");

		dispatcher.execute(siteAttachment, null,
				new AsyncCallback<VoidResult>() {
					public void onFailure(Throwable caught) {
						// callback.onFailure(caught);
					}

					@Override
					public void onSuccess(VoidResult result) {
						// TODO Auto-generated method stub
						Window.alert("Site Attachment Created.");

					}
				});

	}
	
	public void getSiteAttachments(){
		
		GetSiteAttachments getAttachments = new GetSiteAttachments();
		getAttachments.setSiteId(currentSite.getId());
		
		dispatcher.execute(getAttachments, null,
				new AsyncCallback<SiteAttachmentResult>() {
					public void onFailure(Throwable caught) {
						// callback.onFailure(caught);
					}

					@Override
					public void onSuccess(SiteAttachmentResult result) {
						// TODO Auto-generated method stub
						Window.alert("Site Attachment founds." + result.getData().size());

					}
				});
		
		
	}
	
	public void onDelete(){
		
		DeleteSiteAttachment attachment = new DeleteSiteAttachment();
		attachment.setAttachmentId(view.getSelectedItem());
		
		dispatcher.execute(attachment, null,
				new AsyncCallback<VoidResult>() {
					public void onFailure(Throwable caught) {
						// callback.onFailure(caught);
					}

					@Override
					public void onSuccess(VoidResult result) {
						// TODO Auto-generated method stub
						Window.alert("Site Attachment Deleted!");

					}
				});
	}

}
