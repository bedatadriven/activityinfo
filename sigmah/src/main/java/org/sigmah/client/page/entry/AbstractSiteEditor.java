package org.sigmah.client.page.entry;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.AppEvents;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.loader.CommandLoadEvent;
import org.sigmah.client.event.DownloadRequestEvent;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.event.SiteEvent;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.common.Shutdownable;
import org.sigmah.client.page.common.grid.AbstractEditorGridPresenter;
import org.sigmah.client.page.common.grid.GridView.SiteGridView;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.entry.editor.SiteFormPage;
import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.command.BatchCommand;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.Delete;
import org.sigmah.shared.command.UpdateSite;
import org.sigmah.shared.command.result.PagingResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.LockedPeriodDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public abstract class AbstractSiteEditor extends AbstractEditorGridPresenter<SiteDTO> implements Page {
    public interface View extends SiteGridView {
        public AsyncMonitor getLoadingMonitor();
        void setSelection(int siteId);
		public void showLockedPeriods(List<LockedPeriodDTO> list);
		public void setSite(SiteDTO selectedSite);
		public void update(SiteDTO site);
		public void setSelected(int id);
		public void remove(SiteDTO site);
    }
    
    private Listener<SiteEvent> siteChangedListener;
    private Listener<SiteEvent> siteCreatedListener;
    private Listener<SiteEvent> siteSelectedListner;
    private List<Shutdownable> subComponents = new ArrayList<Shutdownable>();

    private final View view;
    protected final EventBus eventBus;
    protected final Dispatcher service;

    protected ActivityDTO currentActivity;
    protected SiteDTO currentSite;
    protected Store<SiteDTO> store;
    protected Loader loader;

    private Integer siteIdToSelectOnNextLoad;

    @Inject
    public AbstractSiteEditor(EventBus eventBus, Dispatcher service, StateProvider stateMgr, final View view) {
        super(eventBus, service, stateMgr, view);
        
        this.view = view;
        this.eventBus = eventBus;
        this.service = service;

        loader = createLoader();
        store = createStore();
        
        initListeners(store, loader);

        addListeners(); 
    }

	protected abstract Loader createLoader();

	protected abstract Store<SiteDTO> createStore();

	private void addListeners() {
        this.eventBus.addListener(AppEvents.SiteChanged, new Listener<SiteEvent>() {
            public void handleEvent(SiteEvent se) {
                SiteDTO ourCopy = store.findModel("id", se.getSite().getId());
                if (ourCopy != null) {
                    ourCopy.setProperties(se.getSite().getProperties());
                }
                store.update(ourCopy);
            }
        });

        this.eventBus.addListener(AppEvents.SiteCreated, new Listener<SiteEvent>() {
            public void handleEvent(SiteEvent se) {
                onSiteCreated(se);
            }
        });

        this.eventBus.addListener(AppEvents.SiteSelected, new Listener<SiteEvent>() {
            public void handleEvent(SiteEvent se) {
                if (se.getSource() != AbstractSiteEditor.this) {
                	view.setSelected(se.getSiteId());
                }
            }
        });
	}
	
    protected void onSiteCreated(SiteEvent se) {
    }
    
    public void addSubComponent(Shutdownable subComponent) {
        subComponents.add(subComponent);
    }

    public void shutdown() {
        eventBus.removeListener(AppEvents.SiteChanged, siteChangedListener);
        eventBus.removeListener(AppEvents.SiteCreated, siteCreatedListener);
        eventBus.removeListener(AppEvents.SiteSelected, siteSelectedListner);

        for (Shutdownable subComponet : subComponents) {
            subComponet.shutdown();
        }
    }

    @Override
    public Store<SiteDTO> getStore() {
        return store;
    }

    @Override
    public Object getWidget() {
        return view;
    }

    public ActivityDTO getCurrentActivity() {
        return currentActivity;
    }

    private String stateId(String suffix) {
        return "sitegridpage." + currentActivity.getId();
    }

    public void go(SiteGridPageState place, ActivityDTO activity) {
        this.currentActivity = activity;
    }
//		@Override
//	    public boolean navigate(final PageState place) {
//			if (!super.navigate(place)) {
//				return false;
//			}
//	        final SiteGridPageState gridPlace = (SiteGridPageState) place;
//
//	        if (currentActivity.getId() != gridPlace.getActivityId()) {
//	            return false;
//	        }
//
//	        handleGridNavigation(loader, gridPlace);
//
//	        return true;
//	    }

	protected void setActionsDisabled() {
		view.setActionEnabled(UIActions.add, currentActivity.getDatabase().isEditAllowed());
        view.setActionEnabled(UIActions.edit, false);
        view.setActionEnabled(UIActions.delete, false);
	}

    protected void onLoaded(LoadEvent le) {
        super.onLoaded(le);

        PagingResult result = (PagingResult) le.getData();

        view.setActionEnabled(UIActions.export, result.getTotalLength() != 0);

        // Let everyone else know we have navigated
        firePageEvent(new SiteGridPageState(currentActivity), le);

        // Select a site
        if (siteIdToSelectOnNextLoad != null) {
            view.setSelection(siteIdToSelectOnNextLoad);
            siteIdToSelectOnNextLoad = null;
        }
    }

    public void onSelectionChanged(SiteDTO selectedSite) {
    	this.currentSite = selectedSite;
    	view.setSite(selectedSite);
    	
        if (selectedSite == null) {
            view.setActionEnabled(UIActions.delete, false);
            view.setActionEnabled(UIActions.edit, false);
        } else {
            boolean editable = isEditable(selectedSite);
            view.setActionEnabled(UIActions.delete, editable);
            view.setActionEnabled(UIActions.edit, editable);
            view.setActionEnabled(UIActions.showLockedPeriods, 
            		currentSite.fallsWithinLockedPeriod(currentActivity));
        }

        eventBus.fireEvent(new SiteEvent(AppEvents.SiteSelected, this, selectedSite));
    }

    @Override
    protected void onBeforeLoad(CommandLoadEvent le) {
        super.onBeforeLoad(le);

        setActionsDisabled();
    }

    private boolean isEditable(SiteDTO selectedSite) {
        UserDatabaseDTO db = currentActivity.getDatabase();
        boolean editable = (db.isEditAllAllowed() ||
                (db.isEditAllowed() && db.getMyPartnerId() == selectedSite.getPartner().getId())) &&
                !selectedSite.fallsWithinLockedPeriod(currentActivity);
        return editable;
    }

    @Override
    public boolean beforeEdit(Record record, String property) {
        return isEditable((SiteDTO) record.getModel());
    }

    @Override
    protected Command createSaveCommand() {
        BatchCommand batch = new BatchCommand();
        for (Record record : store.getModifiedRecords()) {
            batch.add(new UpdateSite((Integer) record.get("id"), getChangedProperties(record)));
        }

        return batch;
    }
    @Override
    public void onUIAction(String actionId) {
        super.onUIAction(actionId);
        if (UIActions.export.equals(actionId)) {
            onExport();
        } else if (UIActions.showLockedPeriods.equals(actionId)) {
        	view.showLockedPeriods(getLockedPeriods());
        } else if (UIActions.map.equals(actionId)) {

        }
    }

    protected List<LockedPeriodDTO> getLockedPeriods() {
    	List<LockedPeriodDTO> lockedPeriods = new ArrayList<LockedPeriodDTO>();
    	
    	lockedPeriods.addAll(currentActivity.getEnabledLockedPeriods());
    	lockedPeriods.addAll(currentActivity.getDatabase().getEnabledLockedPeriods());
    	
    	if (currentSite != null && currentSite.getProject() != null) {
    		lockedPeriods.addAll(currentSite.getProject().getEnabledLockedPeriods());
    	}
    	
    	return lockedPeriods;
	}

	protected void onAdd() {
        SiteDTO newSite = new SiteDTO();
        newSite.setActivityId(currentActivity.getId());

        if (!currentActivity.getDatabase().isEditAllAllowed()) {
            newSite.setPartner(currentActivity.getDatabase().getMyPartner());
        }

    	eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, new SiteFormPage.NewPageState(currentActivity.getId())));
	}

    protected void onEdit(SiteDTO site) {
    	eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, new SiteFormPage.EditPageState(site.getId())));
    }

    @Override
    protected void onDeleteConfirmed(final SiteDTO site) {
        service.execute(new Delete(site), view.getDeletingMonitor(), new AsyncCallback<VoidResult>() {
            public void onFailure(Throwable caught) {

            }

            public void onSuccess(VoidResult result) {
                view.remove(site);
            }
        });
    }

    protected void onExport() {
        String url = GWT.getModuleBaseURL() + "export?auth=#AUTH#&a=" + currentActivity.getId();
        eventBus.fireEvent(new DownloadRequestEvent("siteExport", url));
    }
}
