package org.sigmah.client.page.entry;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.command.DeleteSiteAttachment;
import org.sigmah.shared.command.GetSiteAttachments;
import org.sigmah.shared.command.result.SiteAttachmentResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SiteAttachmentDTO;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.aria.NavigationHandler;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AttachmentsTab extends TabItem implements
		AttachmentsPresenter.View {

	protected ActionToolBar toolBar;
	private ContentPanel panel;
	protected ListStore<AttachmentModel> store;

	private AttachmentsPresenter presenter;
	private final EventBus eventBus;
	private final ActivityDTO activity;
	private final UIConstants messages;
	private final Dispatcher dispatcher;

	
	private int currentAttachment;
	
	public AttachmentsTab(final EventBus eventBus, Dispatcher service,
			ActivityDTO activity, UIConstants messages) {
		this.eventBus = eventBus;
		this.dispatcher = service;
		this.activity = activity;
		this.messages = messages;
		presenter = new AttachmentsPresenter(eventBus, service, this);

		setText(I18N.CONSTANTS.attachment());
		setLayout(new FitLayout());

		toolBar = new ActionToolBar();
		toolBar.addUploadButton();
		toolBar.setUploadEnabled(false);
		toolBar.add(new SeparatorToolItem());
		toolBar.addDeleteButton();
		toolBar.setDeleteEnabled(false);
		toolBar.setListener(presenter);

		panel = new ContentPanel();
		panel.setHeading(I18N.CONSTANTS.attachment());
		panel.setScrollMode(Style.Scroll.AUTOY);
		panel.setTopComponent(toolBar);
		panel.setLayout(new FitLayout());

		store = new ListStore<AttachmentModel>();

		ListView<AttachmentModel> view = new ListView<AttachmentModel>();
		view.setTemplate(getTemplate(GWT.getModuleBaseURL() + "image/"));
		view.setBorders(false);
		view.setStore(store);
		view.setItemSelector("dd");
		view.setOverStyle("over");

		view.addListener(Events.Select,
				new Listener<ListViewEvent<AttachmentModel>>() {

					public void handleEvent(ListViewEvent<AttachmentModel> event) {
//						eventBus.fireEvent(new NavigationEvent(
//								NavigationHandler.NavigationRequested, event
//										.getModel().getAttachmentId()));
						currentAttachment = event.getModel().getAttachmentId();
					}
				});
		panel.add(view);

		add(panel);
	}

	public void setSelectionTitle(String title) {
		panel.setHeading(I18N.CONSTANTS.attachment() + " - " + title);

	}

	public void setActionEnabled(String id, boolean enabled) {
		toolBar.setActionEnabled(id, enabled);
	}

	public void setAttachmentStore(int siteId) {

		GetSiteAttachments getAttachments = new GetSiteAttachments();
		getAttachments.setSiteId(siteId);

		dispatcher.execute(getAttachments, null,
				new AsyncCallback<SiteAttachmentResult>() {
					public void onFailure(Throwable caught) {
						// callback.onFailure(caught);
					}

					@Override
					public void onSuccess(SiteAttachmentResult result) {
						store.removeAll();
						for (SiteAttachmentDTO a : result.getData()) {
							AttachmentModel model = new AttachmentModel();
							model.setAttachmentId(a.getAttachmentId());
							model.setSiteId(a.getSiteId());
							model.setBlobId(a.getBlobId());
							model.setFileName(a.getFileName());
							model.setUploadedBy(a.getUploadedBy());
							store.add(model);
						}

					}
				});

	}

	private native String getTemplate(String base) /*-{
		return [ '<dl><tpl for=".">', '<dd>',
				'<img src="' + base + 'excel.png" title="{fileName}">',
				'<span>{fileName}</span>', '</tpl>',
				'<div style="clear:left;"></div></dl>' ].join("");

	}-*/;
	
	public int getSelectedItem(){
		return currentAttachment;
	}
}
