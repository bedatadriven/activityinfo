/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.command.handler;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.inject.Inject;
import org.activityinfo.server.dao.SchemaDAO;
import org.activityinfo.server.dao.SiteProjectionBinder;
import org.activityinfo.server.dao.SiteTableDAO;
import org.activityinfo.server.dao.filter.FrenchFilterParser;
import org.activityinfo.server.dao.hibernate.SiteAdminOrder;
import org.activityinfo.server.dao.hibernate.SiteIndicatorOrder;
import org.activityinfo.server.domain.AdminEntity;
import org.activityinfo.server.domain.Indicator;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.generator.FilterCriterionBridge;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.domain.SiteColumn;
import org.activityinfo.shared.dto.*;
import org.activityinfo.shared.exception.CommandException;
import org.dozer.Mapper;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.*;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.GetSites
 */
public class GetSitesHandler implements CommandHandler<GetSites> {

    private final SiteTableDAO siteDAO;
    private final SchemaDAO schemaDAO;
    private final Mapper mapper;
    private final FrenchFilterParser parser;

    @Inject
    public GetSitesHandler(SiteTableDAO siteDAO, SchemaDAO schemaDAO, Mapper mapper, FrenchFilterParser parser) {
        this.siteDAO = siteDAO;
        this.schemaDAO = schemaDAO;
        this.mapper = mapper;
        this.parser = parser;
    }


    @Override
    public CommandResult execute(GetSites cmd, User user) throws CommandException {

        /*
           * Create our criterion for this query
           */

        Conjunction criteria = Restrictions.conjunction();
        if (cmd.getSiteId() != null) {
            criteria.add(Restrictions.eq(SiteColumn.id.property(), cmd.getSiteId()));
        } else if (cmd.getActivityId() != null) {
            criteria.add(Restrictions.eq(SiteColumn.activity_id.property(),
                    cmd.getActivityId()));
        } else if (cmd.getDatabaseId() != null) {
            criteria.add(Restrictions.eq(SiteColumn.database_id.property(),
                    cmd.getDatabaseId()));
        }
        if (cmd.isAssessmentsOnly()) {
            criteria.add(Restrictions.eq("activity.assessment", true));
        }

        /*
         * Build the user filter if provided
         */

        if (cmd.getFilter() != null) {
            criteria.add(parser.parse(cmd.getFilter()));
        }

        if (cmd.getPivotFilter() != null) {
            criteria.add(FilterCriterionBridge.resolveCriterion(cmd.getPivotFilter()));
        }

        /*
           * And the ordering...
           */

        List<Order> order = new ArrayList<Order>();

        if (cmd.getSortInfo().getSortDir() != SortDir.NONE) {

            String field = cmd.getSortInfo().getSortField();

            if (field.equals("date1")) {
                order.add(order(SiteColumn.date1, cmd.getSortInfo()));
            } else if (field.equals("date2")) {
                order.add(order(SiteColumn.date2, cmd.getSortInfo()));
            } else if (field.equals("locationName")) {
                order.add(order(SiteColumn.location_name, cmd.getSortInfo()));
            } else if (field.equals("partner")) {
                order.add(order(SiteColumn.partner_name, cmd.getSortInfo()));
            } else if (field.equals("locationAxe")) {
                order.add(order(SiteColumn.location_axe, cmd.getSortInfo()));
            } else if (field.startsWith(IndicatorModel.PROPERTY_PREFIX)) {

                Indicator indicator = schemaDAO.findById(Indicator.class,
                        IndicatorModel.indicatorIdForPropertyName(field));

                order.add(new SiteIndicatorOrder(indicator,
                        cmd.getSortInfo().getSortDir() == SortDir.ASC));

            } else if (field.startsWith("a")) {

                int levelId = AdminLevelModel.levelIdForProperty(field);

                order.add(new SiteAdminOrder(levelId,
                        cmd.getSortInfo().getSortDir() == SortDir.ASC));

            }

        }

        /*
         *  If we need to seek to the page that contains a given id,
         *  we need to do that here.
         */

        int offset;

        if (cmd.getSeekToSiteId() != null && cmd.getLimit() > 0) {

            int pageNum = siteDAO.queryPageNumber(user, criteria, order, cmd.getLimit(), cmd.getSeekToSiteId());

            offset = pageNum * cmd.getLimit();

        } else {
            offset = cmd.getOffset();

        }


        /*
           * Execute !
           */
        List<SiteModel> sites = siteDAO.query(user, criteria, order,
                new ModelBinder(), SiteTableDAO.RETRIEVE_ALL, offset, cmd.getLimit());


        return new SiteResult(sites, offset, siteDAO.queryCount(criteria));

    }

    protected Order order(SiteColumn column, SortInfo si) {
        if (si.getSortDir() == SortDir.ASC) {
            return Order.asc(column.property());
        } else {
            return Order.desc(column.property());
        }
    }


    protected class ModelBinder implements SiteProjectionBinder<SiteModel> {

        private Map<Integer, AdminEntityModel> adminEntities = new HashMap<Integer, AdminEntityModel>();
        private Map<Integer, PartnerModel> partners = new HashMap<Integer, PartnerModel>();

        @Override
        public SiteModel newInstance(String[] properties, Object[] values) {
            SiteModel model = new SiteModel();
            model.setId((Integer) values[SiteColumn.id.index()]);
            model.setActivityId((Integer) values[SiteColumn.activity_id.index()]);
            model.setDate1((Date) values[SiteColumn.date1.index()]);
            model.setDate2((Date) values[SiteColumn.date2.index()]);
            model.setLocationName((String) values[SiteColumn.location_name.index()]);
            model.setLocationAxe((String) values[SiteColumn.location_axe.index()]);
            model.setStatus((Integer) values[SiteColumn.status.index()]);
            model.setX((Double) values[SiteColumn.x.index()]);
            model.setY((Double) values[SiteColumn.y.index()]);
            model.setComments((String) values[SiteColumn.comments.index()]);

            int partnerId = (Integer) values[SiteColumn.partner_id.index()];
            PartnerModel partner = partners.get(partnerId);
            if (partner == null) {
                partner = new PartnerModel(
                        partnerId,
                        (String) values[SiteColumn.partner_name.index()]);
                partners.put(partnerId, partner);
            }

            model.setPartner(partner);


            return model;

        }

        @Override
        public void setAdminEntity(SiteModel site, AdminEntity entity) {
            AdminEntityModel model = adminEntities.get(entity.getId());
            if (model == null) {
                model = mapper.map(entity, AdminEntityModel.class);
                adminEntities.put(entity.getId(), model);
            }
            site.setAdminEntity(entity.getLevel().getId(), model);
        }

        @Override
        public void setAttributeValue(SiteModel site, int attributeId,
                                      boolean value) {

            site.setAttributeValue(attributeId, value);

        }

        @Override
        public void addIndicatorValue(SiteModel site, int indicatorId,
                                      int aggregationMethod, double value) {

            site.setIndicatorValue(indicatorId, value);

        }


    }

}
