/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.command.handler;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.inject.Inject;
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
import org.sigmah.shared.report.model.DimensionType;

import java.util.*;


/**
 * An off-line CommandHandler for the GetSites command.
 * 
 * @author jon
 *
 */
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
    	criteria.put("site.siteId", firstOrNull(cmd.getFilter().getRestrictions(DimensionType.Site)));
    	criteria.put("site.activityId", firstOrNull(cmd.getFilter().getRestrictions(DimensionType.Activity)));
    	criteria.put("site.databaseId", firstOrNull(cmd.getFilter().getRestrictions(DimensionType.Database)));

    	// TODO Filters
    	//criteria.put("filter", cmd.getFilter());
    	//criteria.put("pivotFilter",cmd.getFilter());
     
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
        
        Log.debug("START ADD ENITTIES");
        
        // add entities
        addEntities(sites);
        
        // add attribute values
        addAttributes(sites);

        // add indicator values
        addIndicators(sites);
        
        Log.debug("END ADD SITES");
        
        return new SiteResult(new ArrayList(sites.values()), offset, totalSize);
		
    }

    private <T> T firstOrNull(Set<T> restrictions) {
        if(restrictions.isEmpty()) {
            return null;
        } else {
            return restrictions.iterator().next();
        }
    }

    private void addEntities( Map<Integer, SiteDTO> sites) {
    	// add admin entities
        Map<Integer,Set<AdminEntity>> map = siteTableDAO.getSiteIdToEntitiesMap(sites.keySet());
        Log.debug("got entity size: " + map.size());
         
        Map<Integer,AdminEntityDTO> dtoMap = new HashMap<Integer,AdminEntityDTO>();
        
        for (SiteDTO s: sites.values()) {
        	if (map.containsKey(s.getId())) {
        		for (AdminEntity ae: map.get(s.getId())) {
        		    AdminEntityDTO dto;
        		    if (dtoMap.containsKey(ae.getId())) {
        		    	dto = dtoMap.get(ae.getId());
        		    } else {
        		    	dto = mapper.map(ae, AdminEntityDTO.class);
        		    }
        		    
        			s.setAdminEntity(ae.getLevel().getId(), dto);
        			
        		}
        	}
        }
        
    }

	private void addAttributes(Map<Integer, SiteDTO> sites) {
		Map<Integer, Map<Integer, Boolean>> map = siteTableDAO
				.getSiteIdToAttributeMap(sites.keySet());
		  Log.debug("got attr size: " + map.size());
		for (SiteDTO s : sites.values()) {
			if (map.containsKey(s.getId())) {

				for (Integer attId : map.get(s.getId()).keySet()) {
					s.setAttributeValue(attId, map.get(s.getId()).get(attId));
				}
			}
		}
	}

	private void addIndicators(Map<Integer, SiteDTO> sites) {
		Map<Integer, Map<Integer, Double>> map = siteTableDAO
				.getSiteIdToIndicatorMap(sites.keySet());
		  Log.debug("got indicator size: " + map.size());
		for (SiteDTO s : sites.values()) {
			if (map.containsKey(s.getId())) {
				for (Integer inId : map.get(s.getId()).keySet()) {
					s.setIndicatorValue(inId, map.get(s.getId()).get(inId));
				}
			}
		}
	}
}
