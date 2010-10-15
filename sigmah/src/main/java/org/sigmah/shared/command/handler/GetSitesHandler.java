/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.handler;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.inject.Inject;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.dao.SiteOrder;
import org.sigmah.shared.dao.SiteProjectionBinder;
import org.sigmah.shared.dao.SiteTableColumn;
import org.sigmah.shared.dao.SiteTableDAO;
import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.BoundingBoxDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.exception.CommandException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.sigmah.shared.dao.SiteTableColumn.*;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.GetSites
 */
public class GetSitesHandler implements CommandHandler<GetSites> {

    private final SiteTableDAO siteDAO;

    @Inject
    public GetSitesHandler(SiteTableDAO siteDAO) {
        this.siteDAO = siteDAO;
    }

    @Override
    public CommandResult execute(GetSites cmd, User user) throws CommandException {
        List<SiteOrder> order = sortInfoToSortOrder(cmd);
        int offset = calculateOffset(cmd, user, order);

        List<SiteDTO> sites = siteDAO.query(
                user,
                cmd.getFilter(),
                order,
                new ModelBinder(),
                SiteTableDAO.RETRIEVE_ALL,
                offset,
                cmd.getLimit());

        return new SiteResult(sites, offset, siteDAO.queryCount(user, cmd.getFilter()));
    }

    private int calculateOffset(GetSites cmd, User user, List<SiteOrder> order) {
        int offset;
        if (cmd.getSeekToSiteId() != null && cmd.getLimit() > 0) {
            int pageNum = siteDAO.queryPageNumber(
                    user,
                    cmd.getFilter(),
                    order,
                    cmd.getLimit(),
                    cmd.getSeekToSiteId());
            offset = pageNum * cmd.getLimit();

        } else {
            offset = cmd.getOffset();
        }
        return offset;
    }

    // TODO: ideally the client is just sending the SiteOrder object directly,
    // but we'll need to harmonize the field names first.
    private List<SiteOrder> sortInfoToSortOrder(GetSites cmd) {
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
        return order;
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


        @Override
        public SiteDTO newInstance(String[] properties, ResultSet rs) throws SQLException {
            SiteDTO model = new SiteDTO();
            model.setId( rs.getInt(SiteTableColumn.id.index()) );
            model.setActivityId( rs.getInt(activity_id.index() ));
            model.setDate1(rs.getDate(date1.index() ));
            model.setDate2(rs.getDate(date2.index() ));
            model.setLocationName( rs.getString( location_name.index() ));
            model.setLocationAxe( rs.getString( location_axe.index() ));
            model.setX( rs.getDouble( x.index()));
            model.setY( rs.getDouble( y.index()));
            model.setComments( rs.getString( comments.index() ));

            int partnerId = rs.getInt(partner_id.index());
            PartnerDTO partner = partners.get(partnerId);
            if (partner == null) {
                partner = new PartnerDTO(
                        partnerId,
                        rs.getString(partner_name.index()));
                partners.put(partnerId, partner);
            }

            model.setPartner(partner);
            return model;

        }

        @Override
        public void setAdminEntity(SiteDTO site, AdminEntity entity) {
            AdminEntityDTO model = adminEntities.get(entity.getId());
            if (model == null) {
                model = new AdminEntityDTO();
                model.setId(entity.getId());
                model.setName(entity.getName());
                model.setLevelId(entity.getLevel().getId());
                if(entity.getParent() != null) {
                    model.setParentId(entity.getParent().getId());
                }
                if(entity.getBounds() != null) {
                    model.setBounds(new BoundingBoxDTO(
                            entity.getBounds().getX1(),
                            entity.getBounds().getY1(),
                            entity.getBounds().getX2(),
                            entity.getBounds().getY2()
                    ));
                }
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
