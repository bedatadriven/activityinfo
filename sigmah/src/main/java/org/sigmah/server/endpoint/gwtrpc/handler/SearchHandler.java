package org.sigmah.server.endpoint.gwtrpc.handler;

import org.sigmah.shared.command.GenerateElement;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.Search;
import org.sigmah.shared.command.handler.CommandHandlerAsync;
import org.sigmah.shared.command.handler.ExecutionContext;
import org.sigmah.shared.command.result.SearchResult;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.report.content.PivotContent;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.search.AllSearcher;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/*
 * Handles a search locally or remotely. Searchers being used utilize SQL LIKE queries, beware
 * of any OR left joins.
 * 
 * 1. Get a filter from the query
 * 2. Get list of DB/Activities/Indicators based on filter
 * 3. Get list of recent sites
 * 4. Return result
 */
public class SearchHandler implements CommandHandlerAsync<Search, SearchResult> {
	private GenerateElementHandler genElHandler;

	@Inject 
    public SearchHandler(GenerateElementHandler genElHandler) {
		this.genElHandler = genElHandler;
    }

	@Override
	public void execute(final Search command, final ExecutionContext context,
			final AsyncCallback<SearchResult> callback) {
		
		AllSearcher allSearcher = new AllSearcher(context.getTransaction());
		allSearcher.searchAll(command.getSearchQuery(), new AsyncCallback<Filter>() {
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
			@Override
			public void onSuccess(final Filter resultFilter) {
				final PivotTableReportElement pivotTable = createSearchPivotTableElement();
				final SearchResult searchResult = new SearchResult();
				if (resultFilter.getRestrictedDimensions().size() > 0) {
					pivotTable.setFilter(resultFilter);
					GenerateElement<PivotContent> zmd = new GenerateElement<PivotContent>(pivotTable);
					PivotContent content = null;
					try {
						content = (PivotContent) genElHandler.execute(zmd, context.getUser());
					} catch (CommandException e) {
						callback.onFailure(e);
					}
					content.setEffectiveFilter(resultFilter);
					searchResult.setPivotTabelData(content);
					GetSites getSites = new GetSites();
					getSites.setSortInfo(new SortInfo("DateEdited", SortDir.DESC));
					getSites.setLimit(10);
					getSites.setFilter(resultFilter);
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
		});
	}

	private PivotTableReportElement createSearchPivotTableElement() {
		final PivotTableReportElement pivotTable = new PivotTableReportElement();
		
		pivotTable.addRowDimension(new Dimension(DimensionType.Database));
		pivotTable.addRowDimension(new Dimension(DimensionType.Activity));
		pivotTable.addRowDimension(new Dimension(DimensionType.Indicator));
		
		return pivotTable;
	}
}
