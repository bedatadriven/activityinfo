package org.sigmah.client.offline.command.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sigmah.client.offline.dao.SiteTableLocalDAO;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.handler.GetSitesHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.domain.Site;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.DTOMapper;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.exception.CommandException;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.inject.Inject;


public class GetSitesHandlerLocal implements GetSitesHandler<GetSites> {

    private final SiteTableLocalDAO siteTableDAO;
    //private final IndicatorDAO indicatorDAO;
    private final DTOMapper mapper;
    //private final FrenchFilterParser parser;

    //IndicatorDAO indicatorDAO
    
    @Inject
    public GetSitesHandlerLocal(SiteTableLocalDAO siteTableDAO,  DTOMapper mapper) {
        this.siteTableDAO = siteTableDAO;
        this.mapper = mapper;
       // this.parser = parser;
       // this.indicatorDAO = indicatorDAO;
       
    }

    @Override
    public CommandResult execute(GetSites cmd, User user) throws CommandException {

    	Map <String, Object> criteria = new HashMap<String, Object> ();
    	criteria.put("site.siteId", cmd.getSiteId());
    	criteria.put("site.activityId", cmd.getActivityId());
    	criteria.put("site.databaseId", cmd.getDatabaseId());
    	//	criteria.put("site.assesment", cmd.isAssessmentsOnly());
    	
    	// TODO Filters
    	//criteria.put("filter", cmd.getFilter());
    	//criteria.put("pivotFilter",cmd.getPivotFilter());
     
        List<SortInfo> order = new ArrayList<SortInfo>();

        if (cmd.getSortInfo().getSortDir() != SortDir.NONE) {

            String field = cmd.getSortInfo().getSortField();
          
            if (field.equals("date1")) {
                order.add(cmd.getSortInfo());
            } else if (field.equals("date2")) {
                order.add(cmd.getSortInfo());
            } else if (field.equals("locationName")) {
                order.add(cmd.getSortInfo());
            } else if (field.equals("partner")) {
                order.add(cmd.getSortInfo());
            } else if (field.equals("locationAxe")) {
                order.add(cmd.getSortInfo());
          //  }
            //else if (field.startsWith(IndicatorDTO.PROPERTY_PREFIX)) {
            	// TODO
            //	Indicator indicator = indicatorDAO.findById(
            //            IndicatorDTO.indicatorIdForPropertyName(field));

            //    order.add(new SiteIndicatorOrder(indicator,
            //            cmd.getSortInfo().getSortDir() == SortDir.ASC));

        	} else if (field.startsWith("a")) {
                int levelId = AdminLevelDTO.levelIdForPropertyName(field);

//                order.add(new SiteAdminOrder(levelId,
                //       cmd.getSortInfo().getSortDir() == SortDir.ASC));
                //TODO
            }

        }

        /*
         *  If we need to seek to the page that contains a given id,
         *  we need to do that here.
         */

        int offset;
        if (cmd.getSeekToSiteId() != null && cmd.getLimit() > 0) {
            int pageNum = siteTableDAO.queryPageNumber(user, criteria, order, cmd.getLimit(), cmd.getSeekToSiteId());
            offset = pageNum * cmd.getLimit();
        } else {
            offset = cmd.getOffset();
        }

        // query sites
        List<Site> results = siteTableDAO.query(user, criteria, order);
       
        
        int totalSize = results.size();
        
        // make a sublist for our current page
        if (totalSize > 0 ) {
	        int limit = cmd.getLimit();
			if (limit < 1) {
				limit = 50;
			}
			if (offset < 1) {
				offset = 0;
			}
			if (offset >= totalSize) {
				// result is empty list
				results  = new ArrayList<Site>();
			} else {
				int endIndex = offset + limit;
				if (endIndex >= totalSize) {
					endIndex = totalSize -1;
				}
				// only use a sublist going forward
				results = results.subList(offset, endIndex);
			}
        }

       // return now if no sites found
        if (totalSize < 1) {
        	return new SiteResult(new ArrayList(), offset, totalSize);
        }
        
        // convert sites to dtos
        Map<Integer, SiteDTO> sites = new LinkedHashMap<Integer, SiteDTO> ();
        for (Site s: results) {
        	sites.put(s.getId(), mapper.map(s, SiteDTO.class));
        }
        
        // add admin entities
        Map<Integer,Set<AdminEntity>> map = siteTableDAO.getSiteIdToEntitiesMap(sites.keySet());
        
        for (SiteDTO s: sites.values()) {
        	if (map.containsKey(s.getId())) {
        		for (AdminEntity ae: map.get(s.getId())) {
        			s.setAdminEntity(ae.getLevel().getId(), mapper.map(ae, AdminEntityDTO.class));
        		}
        	}
        }
        
        
        // Map<Integer, Set<AdminEntity>> getSiteIdToEntitiesMap(
    	//		Set<Integer> siteIds)
        
        
        // add attribute values
        
        
        
        // add indicator values
    
        
        return new SiteResult(new ArrayList(sites.values()), offset, totalSize);
		
    }

}
