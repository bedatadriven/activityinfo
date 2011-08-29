package org.sigmah.shared.command.handler;

import org.sigmah.server.dao.SiteDAO;
import org.sigmah.server.endpoint.gwtrpc.handler.GenerateElementHandler;
import org.sigmah.shared.command.GenerateElement;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.Search;
import org.sigmah.shared.command.result.SearchResult;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.report.content.PivotContent;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.search.AllSearcher;
import org.sigmah.shared.search.SearchException;

import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class SearchHandler implements CommandHandlerAsync<Search, SearchResult> {
	private GenerateElementHandler getPivotData;

    @Inject
    public SearchHandler(SiteDAO siteDAO, GenerateElementHandler genElHandler) {
		this.getPivotData = genElHandler;
    }

	@Override
	public void execute(final Search command, final ExecutionContext context,
			final AsyncCallback<SearchResult> callback) {
		
		final PivotTableReportElement pivotTable = createSearchPivotTableElement();
		
		createFilterFromQuery(command.getSearchQuery(), pivotTable, context.getTransaction(), new AsyncCallback<Filter>() {
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(Filter resultFilter) {
				//pivotTable.getFilter().setOr(true);
				PivotContent content = null;
				final SearchResult searchResult = new SearchResult();

				// Only request search results when restriciton are present, no need to query when no restrictions
				if (resultFilter.getRestrictedDimensions().size() > 0) {
					
					pivotTable.setFilter(resultFilter);
					content = getPivotDataUsingFilter(context, callback, pivotTable, content);
					if (content == null) {
						callback.onFailure(new SearchException("PivotContent is null. Probably an uncaught database or searcher error "));
					} else {
						content.setEffectiveFilter(resultFilter);
						searchResult.setPivotTabelData(content);
					}

					getRecentSitesUsingFilter(context, pivotTable, new AsyncCallback<SiteResult>() {
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

	private void getRecentSitesUsingFilter(ExecutionContext context,
			final PivotTableReportElement pivotTable,
			final AsyncCallback<SiteResult> callback) {
		
		GetSites getSites = new GetSites();
		getSites.setSortInfo(new SortInfo("DateEdited", SortDir.DESC));
		getSites.setLimit(10);
		//Filter siteFilter = pivotTable.getFilter()
		getSites.setFilter(pivotTable.getFilter());
		
		context.execute(getSites, new AsyncCallback<SiteResult>() {
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(SiteResult siteResult) {
				callback.onSuccess(siteResult);
			}
		});
	}

	private PivotContent getPivotDataUsingFilter(ExecutionContext context,
			final AsyncCallback<SearchResult> callback,
			final PivotTableReportElement pivotTable, PivotContent content) {
		
		GenerateElement<PivotContent> zmd = new GenerateElement<PivotContent>(pivotTable);
		try {
			content = (PivotContent) getPivotData.execute(zmd, context.getUser());
		} catch (CommandException e) {
			callback.onFailure(e);
		}
		return content;
	}
	
	private void createFilterFromQuery(String query, final PivotTableReportElement pivotTable, SqlTransaction tx, final AsyncCallback<Filter> callback) {
		AllSearcher allSearcher = new AllSearcher(tx);
		allSearcher.searchAll(query, new AsyncCallback<Filter>() {
			
			@Override
			public void onSuccess(Filter result) {
				callback.onSuccess(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}
}
