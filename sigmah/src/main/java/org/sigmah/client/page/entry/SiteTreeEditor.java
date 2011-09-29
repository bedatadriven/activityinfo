package org.sigmah.client.page.entry;

import java.util.Arrays;
import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;
import org.sigmah.client.page.common.grid.GridPresenter;
import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.store.TreeStore;

public class SiteTreeEditor extends AbstractSiteEditor implements GridPresenter.SiteGridPresenter{
	public static class SiteTreeGridPageState implements PageState{
		public static PageId SITE_TREE_VIEW = new PageId("site-treeview");
		public enum TreeType {
			GEO,
			TIME
		}
		// Default on showing timed treeview
		private TreeType treeType = TreeType.TIME;
		private int activityId;
		
		// Time 
		private int month;
		private int year;
		
		// Geo
		private int adminEntityId;
		
	    public TreeType getTreeType() {
			return treeType;
		}
		public int getActivityId() {
			return activityId;
		}
		public int getMonth() {
			return month;
		}
		public int getYear() {
			return year;
		}
		public int getAdminEntityId() {
			return adminEntityId;
		}
		
		public void setActivityId(int activityId) {
			this.activityId = activityId;
		}

		public static class Parser implements PageStateParser {
	        @Override
	        public PageState parse(String token) {
	        	SiteTreeGridPageState place = new SiteTreeGridPageState();

	            for(String t : token.split("/")) {
	                if (t.startsWith("time")) {
		            	place.treeType = TreeType.TIME;
		            } else if (t.startsWith("geo")) {
		            	place.treeType = TreeType.GEO;
		            } else if (t.startsWith("month")) {
		            	place.month=Integer.parseInt(t.substring("month".length()));
		            } else if (t.startsWith("year")) {
		            	place.year = Integer.parseInt(t.substring("year".length()));
		            } else if (t.startsWith("adminEntity")) {
		            	place.adminEntityId = Integer.parseInt(t.substring("adminEntity".length()));
		            } else {
	                    place.activityId = Integer.parseInt(t);
		            }
	            }
	            
	            return place;
	        }

	    }
		@Override
		public String serializeAsHistoryToken() {
			StringBuilder sb = new StringBuilder();
			sb.append(activityId);
			sb.append("/");
			if (treeType == TreeType.GEO) {
				sb.append("geo");
				sb.append("/");
				sb.append(adminEntityId);
			} else if (treeType == TreeType.TIME) {
				sb.append("time");
				sb.append("/");
				sb.append("year=");
				sb.append(year);
				sb.append("/");
				sb.append("month=");
				sb.append(month);
			}
			return sb.toString();
		}
		@Override
		public PageId getPageId() {
			return SITE_TREE_VIEW;
		}
		@Override
		public List<PageId> getEnclosingFrames() {
			return Arrays.asList(SITE_TREE_VIEW);
		}
	}

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
