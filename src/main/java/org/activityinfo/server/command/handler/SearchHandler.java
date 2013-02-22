package org.activityinfo.server.command.handler;

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

import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.Search;
import org.activityinfo.shared.command.handler.CommandHandlerAsync;
import org.activityinfo.shared.command.handler.ExecutionContext;
import org.activityinfo.shared.command.handler.search.AllSearcher;
import org.activityinfo.shared.command.handler.search.QueryParser;
import org.activityinfo.shared.command.result.SearchResult;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.report.content.PivotContent;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.PivotTableReportElement;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Handles a search locally or remotely. Searchers being used utilize SQL LIKE
 * queries, beware of any OR left joins.
 * 
 * 1. Get a filter from the query 2. Get list of DB/Activities/Indicators based
 * on filter 3. Get list of recent sites 4. Return result
 */
public class SearchHandler implements CommandHandlerAsync<Search, SearchResult> {

    public class SearchParserException extends Throwable {
        private String failReason;

        public SearchParserException(String failReason) {
            super();
            this.failReason = failReason;
        }

        public String getFailReason() {
            return failReason;
        }
    }

    @Override
    public void execute(final Search command, final ExecutionContext context,
        final AsyncCallback<SearchResult> callback) {
        QueryParser parser = new QueryParser();
        parser.parse(command.getSearchQuery().trim());
        if (parser.hasFailed()) {
            callback
                .onFailure(new SearchParserException(parser.getFailReason()));
            // FIXME temporary removed the dimension search
            // } else if (parser.hasDimensions()) { // assume more refined
            // search using "location:kivu"-like queries
            // searchDimensions(parser, context, callback);
        } else { // assume first time search
            searchAll(parser.getSimpleSearchTerms(), context, callback);
        }
    }

    private void checkParserResult(QueryParser parser,
        AsyncCallback<SearchResult> callback) {

    }

    /**
     * Assumes the user typed a generic search term without specifying a
     * dimension. Search using all possible searchers, and return a list of
     * matched dimensions
     */
    private void searchAll(final List<String> q,
        final ExecutionContext context,
        final AsyncCallback<SearchResult> callback) {

        AllSearcher allSearcher = new AllSearcher(context.getTransaction());
        allSearcher.searchAll(q, new AsyncCallback<Filter>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(final Filter resultFilter) {
                processFilter(context, callback, resultFilter);
            }

        });
    }

    private void searchDimensions(QueryParser parser,
        final ExecutionContext context,
        final AsyncCallback<SearchResult> callback) {
        AllSearcher allSearcher = new AllSearcher(context.getTransaction());
        allSearcher.searchDimensions(parser.getUniqueDimensions(),
            new AsyncCallback<Filter>() {
                @Override
                public void onSuccess(Filter result) {
                    processFilter(context, callback, result);
                }

                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }
            });
    }

    private PivotTableReportElement createSearchPivotTableElement() {
        final PivotTableReportElement pivotTable = new PivotTableReportElement();

        pivotTable.addRowDimension(new Dimension(DimensionType.Database));
        pivotTable.addRowDimension(new Dimension(DimensionType.Activity));
        pivotTable.addRowDimension(new Dimension(DimensionType.Indicator));

        return pivotTable;
    }

    private void processFilter(final ExecutionContext context,
        final AsyncCallback<SearchResult> callback,
        final Filter resultFilter) {

        final SearchResult searchResult = new SearchResult();

        if (resultFilter.getRestrictedDimensions().size() > 0) {

            // pivot data query
            final PivotTableReportElement pivotTable = createSearchPivotTableElement();
            pivotTable.setFilter(resultFilter);
            GenerateElement<PivotContent> zmd = new GenerateElement<PivotContent>(
                pivotTable);
            context.execute(zmd, new AsyncCallback<PivotContent>() {
                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(PivotContent content) {
                    content.setEffectiveFilter(resultFilter);
                    searchResult.setPivotTabelData(content);

                    // recent sites query
                    GetSites getSites = createGetSitesCommand(resultFilter);
                    context.execute(getSites, new AsyncCallback<SiteResult>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            callback.onFailure(caught);
                        }

                        @Override
                        public void onSuccess(SiteResult resultSites) {
                            searchResult.setRecentAdditions(resultSites
                                .getData());
                            callback.onSuccess(searchResult);
                        }
                    });

                }
            });
        } else {
            // Return empty searchresult when no filtered entities found
            callback.onSuccess(searchResult);
        }
    }

    private GetSites createGetSitesCommand(final Filter resultFilter) {
        GetSites getSites = new GetSites();
        getSites.setSortInfo(new SortInfo("DateEdited", SortDir.DESC));
        getSites.setLimit(10);
        getSites.setFilter(resultFilter);
        return getSites;
    }
}
