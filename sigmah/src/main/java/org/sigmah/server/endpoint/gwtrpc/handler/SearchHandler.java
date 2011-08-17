package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.report.content.PivotContent;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.search.AllSearcher;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
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
		
		final PivotTableReportElement pivotTable = new PivotTableReportElement();
		pivotTable.addRowDimension(new Dimension(DimensionType.Database));
		pivotTable.addRowDimension(new Dimension(DimensionType.Activity));
		//pivotTable.addRowDimension(new Dimension(DimensionType.Indicator));
		
		createFilterFromQuery(command.getSearchQuery(), pivotTable, callback);
	
		PivotContent content = null;
		final SearchResult result = new SearchResult();

		// Only request search results when restriciton are present, no need to query when no restrictions
		if (pivotTable.getFilter().getRestrictedDimensions().size() > 0) {
			content = getPivotDataUsingFilter(context, callback, pivotTable, content);
			getRecentSitesUsingFilter(context, callback, pivotTable, result); 
		}
		
		result.setPivotTabelData(content);
		
		callback.onSuccess(result);
	}

	private void getRecentSitesUsingFilter(CommandContext context,
			final AsyncCallback<SearchResult> callback,
			final PivotTableReportElement pivotTable, final SearchResult result) {
		
		GetSitesHandler getSitesHandler = new GetSitesHandler(db);
		GetSites getSites = new GetSites();
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

	private List<SiteDTO> mockSites() {
		List<SiteDTO> mockSites = new ArrayList<SiteDTO>();
		
		for (int i=0; i<5; i++) {
			SiteDTO site = new SiteDTO();
			site.setDate1(new Date(2000,1,1));
			site.setDate2(new Date(2000,2,2));
			site.setComments("KJHKJ KJH LKUHLIUHIUE u");
			site.setLocationAxe("Somewhere");
			site.setY(-4.2 +(0.1*i));
			site.setX(23.9 + (0.1 * i));
			site.setId(i);
			site.setLocationName("somewhere");
			mockSites.add(site);
		}
		
		return mockSites;
	}

}
