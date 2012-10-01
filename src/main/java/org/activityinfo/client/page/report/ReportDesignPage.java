package org.activityinfo.client.page.report;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.NavigationHandler;
import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.common.dialog.SaveChangesCallback;
import org.activityinfo.client.page.common.dialog.SavePromptMessageBox;
import org.activityinfo.client.page.common.toolbar.ExportCallback;
import org.activityinfo.client.page.report.editor.CompositeEditor2;
import org.activityinfo.client.page.report.editor.EditorProvider;
import org.activityinfo.client.page.report.editor.ReportElementEditor;
import org.activityinfo.client.page.report.resources.ReportResources;
import org.activityinfo.client.page.report.template.ReportTemplate;
import org.activityinfo.shared.command.BatchCommand;
import org.activityinfo.shared.command.CreateReport;
import org.activityinfo.shared.command.GetReportModel;
import org.activityinfo.shared.command.GetReports;
import org.activityinfo.shared.command.RenderElement.Format;
import org.activityinfo.shared.command.UpdateReportModel;
import org.activityinfo.shared.command.UpdateReportSubscription;
import org.activityinfo.shared.command.result.BatchResult;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.command.result.ReportsResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.ReportMetadataDTO;
import org.activityinfo.shared.report.model.Report;

import org.activityinfo.client.Log;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.EditorEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.common.base.Strings;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class ReportDesignPage extends ContentPanel implements Page, ExportCallback {

	private class SaveCallback implements AsyncCallback<VoidResult> {
		@Override
		public void onSuccess(VoidResult result) {
			Info.display(I18N.CONSTANTS.saved(), I18N.MESSAGES.reportSaved(currentModel.getTitle()));
			onSaved();
		}

		@Override
		public final void onFailure(Throwable caught) {
			MessageBox.alert(I18N.CONSTANTS.serverError(), caught.getMessage(), null);
		}
		
		public void onSaved() {
		}
	}


	public static final PageId PAGE_ID = new PageId("reportdesign");

	private final EventBus eventBus;
	private final Dispatcher dispatcher;
	private final EditorProvider editorProvider;
	
	private boolean reportEdited;
	private ReportBar reportBar;
	
	private boolean dirty = false;
	
	/**
	 * The model being edited on this page
	 */
	private Report currentModel;
	private ReportMetadataDTO currentMetadata;
	 

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
		
		eventBus.addListener(ReportChangeEvent.TYPE, new Listener<ReportChangeEvent>() {

			@Override
			public void handleEvent(ReportChangeEvent event) {
				if(event.getModel() == currentModel || 
						currentModel.getElements().contains(event.getModel())) {
					Log.debug("marking report as dirty");
					dirty = true;
				}
			}
		});
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
					save(new SaveCallback());
				}
			}
		});

		reportBar.getSaveButton().addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				ensureTitledThenSave(null, new SaveCallback());
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
		
		reportBar.getSwitchViewButton().addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				switchView();
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
		loadReport(reportId);
	}

	private void loadReport(int reportId) {

		BatchCommand batch = new BatchCommand();
		batch.add(new GetReportModel(reportId));
		batch.add(new GetReports());
		
		dispatcher.execute(batch, new MaskingAsyncMonitor(this, I18N.CONSTANTS.loading()),
				new AsyncCallback<BatchResult>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO show appropriate message.
			}

			@Override
			public void onSuccess(BatchResult result) {						
				onModelLoaded((Report) result.getResult(0), (ReportsResult)result.getResult(1));
			}
		});
	}

	private void onModelLoaded(Report report, ReportsResult reportsResult) {
		try {
			this.currentMetadata = reportsResult.forId(report.getId());
		} catch(IllegalArgumentException e) {
			this.currentMetadata = new ReportMetadataDTO();
			this.currentMetadata.setId(report.getId());
		}
		this.currentModel = report;
		
		reportBar.setReportTitle(currentModel.getTitle());
		reportBar.getDashboardButton().toggle(currentMetadata.isDashboard());
		
		if(currentModel.getElements().size() == 1) {
			ReportElementEditor editor = editorProvider.create(currentModel.getElement(0));
			editor.bind(currentModel.getElement(0));
			installEditor( editor );
			reportBar.getSwitchViewButton().setVisible(true);
		} else {
			installCompositeEditor();
		}		
	}

	private void installCompositeEditor() {
		CompositeEditor2 editor = (CompositeEditor2)editorProvider.create(currentModel);
		editor.bind(currentModel);
		installEditor( editor );
		
		reportBar.getSwitchViewButton().setVisible(false);
	}
	

	protected void switchView() {
		installCompositeEditor();
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

	private void pinToDashboard(final boolean pressed) {
		ensureTitled(new SaveCallback() {
			@Override
			public void onSaved() {
				final UpdateReportSubscription update = new UpdateReportSubscription();
				update.setReportId(currentModel.getId());
				update.setPinnedToDashboard(pressed);
				
				dispatcher.execute(update, new SaveCallback() {
					@Override
					public void onSuccess(VoidResult result) {
						if(update.getPinnedToDashboard()) {
							Info.display(I18N.CONSTANTS.saved(), 
									I18N.MESSAGES.addedToDashboard(currentModel.getTitle()));
						} else {
							Info.display(I18N.CONSTANTS.saved(), 
									I18N.MESSAGES.removedFromDashboard(currentModel.getTitle()));
						}
					}
					
				});
			}
		});
	}

	private void ensureTitledThenSave(AsyncMonitor monitor, SaveCallback callback) {
		if (untitled()) {
			promptForTitle(callback);
		} else {
			save(monitor, callback);
		}
	}

	private void ensureTitled(SaveCallback callback) {
		if (untitled()) {
			promptForTitle(callback);
		} else {
			callback.onSaved();
		}
	}
	
	private void promptForTitle(final AsyncCallback<VoidResult> callback) {
		MessageBox.prompt(I18N.CONSTANTS.save(), I18N.CONSTANTS.chooseReportTitle(), new Listener<MessageBoxEvent>() {
			
			@Override
			public void handleEvent(MessageBoxEvent be) {
				String newTitle = be.getMessageBox().getTextBox().getValue();
				if(!Strings.isNullOrEmpty(newTitle)) {
					currentModel.setTitle(newTitle);
					reportBar.setReportTitle(newTitle);
					save(callback);
				}
			}
		});
	}
	
	private void save(AsyncCallback<VoidResult> callback) {
		save(null, callback);
	}
	
	private void save(AsyncMonitor monitor, final AsyncCallback<VoidResult> callback) {
		if (currentMetadata.isEditAllowed()) {
			performUpdate(monitor, callback);
		} else {
			confirmCreate(monitor, callback);
		}
	}
	
	private void performUpdate(AsyncMonitor monitor, final AsyncCallback<VoidResult> callback) {
		UpdateReportModel updateReport = new UpdateReportModel();
		updateReport.setModel(currentModel);
		
		dispatcher.execute(updateReport, monitor, new AsyncCallback<VoidResult>() {
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
			@Override
			public void onSuccess(VoidResult result) {
				dirty = false;
				callback.onSuccess(result);
			}
		});
	}
	
	private void confirmCreate(final AsyncMonitor monitor, final AsyncCallback<VoidResult> callback) {
		MessageBox.confirm(I18N.CONSTANTS.save(), I18N.MESSAGES.confirmSaveCopy(), new Listener<MessageBoxEvent>() {
			@Override
			public void handleEvent(MessageBoxEvent be) {
				Button btn = be.getButtonClicked();
				if (Dialog.YES.equalsIgnoreCase(btn.getItemId())) {
					performCreate();
				}
			}
		});
	}

	private void performCreate() {
		currentModel.setTitle(currentModel.getTitle() + " (" + I18N.CONSTANTS.copy() + ")");
		
		dispatcher.execute(new CreateReport(currentModel), new AsyncCallback<CreateResult>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(CreateResult created) {
				eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, 
						new ReportDesignPageState(created.getNewId())));
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
			final NavigationCallback callback) {
		if(!dirty) {
			callback.onDecided(true);
		} else {
			SavePromptMessageBox box = new SavePromptMessageBox();
			box.show(new SaveChangesCallback() {
				
				@Override
				public void save(AsyncMonitor monitor) {
					ensureTitledThenSave(monitor, new SaveCallback() {

						@Override
						public void onSaved() {
							callback.onDecided(true);
						}
					});
				}
				
				@Override
				public void discard() {
					callback.onDecided(true);
				}
				
				@Override
				public void cancel() {
					callback.onDecided(false);
				}
			});
			
		}
	}

	@Override
	public String beforeWindowCloses() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void showShareForm() {
		ensureTitled(new SaveCallback() {

			@Override
			public void onSaved() {
				final ShareReportDialog dialog = new ShareReportDialog(dispatcher);
				//form.updateForm(currentReportId);
				dialog.show(currentModel);
			}
		});
	}

	@Override
	public void export(Format format) {
		ExportDialog dialog = new ExportDialog(dispatcher);
		dialog.export(currentEditor.getModel(), format);
	}

	private boolean untitled() {
		return currentModel.getTitle()==null;
	}
}
