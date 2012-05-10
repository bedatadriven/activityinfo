package org.sigmah.server.command.handler;

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
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.content.PivotContent;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.PivotTableReportElement;
import org.sigmah.server.database.hibernate.entity.User;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * Handles a search locally or remotely. Searchers being used utilize SQL LIKE queries, beware
 * of any OR left joins.
 * 
 * 1. Get a filter from the query
 * 2. Get list of DB/Activities/Indicators based on filter
 * 3. Get list of recent sites
 * 4. Return result
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

	private GenerateElementHandler genElHandler;

	@Inject 
    public SearchHandler(GenerateElementHandler genElHandler) {
		this.genElHandler = genElHandler;
    }

	@Override
	public void execute(final Search command, final ExecutionContext context, final AsyncCallback<SearchResult> callback) {
		QueryParser parser = new QueryParser();
		parser.parse(command.getSearchQuery().trim());
		if (parser.hasFailed()) {
			callback.onFailure(new SearchParserException(parser.getFailReason()));
		} else if (parser.hasDimensions()) { // assume more refined search using "location:kivu"-like queries 
			searchDimensions(parser, context, callback);
		} else { // assume first time search
			searchAll(parser.getSimpleSearchTerms(), context, callback);
		}
	}

	private void checkParserResult(QueryParser parser, AsyncCallback<SearchResult> callback) {

	}

	/** Assumes the user typed a generic search term without specifying a dimension. Search
	 * using all possible searchers, and return a list of matched dimensions */
	private void searchAll(final List<String> q, final ExecutionContext context,
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

	private void searchDimensions(QueryParser parser, final ExecutionContext context, final AsyncCallback<SearchResult> callback) {
		AllSearcher allSearcher = new AllSearcher(context.getTransaction());
		allSearcher.searchDimensions(parser.getUniqueDimensions(), new AsyncCallback<Filter>() {
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
		
		final PivotTableReportElement pivotTable = createSearchPivotTableElement();
		final SearchResult searchResult = new SearchResult();
		
		if (resultFilter.getRestrictedDimensions().size() > 0) {
			pivotTable.setFilter(resultFilter);
			GenerateElement<PivotContent> zmd = new GenerateElement<PivotContent>(pivotTable);
			PivotContent content = null;
			try {
				content = (PivotContent) genElHandler.execute(zmd, new User(context.getUser()));
			} catch (CommandException e) {
				callback.onFailure(e);
			}
			content.setEffectiveFilter(resultFilter);
			searchResult.setPivotTabelData(content);
			GetSites getSites = createGetSitesCommand(resultFilter);
			context.execute(getSites, new AsyncCallback<SiteResult>() {
				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}
				@Override
				public void onSuccess(SiteResult resultSites) {
					searchResult.setRecentAdditions(resultSites.getData());
					callback.onSuccess(searchResult);
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
