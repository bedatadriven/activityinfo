/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.inject.Inject;
import org.dozer.Mapper;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.sigmah.server.dao.SiteProjectionBinder;
import org.sigmah.server.dao.SiteTableDAO;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.handler.GetSitesHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.dao.IndicatorDAO;
import org.sigmah.shared.dao.SiteOrder;
import org.sigmah.shared.dao.SiteTableColumn;
import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.exception.CommandException;

import java.util.*;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.GetSites
 */
public class GetSitesHandlerHibernate implements GetSitesHandler<GetSites> {

    private final SiteTableDAO siteDAO;
    private final IndicatorDAO indicatorDAO;
    private final Mapper mapper;

    @Inject
    public GetSitesHandlerHibernate(SiteTableDAO siteDAO, IndicatorDAO indicatorDAO, Mapper mapper) {
        this.siteDAO = siteDAO;
        this.indicatorDAO = indicatorDAO;
        this.mapper = mapper;
    }


    @Override
    public CommandResult execute(GetSites cmd, User user) throws CommandException {

        /*
         * Create our criterion for this query
         */

        Conjunction criteria = Restrictions.conjunction();

        /*
         * Build the user filter if provided
         */

        /*
           * And the ordering...
           */

        List<SiteOrder> order = new ArrayList<SiteOrder>();

        if (cmd.getSortInfo().getSortDir() != SortDir.NONE) {
            String field = cmd.getSortInfo().getSortField();

            if (field.equals("date1")) {
                order.add( order(SiteTableColumn.date1, cmd.getSortInfo()));
            } else if (field.equals("date2")) {
                order.add(order(SiteTableColumn.date2, cmd.getSortInfo()));
            } else if (field.equals("locationName")) {
                order.add(order(SiteTableColumn.location_name, cmd.getSortInfo()));
            } else if (field.equals("partner")) {
                order.add(order(SiteTableColumn.partner_name, cmd.getSortInfo()));
            } else if (field.equals("locationAxe")) {
                order.add(order(SiteTableColumn.location_axe, cmd.getSortInfo()));
            } else {
                order.add(new SiteOrder(field, cmd.getSortInfo().getSortDir() == SortDir.DESC));
            }
        }

        /*
         *  If we need to seek to the page that contains a given id,
         *  we need to do that here.
         */

        int offset;

        if (cmd.getSeekToSiteId() != null && cmd.getLimit() > 0) {

            int pageNum = siteDAO.queryPageNumber(user, cmd.getFilter(), order, cmd.getLimit(), cmd.getSeekToSiteId());

            offset = pageNum * cmd.getLimit();

        } else {
            offset = cmd.getOffset();

        }


        /*
           * Execute !
           */
        List<SiteDTO> sites = siteDAO.query(user, cmd.getFilter(), order,
                new ModelBinder(mapper), SiteTableDAO.RETRIEVE_ALL, offset, cmd.getLimit());

        return new SiteResult(sites, offset, siteDAO.queryCount(cmd.getFilter()));

    }

    protected SiteOrder order(SiteTableColumn column, SortInfo si) {
        if (si.getSortDir() == SortDir.ASC) {
            return SiteOrder.ascendingOn(column.property());
        } else {
            return SiteOrder.descendingOn(column.property());
        }
    }

    protected class ModelBinder implements SiteProjectionBinder<SiteDTO> {

        private Map<Integer, AdminEntityDTO> adminEntities = new HashMap<Integer, AdminEntityDTO>();
        private Map<Integer, PartnerDTO> partners = new HashMap<Integer, PartnerDTO>();
        private Mapper mapper;
        
        @Inject
        public ModelBinder(Mapper mapper){
        	this.mapper=mapper;
        }

        @Override
        public SiteDTO newInstance(String[] properties, Object[] values) {
            SiteDTO model = new SiteDTO();
            model.setId((Integer) values[SiteTableColumn.id.index()]);
            model.setActivityId((Integer) values[SiteTableColumn.activity_id.index()]);
            model.setDate1((Date) values[SiteTableColumn.date1.index()]);
            model.setDate2((Date) values[SiteTableColumn.date2.index()]);
            model.setLocationName((String) values[SiteTableColumn.location_name.index()]);
            model.setLocationAxe((String) values[SiteTableColumn.location_axe.index()]);
            model.setX((Double) values[SiteTableColumn.x.index()]);
            model.setY((Double) values[SiteTableColumn.y.index()]);
            model.setComments((String) values[SiteTableColumn.comments.index()]);

            int partnerId = (Integer) values[SiteTableColumn.partner_id.index()];
            PartnerDTO partner = partners.get(partnerId);
            if (partner == null) {
                partner = new PartnerDTO(
                        partnerId,
                        (String) values[SiteTableColumn.partner_name.index()]);
                partners.put(partnerId, partner);
            }

            model.setPartner(partner);
            return model;

        }

        @Override
        public void setAdminEntity(SiteDTO site, AdminEntity entity) {
            AdminEntityDTO model = adminEntities.get(entity.getId());
            if (model == null) {
                model = mapper.map(entity, AdminEntityDTO.class);
                adminEntities.put(entity.getId(), model);

            }
            site.setAdminEntity(entity.getLevel().getId(), model);
        }

        @Override
        public void setAttributeValue(SiteDTO site, int attributeId,
                                      boolean value) {

            site.setAttributeValue(attributeId, value);

        }

        @Override
        public void addIndicatorValue(SiteDTO site, int indicatorId,
                                      int aggregationMethod, double value) {

            site.setIndicatorValue(indicatorId, value);

        }
    }

}
