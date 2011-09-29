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

public class SiteTreeEditor extends AbstractSiteEditor implements GridPresenter.SiteGridPresenter
{
	public interface View extends AbstractSiteEditor.View {
        public void init(SiteTreeEditor presenter, ActivityDTO activity, TreeStore<SiteDTO> store);
	}

	private SiteTreeLoader treeLoader;
	private TreeStore<SiteDTO> treeStore;
	
	public SiteTreeEditor(EventBus eventBus, Dispatcher service, StateProvider stateMgr, SiteTreeEditor.View view) {
		super(eventBus, service, stateMgr, view);
		
		
	}

	@Override
	public PageId getPageId() {
		return null;
	}

	@Override
	public boolean navigate(PageState place) {
		return false;
	}

	@Override
	protected Loader createLoader() {
		treeLoader = new SiteTreeLoader(service);
		return treeLoader;
	}

	@Override
	protected TreeStore<SiteDTO> createStore() {
		treeStore = new TreeStore<SiteDTO>(treeLoader);
		return treeStore;
	}

	@Override
	protected String getStateId() {
		return null;
	}

	@Override
	public TreeStore<SiteDTO> getStore() {
		return treeStore;
	}

}
