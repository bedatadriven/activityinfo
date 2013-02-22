package org.activityinfo.client.page.search;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.callback.Got;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.PageStateSerializer;
import org.activityinfo.client.page.entry.place.DataEntryPlace;
import org.activityinfo.client.page.search.SearchView.SearchEvent;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.Search;
import org.activityinfo.shared.command.result.SearchResult;
import org.activityinfo.shared.command.result.SitePointList;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.content.FilterDescription;
import org.activityinfo.shared.report.content.PivotContent;
import org.activityinfo.shared.report.model.DimensionType;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * Presenter for the location search functionality in the LocationDialog. This
 */
public class SearchPresenter implements SearchView.SearchHandler, Page {
    public static final PageId SEARCH_PAGE_ID = new PageId("search");

    private final Dispatcher service;
    private final EventBus eventBus;
    private final SearchView view;
    private SchemaDTO schema;

    @Inject
    public SearchPresenter(Dispatcher service, EventBus eventBus,
        SearchView view) {
        this.service = service;
        this.eventBus = eventBus;
        this.view = view;

        view.addSearchHandler(this);

        service.execute(new GetSchema(), new Got<SchemaDTO>() {
            @Override
            public void got(SchemaDTO result) {
                SearchPresenter.this.schema = result;
            }
        });
    }

    public void init(SchemaDTO schema) {

    }

    @Override
    public void onSearch(SearchEvent searchEvent) {
        if (!Strings.isNullOrEmpty(searchEvent.getQuery())) {
            view.setSearchQuery(searchEvent.getQuery());
            view.getLoadingMonitor().beforeRequest();

            service.execute(new Search(searchEvent.getQuery()),
                view.getLoadingMonitor(), new AsyncCallback<SearchResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        // TODO handle failure
                        view.getLoadingMonitor().onServerError();
                    }

                    @Override
                    public void onSuccess(SearchResult result) {
                        view.setSearchResults(result.getPivotTabelData());
                        Filter filter = result.getPivotTabelData() == null ? null
                            : result.getPivotTabelData().getEffectiveFilter();
                        view.setFilter(createFilter(filter,
                            result.getPivotTabelData()));
                        view.getLoadingMonitor().onCompleted();
                        view.setSitePoints(SitePointList.fromSitesList(result
                            .getRecentAdditions()));
                        view.setSites(fromSitesList(result.getRecentAdditions()));
                    }

                    private Map<DimensionType, List<SearchResultEntity>> createFilter(
                        Filter effectiveFilter, PivotContent pivotContent) {

                        Map<DimensionType, List<SearchResultEntity>> newFilter = new HashMap<DimensionType, List<SearchResultEntity>>();

                        if (effectiveFilter != null) {
                            for (DimensionType type : effectiveFilter
                                .getRestrictedDimensions()) {
                                List<SearchResultEntity> entities = new ArrayList<SearchResultEntity>();
                                for (Integer entityId : effectiveFilter
                                    .getRestrictions(type)) {
                                    String name = getName(entityId, type,
                                        pivotContent);
                                    String link = GWT.getHostPageBaseURL()
                                        + "#search/" + type.toString() + ":"
                                        + name;

                                    SearchResultEntity entity = new SearchResultEntity(
                                        entityId, name, link, type);
                                    entities.add(entity);
                                }
                                newFilter.put(type, entities);
                            }
                        }

                        return newFilter;
                    }

                    private String getName(Integer entityId,
                        DimensionType type, PivotContent pivotTable) {
                        for (FilterDescription fd : pivotTable
                            .getFilterDescriptions()) {
                            if (fd.getDimensionType() == type) {
                                return fd.getLabels().get(entityId);
                            }
                        }
                        return "noName";
                    }
                });
        }
    }

    @Override
    public void shutdown() {
    }

    @Override
    public PageId getPageId() {
        return SEARCH_PAGE_ID;
    }

    @Override
    public Object getWidget() {
        return view.asWidget();
    }

    @Override
    public void requestToNavigateAway(PageState place,
        NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    @Override
    public boolean navigate(PageState place) {
        if (place instanceof SearchPageState) {
            SearchPageState search = (SearchPageState) place;
            onSearch(new SearchEvent(search.getSearchQuery()));
        }
        return true;
    }

    private List<RecentSiteModel> fromSitesList(List<SiteDTO> sites) {
        List<RecentSiteModel> result = new ArrayList<RecentSiteModel>();

        if (sites != null) {
            for (SiteDTO site : sites) {
                RecentSiteModel recent = null;
                if (schema != null) {
                    ActivityDTO activity = schema.getActivityById(site
                        .getActivityId());
                    if (activity != null) {
                        recent = new RecentSiteModel(site);
                        recent.setActivityName(activity.getName());
                        recent.setActivityLink(PageStateSerializer
                            .asLink(new DataEntryPlace(activity)));
                        recent
                            .setDatabaseName(activity.getDatabase().getName() == null ? "[Database]"
                                : activity.getDatabase().getName());
                    }
                }
                if (recent != null) {
                    result.add(recent);
                }
            }
        }

        return result;
    }

    public static final class RecentSiteModel extends BaseModelData {

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
            return (Integer) get("siteId");
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

        public LocalDate getDate1() {
            return get("date1");
        }

        public void setDate1(LocalDate date1) {
            set("date1", date1);
        }

        public LocalDate getDate2() {
            return get("date2");
        }

        public void setDate2(LocalDate date2) {
            set("date2", date2);
        }

        public String getComments() {
            return get("comments");
        }

        public void setComments(String comments) {
            set("comments", comments);
            set("hasComment", comments != null);
        }

        public boolean hasComment() {
            return (Boolean) get("hasComment");
        }
    }

    public void setQuery(String searchQuery) {
        onSearch(new SearchEvent(searchQuery));
    }
}
