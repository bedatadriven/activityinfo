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

package org.activityinfo.server.endpoint.gwtrpc.handler;

import org.activityinfo.server.domain.*;
import org.activityinfo.shared.dto.*;
import org.activityinfo.shared.exception.IllegalAccessCommandException;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Provides functionality common to CreateEntityHandler and
 * UpdateEntityHandler
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class BaseEntityHandler {
    protected final EntityManager em;

    public BaseEntityHandler(EntityManager em) {
        this.em = em;
    }


    protected void updateSiteProperties(Site site, Map<String, Object> changes, boolean creating) {

        int primaryReportingPeriodId = -1;

        for (Map.Entry<String, Object> change : changes.entrySet()) {

            String property = change.getKey();
            Object value = change.getValue();

            if ("date1".equals(property)) {
                site.setDate1((Date) value);

            } else if ("date2".equals(property)) {
                site.setDate2((Date) value);

            } else if ("assessmentId".equals(property)) {
                site.setAssessment(em.getReference(Site.class, value));

            } else if ("comments".equals(property)) {
                site.setComments((String) value);

            } else if ("status".equals(property)) {
                site.setStatus((Integer) value);
            }
        }
    }

    /**
     * Asserts that the user has permission to edit a site in a given database
     * belonging to a given partner
     *
     * @param user     The user for whom to check permission
     * @param activity The activity to which the site belongs
     * @param partner  The partner who owns the site
     * @throws IllegalAccessCommandException if the user does not have permission to modify this <code>Site</code>
     */
    protected void assertSiteEditPriveleges(User user, Activity activity, Partner partner) throws IllegalAccessCommandException {
        UserDatabase db = activity.getDatabase();

        if (db.getOwner().getId() == user.getId())
            return;

        UserPermission perm = db.getPermissionByUser(user);
        if (perm.isAllowEditAll())
            return;
        if (!perm.isAllowEdit())
            throw new IllegalAccessCommandException();
        if (perm.getPartner().getId() != partner.getId())
            throw new IllegalAccessCommandException();
    }

    /**
     * Asserts that the user has permission to modify the structure of the given database.
     *
     * @param user     THe user for whom to check permissions
     * @param database The database the user is trying to modify
     * @throws IllegalAccessCommandException If the user does not have permission
     */
    protected void assertDesignPriviledges(User user, UserDatabase database) throws IllegalAccessCommandException {

        if (!database.isAllowedDesign(user)) {
            throw new IllegalAccessCommandException();
        }

    }


    protected void updateLocationProperties(Location location, Map<String, Object> changes) {

        boolean isAdminBound = location.getLocationType().getBoundAdminLevel() != null;

        for (Map.Entry<String, Object> change : changes.entrySet()) {

            String property = change.getKey();
            Object value = change.getValue();

            if (!isAdminBound && "locationName".equals(property)) {
                location.setName((String) value);

            } else if ("locationAxe".equals(property)) {
                location.setAxe((String) value);

            } else if ("x".equals(property)) {
                location.setX((Double) value);

            } else if ("y".equals(property)) {
                location.setY((Double) changes.get("y"));

            } else if (isAdminBound &&
                    AdminLevelModel.getPropertyName(location.getLocationType().getBoundAdminLevel().getId()).equals(property)) {

                location.setName(em.find(AdminEntity.class, ((AdminEntityModel) value).getId()).getName());

            }
        }
    }

    protected void updateAdminProperties(Location location, Map<String, Object> changes, boolean creating) {

        for (Map.Entry<String, Object> change : changes.entrySet()) {
            String property = change.getKey();
            Object value = change.getValue();

            if (property.startsWith(AdminLevelModel.PROPERTY_PREFIX)) {

                int levelId = AdminLevelModel.levelIdForProperty(property);

                // clear the existing entity for this level

                if (!creating) {

                    em.createNativeQuery("delete from LocationAdminLink where " +
                            "LocationId = ?1 and " +
                            "AdminEntityId in " +
                            "(select e.AdminEntityId from AdminEntity e where e.AdminLevelId = ?2)")
                            .setParameter(1, location.getId())
                            .setParameter(2, levelId)
                            .executeUpdate();
                }

                if (value != null) {

                    AdminEntityModel entity = (AdminEntityModel) value;

                    location.getAdminEntities().add(em.getReference(AdminEntity.class, entity.getId()));

                }
            }
        }
    }

    protected void updateAttributeValueProperties(Site site, Map<String, Object> changes, boolean creating) {


        Set<Integer> falseValues = new HashSet<Integer>();
        Set<Integer> trueValues = new HashSet<Integer>();
        Set<Integer> nullValues = new HashSet<Integer>();

        for (Map.Entry<String, Object> change : changes.entrySet()) {

            String property = change.getKey();
            Object value = change.getValue();

            if (property.startsWith(AttributeModel.PROPERTY_PREFIX)) {

                int attributeId = AttributeModel.idForPropertyName(property);

                if (value == null) {
                    nullValues.add(attributeId);
                } else if ((Boolean) value) {
                    trueValues.add(attributeId);
                } else {
                    falseValues.add(attributeId);
                }
            }
        }

        if (nullValues.size() != 0) {

            // the values for this attribute group are "unknown" and
            // need to be removed from the database

            em.createQuery("delete AttributeValue v where v.site.id = ?1 and v.attribute.id in " +
                    attributeList(nullValues))
                    .setParameter(1, site.getId())
                    .executeUpdate();
        }

        if (trueValues.size() != 0 || falseValues.size() != 0) {
            Set<Integer> knownValues = new HashSet<Integer>();
            knownValues.addAll(trueValues);
            knownValues.addAll(falseValues);


            em.createNativeQuery("INSERT INTO AttributeValue (SiteId, AttributeId, Value) " +
                    "SELECT :siteId AS SiteId, AttributeId, 0 AS Value " +
                    "FROM Attribute AS a " +
                    "WHERE AttributeId in " + attributeList(knownValues) +
                    " AND AttributeId NOT IN " +
                    "(SELECT v.AttributeId FROM AttributeValue AS v WHERE SiteId = :siteId)")
                    .setParameter("siteId", site.getId())
                    .executeUpdate();


            // now set the values

            if (trueValues.size() != 0) {

                em.createNativeQuery("UPDATE AttributeValue " +
                        "SET Value = (CASE WHEN (AttributeId IN " + attributeList(trueValues) + ") " +
                        "THEN 1 ELSE 0 END) " +
                        "WHERE SiteId = :siteId AND " +
                        "AttributeId IN " + attributeList(knownValues))
                        .setParameter("siteId", site.getId())
                        .executeUpdate();
            } else {
                em.createNativeQuery("UPDATE AttributeValue " +
                        "SET Value = 0 " +
                        "WHERE SiteId = :siteId AND " +
                        "AttributeId IN " + attributeList(knownValues))
                        .setParameter("siteId", site.getId())
                        .executeUpdate();

            }

        }
    }

    protected String attributeList(Set<Integer> attributes) {

        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (Integer id : attributes) {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(id);
        }
        sb.append(")");
        return sb.toString();
    }


    protected void updatePeriodProperties(ReportingPeriod period, Map<String, Object> changes, boolean creating) {

        for (Map.Entry<String, Object> change : changes.entrySet()) {

            String property = change.getKey();
            Object value = change.getValue();

            if ("date1".equals(property)) {
                period.setDate1((Date) value);

            } else if ("date2".equals(property)) {
                period.setDate2((Date) value);
            }
        }

    }

    public static void updateIndicatorValue(EntityManager em, ReportingPeriod period, int indicatorId, Double value, boolean creating) {


        if (value == null && !creating) {
            int rowsAffected = em.createQuery("delete IndicatorValue v where v.indicator.id = ?1 and v.reportingPeriod.id = ?2")
                    .setParameter(1, indicatorId)
                    .setParameter(2, period.getId())
                    .executeUpdate();

            assert rowsAffected <= 1 : "whoops, deleted too many";

        } else if (value != null) {

            int rowsAffected = 0;

            if (!creating) {
                rowsAffected = em.createQuery("update IndicatorValue v set v.value = ?1 where " +
                        "v.indicator.id = ?2 and " +
                        "v.reportingPeriod.id = ?3")
                        .setParameter(1, (Double) value)
                        .setParameter(2, indicatorId)
                        .setParameter(3, period.getId())
                        .executeUpdate();
            }

            if (rowsAffected == 0) {

                IndicatorValue iValue = new IndicatorValue(
                        period,
                        em.getReference(Indicator.class, indicatorId),
                        (Double) value);

                em.persist(iValue);

            }
        }

    }

    protected void updateIndicatorValueProperties(ReportingPeriod period, Map<String, Object> changes, boolean creating) {


        for (Map.Entry<String, Object> change : changes.entrySet()) {

            String property = change.getKey();
            Object value = change.getValue();

            if (property.startsWith(IndicatorModel.PROPERTY_PREFIX)) {

                int indicatorId = IndicatorModel.indicatorIdForPropertyName(property);

                updateIndicatorValue(em, period, indicatorId, (Double) value, creating);
            }
        }
    }

    protected void updateIndicatorProperties(Indicator indicator, Map<String, Object> changes) {
        if (changes.containsKey("name"))
            indicator.setName((String) changes.get("name"));

        if (changes.containsKey("aggregation"))
            indicator.setAggregation((Integer) changes.get("aggregation"));

        if (changes.containsKey("category"))
            indicator.setCategory((String) changes.get("category"));

        if (changes.containsKey("collectIntervention"))
            indicator.setCollectIntervention((Boolean) changes.get("collectIntervention"));

        if (changes.containsKey("collectMonitoring"))
            indicator.setCollectMonitoring((Boolean) changes.get("collectMonitoring"));

        if (changes.containsKey("listHeader"))
            indicator.setListHeader((String) changes.get("listHeader"));

        if (changes.containsKey("description"))
            indicator.setDescription((String) changes.get("description"));

        if (changes.containsKey("units"))
            indicator.setUnits((String) changes.get("units"));

        if (changes.containsKey("sortOrder"))
            indicator.setSortOrder((Integer) changes.get("sortOrder"));

        indicator.getActivity().getDatabase().setLastSchemaUpdate(new Date());
    }

    protected void updateAttributeProperties(Map<String, Object> changes, Attribute attribute) {
        if (changes.containsKey("name"))
            attribute.setName((String) changes.get("name"));
        if (changes.containsKey("sortOrder"))
            attribute.setSortOrder((Integer) changes.get("sortOrder"));

        // TODO: update lastSchemaUpdate
    }

    protected void updateAttributeGroupProperties(AttributeGroup group, Map<String, Object> changes) {
        if (changes.containsKey("name"))
            group.setName((String) changes.get("name"));

        if (changes.containsKey("multipleAllowed")) {
            group.setMultipleAllowed((Boolean) changes.get("multipleAllowed"));
        }
        if (changes.containsKey("sortOrder")) {
            group.setSortOrder((Integer) changes.get("sortOrder"));
        }


    }

    protected void updateActivityProperties(Activity activity, Map<String, Object> changes) {
        if (changes.containsKey("name"))
            activity.setName((String) changes.get("name"));

        if (changes.containsKey("assessment"))
            activity.setAssessment((Boolean) changes.get("assessment"));

        if (changes.containsKey("locationType"))
            activity.setLocationType(
                    em.getReference(LocationType.class,
                            ((LocationTypeModel) changes.get("locationType")).getId()));

        if (changes.containsKey("category"))
            activity.setCategory((String) changes.get("category"));

        if (changes.containsKey("mapIcon"))
            activity.setMapIcon((String) changes.get("mapIcon"));

        if (changes.containsKey("reportingFrequency"))
            activity.setReportingFrequency((Integer) changes.get("reportingFrequency"));

        if (changes.containsKey("sortOrder"))
            activity.setSortOrder((Integer) changes.get("sortOrder"));

        activity.getDatabase().setLastSchemaUpdate(new Date());
    }
}
