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
import org.sigmah.shared.domain.LogFrame;
import org.sigmah.shared.domain.LogFrameRow;
import org.sigmah.shared.domain.LogFrameRowGroup;
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
import org.sigmah.shared.domain.Report;
import org.sigmah.shared.domain.ReportingPeriod;
import org.sigmah.shared.domain.Site;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.UserDatabase;
import org.sigmah.shared.domain.UserPermission;
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
import org.sigmah.shared.domain.value.Value;

public class PersistentClasses {
    public static final Class<?>[] LIST = {
            OrgUnit.class,
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
            QualityCriterion.class,
            FlexibleElement.class,
            ProjectModel.class,
            MessageElement.class,
            CheckboxElement.class,
            QuestionElement.class,
            TextAreaElement.class,
            PrivacyLevel.class,
            LogFrameRow.class,
            LogFrameRowGroup.class,
            LogFrame.class,
            CategoryElement.class,
            CategoryType.class
    };
}
