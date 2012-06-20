package org.activityinfo.client.page.entry;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.common.toolbar.ActionToolBar;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.shared.command.GetSiteAttachments;
import org.activityinfo.shared.command.result.SiteAttachmentResult;
import org.activityinfo.shared.dto.SiteAttachmentDTO;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.Style;
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
	protected ListStore<SiteAttachmentDTO> store;

	private AttachmentsPresenter presenter;
	private final Dispatcher dispatcher;
	private final EventBus eventBus;

	private ListView<SiteAttachmentDTO> attachmentList;

	public AttachmentsTab(Dispatcher service, final EventBus eventBus) {
		this.dispatcher = service;
		this.eventBus = eventBus;
		presenter = new AttachmentsPresenter(service, this);

		setText(I18N.CONSTANTS.attachment());
		setLayout(new FitLayout());

		createToolBar();

		panel = new ContentPanel();
		panel.setHeading(I18N.CONSTANTS.attachment());
		panel.setScrollMode(Style.Scroll.AUTOY);
		panel.setTopComponent(toolBar);
		panel.setLayout(new FitLayout());

		store = new ListStore<SiteAttachmentDTO>();

		attachmentList = new ListView<SiteAttachmentDTO>();
		attachmentList.setTemplate(getTemplate(GWT.getModuleBaseURL()
				+ "image/"));
		attachmentList.setBorders(false);
		attachmentList.setStore(store);
		attachmentList.setItemSelector("dd");
		attachmentList.setOverStyle("over");

		attachmentList.addListener(Events.Select,
				new Listener<ListViewEvent<SiteAttachmentDTO>>() {

					@Override
					public void handleEvent(
							ListViewEvent<SiteAttachmentDTO> event) {
						toolBar.setActionEnabled(UIActions.DELETE, true);
					}
				});

		attachmentList.addListener(Events.DoubleClick,
				new Listener<ListViewEvent<SiteAttachmentDTO>>() {

					@Override
					public void handleEvent(
							ListViewEvent<SiteAttachmentDTO> event) {
						String currentAttachment = event.getModel().getBlobId();
						Window.Location.assign(GWT.getModuleBaseURL() + "attachment?blobId=" + 
								event.getModel().getBlobId());						
					}
				});
		panel.add(attachmentList);

		add(panel);
	}

	public void createToolBar() {

		toolBar = new ActionToolBar();
		toolBar.addUploadButton();
		toolBar.add(new SeparatorToolItem());
		toolBar.addDeleteButton();
		toolBar.setListener(presenter);
		toolBar.setUploadEnabled(false);
		toolBar.setDeleteEnabled(false);

	}

	@Override
	public void setSelectionTitle(String title) {
		panel.setHeading(I18N.CONSTANTS.attachment() + " - " + title);

	}

	@Override
	public void setActionEnabled(String id, boolean enabled) {
		toolBar.setActionEnabled(id, enabled);
	}

	@Override
	public void setAttachmentStore(int siteId) {

		GetSiteAttachments getAttachments = new GetSiteAttachments();
		getAttachments.setSiteId(siteId);

		dispatcher.execute(getAttachments, 
				new AsyncCallback<SiteAttachmentResult>() {
					@Override
					public void onFailure(Throwable caught) {
						// callback.onFailure(caught);
					}

					@Override
					public void onSuccess(SiteAttachmentResult result) {
						store.removeAll();
						store.add(result.getData());
					}
				});

	}

	private native String getTemplate(String base) /*-{
		return [ '<dl><tpl for=".">', '<dd>',
				'<img src="' + base + 'attach.png" title="{fileName}">',
				'<span>{fileName}</span>', '</tpl>',
				'<div style="clear:left;"></div></dl>' ].join("");

	}-*/;

	@Override
	public String getSelectedItem() {
		return attachmentList.getSelectionModel().getSelectedItem().getBlobId();
	}

	@Override
	public void refreshList() {
		attachmentList.refresh();
	}

	public void setSite(SiteDTO site) {
		presenter.showSite(site);
	}

}
