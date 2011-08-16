package org.sigmah.server.endpoint.gwtrpc.handler;

import org.sigmah.server.dao.SiteDAO;
import org.sigmah.shared.command.GenerateElement;
import org.sigmah.shared.command.Search;
import org.sigmah.shared.command.handler.CommandContext;
import org.sigmah.shared.command.handler.CommandHandlerAsync;
import org.sigmah.shared.command.result.SearchResult;
import org.sigmah.shared.dao.Filter;
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
			AsyncCallback<SearchResult> callback) {
		Filter filter;
		
		final PivotTableReportElement pivotTable = new PivotTableReportElement();
		pivotTable.addRowDimension(new Dimension(DimensionType.Database));
		pivotTable.addRowDimension(new Dimension(DimensionType.Activity));
		//pivotTable.addRowDimension(new Dimension(DimensionType.Indicator));
		
		AllSearcher allSearcher = new AllSearcher(db);
		allSearcher.searchAll(command.getSearchQuery(), new AsyncCallback<Filter>() {
			
			@Override
			public void onSuccess(Filter result) {
				pivotTable.setFilter(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
					
//		if (result.getRestrictedDimensions().isEmpty()) {
//		}
		GenerateElement<PivotContent> zmd = new GenerateElement<PivotContent>(pivotTable);
		PivotContent content = null;
		try {
			content = (PivotContent) getPivotData.execute(zmd, context.getUser());
		} catch (CommandException e) {
			callback.onFailure(e);
		}

		SearchResult result = new SearchResult();
		result.setPivotTabelData(content);
		
		callback.onSuccess(result);
	}

}
