package org.sigmah.server.domain;

import org.sigmah.server.domain.element.*;
import org.sigmah.server.domain.layout.Layout;
import org.sigmah.server.domain.layout.LayoutConstraint;
import org.sigmah.server.domain.layout.LayoutGroup;
import org.sigmah.server.domain.value.Value;
import org.sigmah.shared.domain.*;

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
            LogFrame.class
    };
}
