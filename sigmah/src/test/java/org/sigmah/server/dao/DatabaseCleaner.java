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
import org.sigmah.server.domain.Authentication;
import org.sigmah.server.domain.LogFrame;
import org.sigmah.server.domain.LogFrameRow;
import org.sigmah.server.domain.LogFrameRowGroup;
import org.sigmah.server.domain.OrgUnitPermission;
import org.sigmah.server.domain.Phase;
import org.sigmah.server.domain.PhaseModel;
import org.sigmah.server.domain.Project;
import org.sigmah.server.domain.ProjectBanner;
import org.sigmah.server.domain.ProjectDetails;
import org.sigmah.server.domain.ProjectModel;
import org.sigmah.server.domain.Report;
import org.sigmah.server.domain.ReportDefinition;
import org.sigmah.server.domain.ReportSubscription;
import org.sigmah.server.domain.element.CheckboxElement;
import org.sigmah.server.domain.element.FlexibleElement;
import org.sigmah.server.domain.element.MessageElement;
import org.sigmah.server.domain.element.QuestionChoiceElement;
import org.sigmah.server.domain.element.QuestionElement;
import org.sigmah.server.domain.element.TextAreaElement;
import org.sigmah.server.domain.layout.Layout;
import org.sigmah.server.domain.layout.LayoutConstraint;
import org.sigmah.server.domain.layout.LayoutGroup;
import org.sigmah.server.domain.value.Value;
import org.sigmah.shared.domain.Activity;
import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.domain.AdminLevel;
import org.sigmah.shared.domain.Attribute;
import org.sigmah.shared.domain.AttributeGroup;
import org.sigmah.shared.domain.AttributeValue;
import org.sigmah.shared.domain.Country;
import org.sigmah.shared.domain.Indicator;
import org.sigmah.shared.domain.IndicatorValue;
import org.sigmah.shared.domain.Location;
import org.sigmah.shared.domain.LocationType;
import org.sigmah.shared.domain.OrgUnit;
import org.sigmah.shared.domain.Organization;
import org.sigmah.shared.domain.PrivacyLevel;
import org.sigmah.shared.domain.ReportingPeriod;
import org.sigmah.shared.domain.Site;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.UserDatabase;
import org.sigmah.shared.domain.UserPermission;

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
            Phase.class,
            Value.class,
            UserDatabase.class,
            Authentication.class,
            User.class,
            LocationType.class,
            AdminEntity.class,
            AdminLevel.class,
            Country.class,
            Report.class,
            ProjectBanner.class,
            ProjectDetails.class,
            Project.class,
            PhaseModel.class,
            LayoutConstraint.class,
            LayoutGroup.class,
            Layout.class,
            QuestionChoiceElement.class,
            FlexibleElement.class,
            ProjectModel.class,
            MessageElement.class,
            CheckboxElement.class,
            QuestionElement.class,
            TextAreaElement.class,
            PrivacyLevel.class,
            LogFrameRow.class,
            LogFrameRowGroup.class,
            LogFrame.class
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
                
                // FIXME H2 compatible only. This statement disables the constraints of the database to perform deletions without integrity problems.
                stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");
                
                stmt.execute("delete from AttributeGroupInActivity");
                stmt.execute("delete from PartnerInDatabase");
                stmt.execute("delete from LocationAdminLink");
                stmt.execute("delete from phase_model_sucessors");
            }
        });

        for (Class<?> entityType : ENTITY_TYPES) {
            em.createQuery("delete from " + entityType.getName())
                    .executeUpdate();
        }

        tx.commit();
    }

}

