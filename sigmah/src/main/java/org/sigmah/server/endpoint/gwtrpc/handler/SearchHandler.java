package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.sigmah.server.dao.SiteDAO;
import org.sigmah.shared.command.GenerateElement;
import org.sigmah.shared.command.Search;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.SearchResult;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.SearchHitDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.report.content.PivotContent;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.search.AdminEntitySearcher;
import org.sigmah.shared.search.PartnerSearcher;
import org.sigmah.shared.search.Searcher;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class SearchHandler implements CommandHandler<Search> {
	private List<Searcher> searchers = new ArrayList<Searcher>();
	private List<Searcher> failedSearchers = new ArrayList<Searcher>();
	private SqlDatabase db;
	private String searchQuery;
	private Filter filter = new Filter();
	private GenerateElementHandler getPivotData;

    @Inject
    public SearchHandler(SqlDatabase db, SiteDAO siteDAO, GenerateElementHandler genElHandler) {
		this.db=db;
		this.getPivotData = genElHandler;
        
        //searcher = new AllSearcher(entityManager.)
    }
    
	@Override
	public CommandResult execute(Search cmd, User user) throws CommandException {
			
		final PivotTableReportElement pivotTable = new PivotTableReportElement();
		pivotTable.addRowDimension(new Dimension(DimensionType.Database));
		pivotTable.addRowDimension(new Dimension(DimensionType.Activity));
		//pivotTable.addRowDimension(new Dimension(DimensionType.Indicator));
	
		initSearchers();
		
		searchNext(cmd.getSearchQuery(), searchers.iterator(), new AsyncCallback<Filter>() {
			@Override
			public void onFailure(Throwable caught) {
				// Not going to happen
			}

			@Override
			public void onSuccess(Filter result) {
				pivotTable.setFilter(filter);
			}
		});
					
		GenerateElement<PivotContent> zmd = new GenerateElement<PivotContent>(pivotTable);
		PivotContent content = (PivotContent) getPivotData.execute(zmd, user);

		SearchResult result = new SearchResult();
		result.setPivotTabelData(content);
		return result;
	}
	
	private void initSearchers() {
		searchers.add(new PartnerSearcher(db));
		searchers.add(new AdminEntitySearcher(db));
	}

	
	private void searchNext(final String q, final Iterator<Searcher> it, final AsyncCallback<Filter> callback) {
		final Filter filter=new Filter();
		final Searcher<?> searcher = it.next();
		
		searcher.search(searchQuery, new AsyncCallback<List<Integer>>() {

			@Override
			public void onFailure(Throwable caught) {
				failedSearchers.add(searcher);
				
				continueOrYieldFilter(q, it, callback, filter);
			}

			@Override
			public void onSuccess(List<Integer> result) {
				for (Integer resultId : result) {
					filter.addRestriction(searcher.getDimensionType(), resultId);
				}
				
				continueOrYieldFilter(q, it, callback, filter);
			}
			
			private void continueOrYieldFilter(final String q,
					final Iterator<Searcher> it,
					final AsyncCallback<Filter> callback, final Filter filter) {
				
				if (it.hasNext()) {
					searchNext(q, it, callback);
				} else {
					callback.onSuccess(filter);
				}
			}
		});
	}
	
	public void go() {
		List<SearchHitDTO> hits = new ArrayList<SearchHitDTO>();
		List<SearchHitDTO> latestAdditions = new ArrayList<SearchHitDTO>();
		
//		SearchHitDTO hit1 = new SearchHitDTO();
//		PartnerDTO partner1 = new PartnerDTO();
//		partner1.setName("Help inc.");
//		partner1.setId(1);
//		hit1.setPosition(1);
//		hit1.setDto(partner1);
//		hits.add(hit1);
		
		SiteDTO site = new SiteDTO();
		site.setId(1);
		site.setDate1(new Date());
		site.setDate2(new Date());
		site.setComments("WoeiSite");
		//site.setPartner(partner1);
		hits.add(new SearchHitDTO(site, 1));
		
//		SearchHitDTO hit2 = new SearchHitDTO();
//		PartnerDTO partner2 = new PartnerDTO();
//		partner2.setName("Help org.");
//		partner2.setId(2);
//		hit2.setPosition(2);
//		hit2.setDto(partner2);
//		latestAdditions.add(hit2);
		
		SiteDTO site2 = new SiteDTO();
		site2.setId(2);
		site2.setDate1(new Date());
		site2.setDate2(new Date());
		site2.setComments("WoeiSite2");
		//site2.setPartner(partner1);
		latestAdditions.add(new SearchHitDTO(site2, 2));
		
		UserDatabaseDTO db = new UserDatabaseDTO(1, "yeydb");
		hits.add(new SearchHitDTO(db, 2));
		
//		SearchResult result = new SearchResult(hits, latestAdditions);
//		
//		return result;
	}

}
