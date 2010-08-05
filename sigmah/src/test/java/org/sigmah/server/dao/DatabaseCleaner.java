/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jdbc.Work;
import org.sigmah.server.domain.Activity;
import org.sigmah.server.domain.AdminEntity;
import org.sigmah.server.domain.AdminLevel;
import org.sigmah.server.domain.Attribute;
import org.sigmah.server.domain.AttributeGroup;
import org.sigmah.server.domain.AttributeValue;
import org.sigmah.server.domain.Authentication;
import org.sigmah.server.domain.Country;
import org.sigmah.server.domain.Indicator;
import org.sigmah.server.domain.IndicatorValue;
import org.sigmah.server.domain.Location;
import org.sigmah.server.domain.LocationType;
import org.sigmah.server.domain.OrgUnit;
import org.sigmah.server.domain.OrgUnitPermission;
import org.sigmah.server.domain.Organization;
import org.sigmah.server.domain.PrivacyLevel;
import org.sigmah.server.domain.ReportDefinition;
import org.sigmah.server.domain.ReportSubscription;
import org.sigmah.server.domain.ReportingPeriod;
import org.sigmah.server.domain.Site;
import org.sigmah.server.domain.User;
import org.sigmah.server.domain.UserDatabase;
import org.sigmah.server.domain.UserPermission;

import com.google.inject.Inject;

public class DatabaseCleaner {

    private static final Class<?>[] ENTITY_TYPES = {
            IndicatorValue.class,
            AttributeValue.class,
            ReportingPeriod.class,
            Site.class,
            Location.class,
            Indicator.class,
            Attribute.class,
            AttributeGroup.class,
            Activity.class,
            UserPermission.class,
            OrgUnitPermission.class,
            OrgUnit.class,
            Organization.class,
            ReportSubscription.class,
            ReportDefinition.class,
            UserDatabase.class,
            Authentication.class,
            User.class,
            LocationType.class,
            AdminEntity.class,
            AdminLevel.class,
            Country.class,
            PrivacyLevel.class
    };

    private final EntityManager em;

    @Inject
    public DatabaseCleaner(EntityManager em) {
        this.em = em;
    }

    public void clean() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        HibernateEntityManager hem = (HibernateEntityManager) em;
        hem.getSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                Statement stmt = connection.createStatement();
                stmt.execute("delete from AttributeGroupInActivity");
                stmt.execute("delete from PartnerInDatabase");
                stmt.execute("delete from LocationAdminLink");
            }
        });

        for (Class<?> entityType : ENTITY_TYPES) {
            em.createQuery("delete from " + entityType.getName())
                    .executeUpdate();
        }

        tx.commit();
    }

}

