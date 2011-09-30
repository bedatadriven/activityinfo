package org.sigmah.client.page.entry;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.grid.GridPresenter;
import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.store.TreeStore;

public class SiteTreeEditor extends AbstractSiteEditor implements GridPresenter.SiteGridPresenter{
	public interface View extends AbstractSiteEditor.View {
        public void init(SiteTreeEditor presenter, ActivityDTO activity, TreeStore<SiteDTO> store);
	}

	private SiteTreeLoader treeLoader;
	private TreeStore<SiteDTO> treeStore;
	private View treeView;
	private SiteTreeProxy proxy;
	
	public SiteTreeEditor(EventBus eventBus, Dispatcher service, StateProvider stateMgr, SiteTreeEditor.View view) {
		super(eventBus, service, stateMgr, view);
		
		this.treeView=view;
	}
	
    public void go(SiteTreeGridPageState place, ActivityDTO activity) {
        this.currentActivity = activity;
        treeView.init(this, activity, treeStore);
        proxy.setPlace(place);
        //load(filterPanel.getValue());
        setActionsDisabled();
    }

	@Override
	protected Loader createLoader() {
		proxy = new SiteTreeProxy(service);
		treeLoader = new SiteTreeLoader(proxy);
		return treeLoader;
	}

	@Override
	protected TreeStore<SiteDTO> createStore() {
		treeStore = new TreeStore<SiteDTO>(treeLoader);
		return treeStore;
	}

	@Override
	public TreeStore<SiteDTO> getStore() {
		return treeStore;
	}

	@Override
	public PageId getPageId() {
		return SiteTreeGridPageState.SITE_TREE_VIEW;
	}

	@Override
    public boolean navigate(final PageState place) {
        final SiteTreeGridPageState treeGridPlace = (SiteTreeGridPageState) place;

        if (currentActivity.getId() != treeGridPlace.getActivityId()) {
            return false;
        }

        boolean reloadRequired = true;
        
        proxy.setPlace(treeGridPlace);
        
        if (reloadRequired) {
        	treeLoader.load();
        }
        return true;
    }

	@Override
	protected String getStateId() {
		return SiteTreeGridPageState.SITE_TREE_VIEW.toString();
	}

}
