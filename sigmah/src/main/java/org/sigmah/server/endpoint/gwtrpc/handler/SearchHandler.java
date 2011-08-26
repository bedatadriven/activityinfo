package org.sigmah.server.endpoint.gwtrpc.handler;

import org.sigmah.server.dao.SiteDAO;
import org.sigmah.shared.command.GenerateElement;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.Search;
import org.sigmah.shared.command.handler.CommandContext;
import org.sigmah.shared.command.handler.CommandHandlerAsync;
import org.sigmah.shared.command.handler.GetSitesHandler;
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

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class SearchHandler implements CommandHandlerAsync<Search, SearchResult> {
	private SqlDatabase db;
	private GenerateElementHandler getPivotData;

    @Inject
    public SearchHandler(SqlDatabase db, SiteDAO siteDAO, GenerateElementHandler genElHandler) {
		this.db=db;
		this.getPivotData = genElHandler;
    }

	@Override
	public void execute(final Search command, CommandContext context,
			final AsyncCallback<SearchResult> callback) {
		
		final PivotTableReportElement pivotTable = createSearchPivotTableElement();
		
		createFilterFromQuery(command.getSearchQuery(), pivotTable, callback);
		//pivotTable.getFilter().setOr(true);
		PivotContent content = null;
		final SearchResult result = new SearchResult();

		// Only request search results when restriciton are present, no need to query when no restrictions
		if (pivotTable.getFilter().getRestrictedDimensions().size() > 0) {
			content = getPivotDataUsingFilter(context, callback, pivotTable, content);
			getRecentSitesUsingFilter(context, callback, pivotTable, result); 
		}
	
		if (content == null) {
			callback.onFailure(new SearchException("PivotContent is null. Probably an uncaught database or searcher error "));
		} else {
			result.setPivotTabelData(content);
		}
		
		callback.onSuccess(result);
	}

	private PivotTableReportElement createSearchPivotTableElement() {
		final PivotTableReportElement pivotTable = new PivotTableReportElement();
		
		pivotTable.addRowDimension(new Dimension(DimensionType.Database));
		pivotTable.addRowDimension(new Dimension(DimensionType.Activity));
		pivotTable.addRowDimension(new Dimension(DimensionType.Indicator));
		
		return pivotTable;
	}

	private void getRecentSitesUsingFilter(CommandContext context,
			final AsyncCallback<SearchResult> callback,
			final PivotTableReportElement pivotTable, final SearchResult result) {
		
		GetSitesHandler getSitesHandler = new GetSitesHandler(db);
		GetSites getSites = new GetSites();
		getSites.setSortInfo(new SortInfo("DateEdited", SortDir.DESC));
		getSites.setLimit(10);
		//Filter siteFilter = pivotTable.getFilter()
		getSites.setFilter(pivotTable.getFilter());
		
		getSitesHandler.execute(getSites, context, new AsyncCallback<SiteResult>() {
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(SiteResult siteResult) {
				result.setRecentAdditions(siteResult.getData());
			}
		});
	}

	private PivotContent getPivotDataUsingFilter(CommandContext context,
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
	
	private void createFilterFromQuery(String query, final PivotTableReportElement pivotTable, final AsyncCallback<SearchResult> callback) {
		AllSearcher allSearcher = new AllSearcher(db);
		allSearcher.searchAll(query, new AsyncCallback<Filter>() {
			
			@Override
			public void onSuccess(Filter result) {
				pivotTable.setFilter(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}
}
