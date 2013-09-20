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

import java.util.List;
import java.util.Map;

import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.search.SearchPresenter.RecentSiteModel;
import org.activityinfo.shared.command.handler.search.QueryChecker;
import org.activityinfo.shared.command.handler.search.QueryChecker.QueryFail;
import org.activityinfo.shared.command.result.SearchResult;
import org.activityinfo.shared.command.result.SitePointList;
import org.activityinfo.shared.dto.SearchHitDTO;
import org.activityinfo.shared.report.content.PivotContent;
import org.activityinfo.shared.report.content.PivotTableData.Axis;
import org.activityinfo.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.TextBox;

public class SearchResultsPage extends ContentPanel implements SearchView {
    private VerticalPanel searchResultsPanel;
    private LayoutContainer containerFilterAndResult;
    private PivotContent pivotContent;
    // private SearchFilterView filterView;
    private RecentSitesView recentSitesView;

    private TextBox textboxSearch;
    private AsyncMonitor loadingMonitor;
    private SimpleEventBus eventBus = new SimpleEventBus();
    private String searchQuery;

    public SearchResultsPage() {
        initializeComponent();

        createRecentSitesView();

        createCompleteResultPanel();
        // createFilterView();
        createSearchResultsPanel();

        createSearchBox();
    }

    private void initializeComponent() {
        setHeaderVisible(false);
        setLayout(new BorderLayout());

        SearchResources.INSTANCE.searchStyles().ensureInjected();
        loadingMonitor = new MaskingAsyncMonitor(this,
            I18N.CONSTANTS.busySearching());
    }

    private void createRecentSitesView() {
        recentSitesView = new RecentSitesView();

        BorderLayoutData bld = new BorderLayoutData(LayoutRegion.EAST);
        bld.setSplit(true);
        bld.setCollapsible(true);
        bld.setMinSize(300);
        bld.setSize(0.4F);

        add(recentSitesView, bld);
    }

    private void createSearchResultsPanel() {
        searchResultsPanel = new VerticalPanel();
        searchResultsPanel.setScrollMode(Scroll.AUTO);
        containerFilterAndResult.add(searchResultsPanel);
    }

    private void createCompleteResultPanel() {
        containerFilterAndResult = new LayoutContainer();
        RowLayout layout = new RowLayout();
        layout.setOrientation(Orientation.VERTICAL);
        containerFilterAndResult.setLayout(layout);
        containerFilterAndResult.setScrollMode(Scroll.AUTOY);

        BorderLayoutData bld = new BorderLayoutData(LayoutRegion.CENTER);
        bld.setSplit(true);
        bld.setSize(0.5F);

        add(containerFilterAndResult, bld);
    }

    // private void createFilterView() {
    // filterView = new SearchFilterView();
    // containerFilterAndResult.add(filterView);
    // filterView.addDimensionAddedHandler(new DimensionAddedEventHandler() {
    // @Override
    // public void onDimensionAdded(DimensionAddedEvent event) {
    // addEntityToSearchBox(event.getAddedEntity());
    // }
    // });
    // }

    private void showError(String error) {
        containerFilterAndResult.el().mask(error);
        recentSitesView.el().mask();
    }

    private void showError(List<QueryFail> fails) {
        StringBuilder sb = new StringBuilder();
        for (QueryFail fail : fails) {
            sb.append(fail.fail());
            sb.append("\r\n");
        }
        showError(sb.toString());
    }

    private void clearErrorsIfShowing() {
        containerFilterAndResult.el().unmask();
        recentSitesView.el().unmask();
    }

    private void addEntityToSearchBox(SearchResultEntity addedEntity) {
        textboxSearch.setText(textboxSearch.getText() + " "
            + createEntityText(addedEntity));
    }

    private String createEntityText(SearchResultEntity addedEntity) {
        return new StringBuilder()
            .append(addedEntity.getDimension().toString())
            .append(":")
            .append(addedEntity.getName())
            .toString();
    }

    private void createSearchBox() {
        textboxSearch = new TextBox();
        textboxSearch.setSize("2em", "2em");
        textboxSearch.setStylePrimaryName("searchBox");
        textboxSearch.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    QueryChecker checker = new QueryChecker();

                    if (checker.checkQuery(textboxSearch.getText())) {
                        eventBus.fireEvent(new SearchEvent(textboxSearch
                            .getText()));
                    } else {
                        showError(checker.getFails());
                    }
                }
            }
        });

        BorderLayoutData bld = new BorderLayoutData(LayoutRegion.NORTH);
        bld.setSize(40);
        bld.setMargins(new Margins(16));

        add(textboxSearch, bld);
    }

    @Override
    public void setParent(SearchResult parent) {
        // searchResult = parent;
    }

    @Override
    public void setItems(List<SearchHitDTO> items) {
    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub

    }

    @Override
    public AsyncMonitor getLoadingMonitor() {
        return loadingMonitor;
    }

    @Override
    public void setValue(SearchHitDTO value) {
    }

    @Override
    public SearchHitDTO getValue() {
        return null;
    }

    @Override
    public com.google.gwt.event.shared.HandlerRegistration addSearchHandler(
        org.activityinfo.client.page.search.SearchView.SearchHandler handler) {
        return eventBus.addHandler(SearchEvent.TYPE, handler);
    }

    @Override
    public void setSearchResults(PivotContent pivotTabelData) {
        this.pivotContent = pivotTabelData;

        showSearchResults();
    }

    private void showSearchResults() {
        searchResultsPanel.removeAll();
        clearErrorsIfShowing();

        int activities = 0;
        int databases = 0;
        int indicators = 0;

        LabelField labelResults = new LabelField();
        searchResultsPanel.add(labelResults);

        if (pivotContent != null) {
            VerticalPanel panelSpacer = new VerticalPanel();
            panelSpacer.setHeight(16);
            searchResultsPanel.add(panelSpacer);
            searchResultsPanel.setStylePrimaryName("searchResults");

            for (Axis axis : pivotContent.getData().getRootRow().getChildren()) {
                SearchResultItem itemWidget = new SearchResultItem();
                itemWidget.setDabaseName(axis.getLabel());
                itemWidget.setChilds(axis.getChildList());

                searchResultsPanel.add(itemWidget);
                databases++;
                activities += itemWidget.getActivityCount();
                indicators += itemWidget.getIndicatorCount();
            }
        }

        labelResults.setText(I18N.MESSAGES.searchResultsFound(
            searchQuery,
            Integer.toString(databases),
            Integer.toString(activities),
            Integer.toString(indicators)));

        layout();
    }

    @Override
    public void setSearchQuery(String query) {
        this.searchQuery = query;

        textboxSearch.setText(query);
    }

    @Override
    public void setFilter(
        Map<DimensionType, List<SearchResultEntity>> affectedEntities) {
        // filterView.setFilter(affectedEntities);
    }

    @Override
    public void setSites(List<RecentSiteModel> sites) {
        recentSitesView.setSites(sites);
    }

    @Override
    public void setSitePoints(SitePointList sitePoints) {
        recentSitesView.setSitePoins(sitePoints);
    }

}
