/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.loader.CommandLoadEvent;
import org.sigmah.client.dispatch.loader.PagingCmdLoader;
import org.sigmah.client.event.SiteEvent;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.grid.GridPresenter;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.entry.editor.PrintDataEntryForm;
import org.sigmah.client.page.entry.place.ActivityDataEntryPlace;
import org.sigmah.client.page.entry.place.DataEntryPlace;
import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.command.BatchCommand;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.Delete;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.UpdateSite;
import org.sigmah.shared.command.result.PagingResult;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteEditor extends AbstractSiteEditor implements Page, GridPresenter.SiteGridPresenter {
	public interface View extends AbstractSiteEditor.View {
        public void init(SiteEditor presenter, ActivityDTO activity, ListStore<SiteDTO> store);
	}
	
    public static final int PAGE_SIZE = 25;
    protected ListStore<SiteDTO> listStore;
    protected PagingCmdLoader<SiteResult> pagingCmdLoader;

    private Integer siteIdToSelectOnNextLoad;
	private View view;
	private DataEntryPlace place;

    @Inject
    public SiteEditor(EventBus eventBus, Dispatcher service, StateProvider stateMgr, final View view) {
        super(eventBus, service, stateMgr, view);
        
        this.view=view;
    }

    @Override
	public Store<SiteDTO> getStore() {
		return listStore;
	}

	@Override
	protected Loader createLoader() {
    	pagingCmdLoader = new PagingCmdLoader<SiteResult>(service);
    	return pagingCmdLoader;
	}

	@Override
	protected Store<SiteDTO> createStore() {
		listStore = new ListStore<SiteDTO>(pagingCmdLoader);
		return listStore;
	}

    @Override
    protected void onSiteCreated(SiteEvent se) {
        if (listStore.getCount() < PAGE_SIZE) {
            // there is only one page, so we can save some time by justing adding this model to directly to
            //  the store
        	listStore.add(se.getSite());
        } else {
            // there are multiple pages and we don't really know where this site is going
            // to end up, so do a reload and seek to the page with the new site
        	GetSites cmd = (GetSites) pagingCmdLoader.getCommand();
            cmd.setSeekToSiteId(se.getSite().getId());
            siteIdToSelectOnNextLoad = se.getSite().getId();
            pagingCmdLoader.load();
        }
    }

    @Override
    public int getPageSize() {
        return PAGE_SIZE;
    }

    @Override
    protected String getStateId() {
        return "SiteGrid" + currentActivity.getId();
    }

    @Override
    public PageId getPageId() {
        return DataEntryPage.PAGE_ID;
    }

    private String stateId(String suffix) {
        return "sitegridpage." + currentActivity.getId();
    }

    public void go(DataEntryPlace place, ActivityDTO activity) {
    	this.currentActivity=activity;
        initLoaderDefaults(pagingCmdLoader, place, new SortInfo("date2", Style.SortDir.DESC));
        view.init(this, currentActivity, listStore);
        load(filterPanel.getValue());
        setActionsDisabled();
    }

	@Override
    public boolean navigate(final PageState place) {
        if (!(place instanceof ActivityDataEntryPlace)) {
            return false;
        }

        final ActivityDataEntryPlace gridPlace = (ActivityDataEntryPlace) place;

        if (currentActivity.getId() != gridPlace.getActivityId()) {
            return false;
        }
        this.place=gridPlace;

        handleGridNavigation(pagingCmdLoader, gridPlace);

        return true;
    }

    protected void onLoaded(LoadEvent le) {
        super.onLoaded(le);

        PagingResult result = (PagingResult) le.getData();

        view.setActionEnabled(UIActions.export, result.getTotalLength() != 0);

        // Let everyone else know we have navigated
        firePageEvent(new ActivityDataEntryPlace(currentActivity), le);

        // Select a site
        if (siteIdToSelectOnNextLoad != null) {
            view.setSelection(siteIdToSelectOnNextLoad);
            siteIdToSelectOnNextLoad = null;
        }
    }

//    public void onSelectionChanged(SiteDTO selectedSite) {
//
//    }

    @Override
    protected void onBeforeLoad(CommandLoadEvent le) {
        super.onBeforeLoad(le);

        view.setActionEnabled(UIActions.add, currentActivity.getDatabase().isEditAllowed());
        view.setActionEnabled(UIActions.edit, false);
        view.setActionEnabled(UIActions.delete, false);
        view.setActionEnabled(UIActions.print, false);
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

        }else if (UIActions.print.equals(actionId)){
          onPrintDataEntryForm();
        }
    }
	
   private void onPrintDataEntryForm(){
    	PrintDataEntryForm dataEntryfromPrint = new PrintDataEntryForm(currentActivity,eventBus, service);
   }
    
    @Override
    protected void onDeleteConfirmed(final SiteDTO site) {
        service.execute(new Delete(site), view.getDeletingMonitor(), new AsyncCallback<VoidResult>() {
            public void onFailure(Throwable caught) {

            }

            public void onSuccess(VoidResult result) {
                listStore.remove(site);
            }
        });
    }

	@Override
	protected void setFilter(Filter filter) {
        GetSites cmd = new GetSites();
        cmd.setFilter(filter);
        
        pagingCmdLoader.setCommand(cmd);
        pagingCmdLoader.load();		
	}

	public DataEntryPlace getPlace() {
		return place;
	}
}
