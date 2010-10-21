package org.sigmah.server.domain;

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
import org.sigmah.shared.domain.Phase;
import org.sigmah.shared.domain.PhaseModel;
import org.sigmah.shared.domain.PrivacyLevel;
import org.sigmah.shared.domain.Project;
import org.sigmah.shared.domain.ProjectBanner;
import org.sigmah.shared.domain.ProjectDetails;
import org.sigmah.shared.domain.ProjectModel;
import org.sigmah.shared.domain.QualityCriterion;
import org.sigmah.shared.domain.QualityFramework;
import org.sigmah.shared.domain.Report;
import org.sigmah.shared.domain.ReportingPeriod;
import org.sigmah.shared.domain.Site;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.UserDatabase;
import org.sigmah.shared.domain.UserPermission;
import org.sigmah.shared.domain.calendar.PersonalCalendar;
import org.sigmah.shared.domain.calendar.PersonalEvent;
import org.sigmah.shared.domain.category.CategoryElement;
import org.sigmah.shared.domain.category.CategoryType;
import org.sigmah.shared.domain.element.CheckboxElement;
import org.sigmah.shared.domain.element.FlexibleElement;
import org.sigmah.shared.domain.element.MessageElement;
import org.sigmah.shared.domain.element.QuestionChoiceElement;
import org.sigmah.shared.domain.element.QuestionElement;
import org.sigmah.shared.domain.element.TextAreaElement;
import org.sigmah.shared.domain.layout.Layout;
import org.sigmah.shared.domain.layout.LayoutConstraint;
import org.sigmah.shared.domain.layout.LayoutGroup;
import org.sigmah.shared.domain.logframe.ExpectedResult;
import org.sigmah.shared.domain.logframe.LogFrame;
import org.sigmah.shared.domain.logframe.LogFrameActivity;
import org.sigmah.shared.domain.logframe.LogFrameGroup;
import org.sigmah.shared.domain.logframe.LogFrameModel;
import org.sigmah.shared.domain.logframe.Prerequisite;
import org.sigmah.shared.domain.logframe.SpecificObjective;
import org.sigmah.shared.domain.value.Value;

/**
 * List of persistent classes managed by Hibernate.
 *
 * IMPORTANT: the order of these classes is important:
 *
 */
public class PersistentClasses {
    public static final Class<?>[] LIST = {
            IndicatorValue.class,
            AttributeValue.class,
            ReportingPeriod.class,
            Site.class,
            OrgUnitPermission.class,
            Organization.class,
            UserPermission.class,
            OrgUnit.class,
            Location.class,
            Indicator.class,
            Attribute.class,
            AttributeGroup.class,
            Activity.class,
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
            QualityCriterion.class,
            QualityFramework.class,
            FlexibleElement.class,
            ProjectModel.class,
            MessageElement.class,
            CheckboxElement.class,
            QuestionElement.class,
            TextAreaElement.class,
            PrivacyLevel.class,
            Prerequisite.class,
            LogFrameActivity.class,
            ExpectedResult.class,
            SpecificObjective.class,
            LogFrame.class,
            LogFrameGroup.class,
            LogFrameModel.class,
            CategoryElement.class,
            CategoryType.class,
            PersonalCalendar.class,
            PersonalEvent.class
    };
}
