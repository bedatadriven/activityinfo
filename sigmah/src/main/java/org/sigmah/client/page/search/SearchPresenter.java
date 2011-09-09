package org.sigmah.client.page.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.callback.Got;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.search.SearchView.SearchEvent;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.Search;
import org.sigmah.shared.command.result.SearchResult;
import org.sigmah.shared.command.result.SitePointList;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.report.content.FilterDescription;
import org.sigmah.shared.report.content.PivotContent;
import org.sigmah.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class SearchPresenter implements SearchView.SearchHandler, Page {
	public static final PageId Search = new PageId("search");
	protected final Dispatcher service;
	protected final EventBus eventBus;
	protected final SearchView view;
	private SchemaDTO schema;
	
	@Inject
	public SearchPresenter(Dispatcher service, EventBus eventBus, SearchView view) {
		this.service=service;
		this.eventBus=eventBus;
		this.view=view;
		
		view.addSearchHandler(this);
		
        service.execute(new GetSchema(), null, new Got<SchemaDTO>() {
			@Override
			public void got(SchemaDTO result) {
				SearchPresenter.this.schema=result;
			}
        });
	}
	
	public void init(SchemaDTO schema) {
		
	}
	
	@Override
	public void onSearch(SearchEvent searchEvent) {
		view.setSearchQuery(searchEvent.getQuery());
		view.getLoadingMonitor().beforeRequest();
		
		service.execute(new Search(searchEvent.getQuery()), view.getLoadingMonitor(), new AsyncCallback<SearchResult>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO handle failure
				view.getLoadingMonitor().onServerError();
			}

			@Override
			public void onSuccess(SearchResult result) {
				view.setSearchResults(result.getPivotTabelData());
				view.setFilter(createFilter(result.getPivotTabelData().getEffectiveFilter(), result.getPivotTabelData()));
				view.getLoadingMonitor().onCompleted();
				view.setSitePoints(SitePointList.fromSitesList(result.getRecentAdditions()));
				view.setSites(fromSitesList(result.getRecentAdditions()));
			}

			private Map<DimensionType, List<SearchResultEntity>> createFilter(
					Filter effectiveFilter, PivotContent pivotContent) {
				
				Map<DimensionType, List<SearchResultEntity>> newFilter = new HashMap<DimensionType, List<SearchResultEntity>>();
				
				for (DimensionType type : effectiveFilter.getRestrictedDimensions()) {
					
					List<SearchResultEntity> entities = new ArrayList<SearchResultEntity>();
					for (Integer entityId : effectiveFilter.getRestrictions(type)) {
						String name = getName(entityId, type, pivotContent);
						String link = GWT.getHostPageBaseURL() + "#search/" + type.toString() + ":" + name;
						
						SearchResultEntity entity = new SearchResultEntity(entityId, name, link, type);
						entities.add(entity);
					}
					newFilter.put(type, entities);
				}
				
				return newFilter;
			}

			private String getName(Integer entityId, DimensionType type, PivotContent pivotTable) {
				for (FilterDescription fd : pivotTable.getFilterDescriptions()) {
					if (fd.getDimensionType() == type) {
						return fd.getLabels().get(entityId);
					}
				}
				return "noName";
			}
		});
	}

	@Override
	public void shutdown() {
	}

	@Override
	public PageId getPageId() {
		return Search;
	}

	@Override
	public Object getWidget() {
		return view.asWidget();
	}

	@Override
	public void requestToNavigateAway(PageState place, NavigationCallback callback) {
		callback.onDecided(true);
	}

	@Override
	public String beforeWindowCloses() {
		return null;
	}

	@Override
	public boolean navigate(PageState place) {
		if (place instanceof SearchPageState) {
			SearchPageState search = (SearchPageState)place;
			onSearch(new SearchEvent(search.getSearchQuery()));
		}
		return true;
	}
	
	private List<RecentSiteModel> fromSitesList(List<SiteDTO> sites) {
		List<RecentSiteModel> result = new ArrayList<RecentSiteModel>();

		for (SiteDTO site : sites) {
			RecentSiteModel recent = new RecentSiteModel(site);
			if (schema != null) {
				ActivityDTO activity = schema.getActivityById(site.getActivityId());
				recent.setActivityName(activity.getName());
				recent.setActivityLink(GWT.getHostPageBaseURL() + "#site-grid/" + activity.getId());
				recent.setDatabaseName(activity.getDatabase().getName() == null ? "[Database]" : activity.getDatabase().getName());
			}
			result.add(recent);
		}
		
		return result;
	}
	
	public class RecentSiteModel extends BaseModelData {
		
		public RecentSiteModel(SiteDTO site) {
			set("hasComment", false);
			setSiteId(site.getId());
			setLocationName(site.getLocationName());
			setDate1(site.getDate1());
			setDate2(site.getDate2());
			setComments(site.getComments());
		}
		
		public String getActivityName() {
			return get("activityName");
		}
		public void setActivityName(String activityName) {
			set("activityName", activityName);
		}
		public String getActivityLink() {
			return get("activityLink");
		}
		public void setActivityLink(String activityLink) {
			set("activityLink", activityLink);
		}
		public String getDatabaseName() {
			return get("activityName");
		}
		public void setDatabaseName(String databaseName) {
			set("databaseName", databaseName);
		}
		public int getSiteId() {
			return (Integer)get("siteId");
		}
		public void setSiteId(int siteId) {
			set("siteId", siteId);
		}
		public String getLocationName() {
			return get("locationName");
		}
		public void setLocationName(String locationName) {
			set("locationName", locationName);
		}
		public Date getDate1() {
			return get("date1");
		}
		public void setDate1(Date date1) {
			set("date1", date1);
		}
		public Date getDate2() {
			return get("date2");
		}
		public void setDate2(Date date2) {
			set("date2", date2);
		}
		public String getComments() {
			return get("comments");
		}
		public void setComments(String comments) {
			set("comments", comments);
			set("hasComment", comments!= null);
		}
		public boolean hasComment() {
			return (Boolean)get("hasComment");
		}
	}

	public void setQuery(String searchQuery) {
		onSearch(new SearchEvent(searchQuery));
	}
}
