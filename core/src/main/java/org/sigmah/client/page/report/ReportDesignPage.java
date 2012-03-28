package org.sigmah.client.page.report;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.callback.DownloadCallback;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.dialog.FormDialogCallback;
import org.sigmah.client.page.common.dialog.FormDialogImpl;
import org.sigmah.client.page.common.toolbar.ExportCallback;
import org.sigmah.client.page.report.editor.CompositeEditor;
import org.sigmah.client.page.report.editor.EditorProvider;
import org.sigmah.client.page.report.editor.ReportElementEditor;
import org.sigmah.client.page.report.resources.ReportResources;
import org.sigmah.shared.command.CreateSubscribe;
import org.sigmah.shared.command.GetReportModel;
import org.sigmah.shared.command.RenderElement;
import org.sigmah.shared.command.RenderElement.Format;
import org.sigmah.shared.command.UpdateReportModel;
import org.sigmah.shared.command.UpdateReportSubscription;
import org.sigmah.shared.command.result.RenderResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.report.model.Report;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.EditorEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.common.base.Strings;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class ReportDesignPage extends ContentPanel implements Page, ExportCallback {

	public static final PageId PAGE_ID = new PageId("reportdesign");

	private final EventBus eventBus;
	private final Dispatcher dispatcher;
	private final EditorProvider editorProvider;

	private boolean reportEdited;
	private ReportBar reportBar;
	
	/**
	 * The model being edited on this page
	 */
	private Report currentModel;
	 

	/**
	 * The editor for the model
	 */
	private ReportElementEditor currentEditor;

	
	@Inject
	public ReportDesignPage(EventBus eventBus, Dispatcher service, EditorProvider editorProvider) {
		this.eventBus = eventBus;
		this.dispatcher = service;
		this.editorProvider = editorProvider;

		ReportResources.INSTANCE.style().ensureInjected();

		setLayout(new BorderLayout());
		setHeaderVisible(false);

		createToolbar();
	}

	public void createToolbar() {
		reportBar = new ReportBar();
		BorderLayoutData reportBarLayout = new BorderLayoutData(LayoutRegion.NORTH);
		reportBarLayout.setSize(35);
		add(reportBar, reportBarLayout);

		reportBar.getExportButton().setCallback(this);

		
		reportBar.addTitleEditCompleteListener(new Listener<EditorEvent>() {
			@Override
			public void handleEvent(EditorEvent be) {
				String newTitle = (String)be.getValue();
				if(newTitle != null && !newTitle.equals(currentModel.getTitle())) {
					currentModel.setTitle(newTitle);
					reportBar.setReportTitle(newTitle);
					save();
				}
			}
		});

		reportBar.getSaveButton().addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				if(currentModel.getTitle()==null) {
					promptForTitle();
				} else {
					save();
				}
			}
		});
		
		reportBar.getShareButton().addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				showShareForm();
			}
			
		});
		
		reportBar.getDashboardButton().addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				pinToDashboard(reportBar.getDashboardButton().isPressed());
			}
		});
	}


	@Override
	public boolean navigate(PageState place) {
		if(place instanceof ReportDesignPageState) {
			go(((ReportDesignPageState) place).getReportId());
			return true;
		}
		return false;
	}


	public void go(int reportId) {
		// TODO if report id is not valid one, should create new report and dont send command to server.
		loadReport(reportId);
	}

	private void loadReport(int reportId) {

		dispatcher.execute(new GetReportModel(reportId), new MaskingAsyncMonitor(this, I18N.CONSTANTS.loading()),
				new AsyncCallback<Report>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO show appropriate message.
			}

			@Override
			public void onSuccess(Report result) {						
				onModelLoaded(result);
			}
		});
	}

	private void onModelLoaded(Report report) {
		this.currentModel = report;
		reportBar.setReportTitle(currentModel.getTitle());
		
		if(currentModel.getElements().size() == 1) {
			ReportElementEditor editor = editorProvider.create(currentModel.getElement(0));
			editor.bind(currentModel.getElement(0));
			installEditor( editor );
		} else {
			CompositeEditor editor = (CompositeEditor)editorProvider.create(currentModel);
			editor.bind(currentModel);
			installEditor( editor );
		}
		
	}

	private void installEditor(ReportElementEditor editor) {
		if(currentEditor != null) {
			remove(currentEditor.getWidget());
		}
		
		reportBar.getExportButton().setFormats(editor.getExportFormats());
		
		add(editor.getWidget(), new BorderLayoutData(LayoutRegion.CENTER));
		this.currentEditor = editor;
		layout();
	}

	public void save() {
		UpdateReportModel updateReport = new UpdateReportModel();
		updateReport.setModel(currentModel);

		dispatcher.execute(updateReport, null, new AsyncCallback<VoidResult>() {
			@Override
			public void onFailure(Throwable caught) {
				// callback.onFailure(caught);
			}

			@Override
			public void onSuccess(VoidResult result) {
				//loadReport(view.getReport().getId());
			}
		});
	}
	
	private void pinToDashboard(boolean pressed) {
		UpdateReportSubscription update = new UpdateReportSubscription();
		update.setReportId(currentModel.getId());
		update.setPinnedToDashboard(pressed);
		
		dispatcher.execute(update, null, new AsyncCallback<VoidResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(VoidResult result) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	
	public void promptForTitle() {
		MessageBox.prompt(I18N.CONSTANTS.save(), I18N.CONSTANTS.chooseReportTitle(), new Listener<MessageBoxEvent>() {
			
			@Override
			public void handleEvent(MessageBoxEvent be) {
				String newTitle = be.getMessageBox().getTextBox().getValue();
				if(!Strings.isNullOrEmpty(newTitle)) {
					currentModel.setTitle(newTitle);
					reportBar.setReportTitle(newTitle);
					save();
				}
			}
		});
	}

	public void setReportEdited(boolean edited){
		reportEdited = edited;
	}

	public boolean reportEdited(){
		return reportEdited;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public PageId getPageId() {
		return PAGE_ID;
	}

	@Override
	public Object getWidget() {
		return this;
	}

	@Override
	public void requestToNavigateAway(PageState place,
			NavigationCallback callback) {
		callback.onDecided(true);
	}

	@Override
	public String beforeWindowCloses() {
		// TODO Auto-generated method stub
		return null;
	}

	public void showShareForm() {
		final ShareReportDialog dialog = new ShareReportDialog(dispatcher);
		//form.updateForm(currentReportId);
		dialog.show(currentModel);
	}

	public void subscribeEmail(final FormDialogImpl<SubscribeForm> dialog, int reportId) {

		CreateSubscribe sub = new CreateSubscribe();
		sub.setReportTemplateId(reportId);
		sub.setEmailsList(dialog.getForm().getEmailList());
		sub.setReportFrequency(dialog.getForm().getReportFrequency());
		sub.setDay(dialog.getForm().getDay());

		dispatcher.execute(sub, dialog, new AsyncCallback<VoidResult>() {
			public void onFailure(Throwable caught) {
				dialog.onServerError();
			}

			public void onSuccess(VoidResult result) {
				dialog.hide();
				//loadReport(selectedReport.getId());
			}
		});
	}

	@Override
	public void export(Format format) {
		dispatcher.execute(new RenderElement(currentEditor.getModel(), format), null, new DownloadCallback(eventBus));
	}
}
