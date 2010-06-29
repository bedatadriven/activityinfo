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
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.server.dao.hibernate;

import com.google.inject.Inject;
import org.activityinfo.server.dao.SiteDAO;
import org.activityinfo.server.domain.Site;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Alex Bertram
 */
public class SiteHibernateDAO extends GenericDAO<Site, Integer> implements SiteDAO {

    @Inject
    public SiteHibernateDAO(EntityManager em) {
        super(em);
    }

    @Override
    public void updateAttributeValues(int siteId, Map<Integer, Boolean> changes) {

        Set<Integer> falseValues = new HashSet<Integer>();
        Set<Integer> trueValues = new HashSet<Integer>();
        Set<Integer> nullValues = new HashSet<Integer>();

        for (Map.Entry<Integer, Boolean> change : changes.entrySet()) {
            int attributeId = change.getKey();
            Boolean value = change.getValue();
            if (value == null) {
                nullValues.add(attributeId);
            } else if (value) {
                trueValues.add(attributeId);
            } else {
                falseValues.add(attributeId);
            }
        }

        if (!nullValues.isEmpty()) {
            removeAttributeValueRowsFor(siteId, nullValues);
        }

        if (!trueValues.isEmpty() || !falseValues.isEmpty()) {
            Set<Integer> knownValues = new HashSet<Integer>();
            knownValues.addAll(trueValues);
            knownValues.addAll(falseValues);

            insertMissingRows(siteId, knownValues);

            // now set the values

            if (trueValues.isEmpty()) {
                updateValuesSetAllToFalse(siteId, knownValues);
            } else {
                updateValues(siteId, trueValues, knownValues);
            }
        }
    }

    private void updateValuesSetAllToFalse(int siteId, Set<Integer> knownValues) {
        em.createNativeQuery("UPDATE AttributeValue " +
                "SET Value = 0 " +
                "WHERE SiteId = :siteId AND " +
                "AttributeId IN " + attributeList(knownValues))
                .setParameter("siteId", siteId)
                .executeUpdate();
    }

    private void updateValues(int siteId, Set<Integer> trueValues, Set<Integer> knownValues) {
        em.createNativeQuery("UPDATE AttributeValue " +
                "SET Value = (CASE WHEN (AttributeId IN " + attributeList(trueValues) + ") " +
                "THEN 1 ELSE 0 END) " +
                "WHERE SiteId = :siteId AND " +
                "AttributeId IN " + attributeList(knownValues))
                .setParameter("siteId", siteId)
                .executeUpdate();
    }

    private void insertMissingRows(int siteId, Set<Integer> knownValues) {
        em.createNativeQuery("INSERT INTO AttributeValue (SiteId, AttributeId, Value) " +
                "SELECT :siteId AS SiteId, AttributeId, 0 AS Value " +
                "FROM Attribute AS a " +
                "WHERE AttributeId in " + attributeList(knownValues) +
                " AND AttributeId NOT IN " +
                "(SELECT v.AttributeId FROM AttributeValue AS v WHERE SiteId = :siteId)")
                .setParameter("siteId", siteId)
                .executeUpdate();
    }

    private void removeAttributeValueRowsFor(int siteId, Set<Integer> nullValues) {
        // the values for this attribute group are "unknown" and
        // need to be removed from the database

        em.createQuery("delete AttributeValue v where v.site.id = ?1 and v.attribute.id in " +
                attributeList(nullValues))
                .setParameter(1, siteId)
                .executeUpdate();
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
}
