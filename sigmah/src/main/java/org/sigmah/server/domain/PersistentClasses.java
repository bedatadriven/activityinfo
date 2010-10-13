package org.sigmah.server.domain;

import org.sigmah.shared.domain.*;
import org.sigmah.shared.domain.category.CategoryElement;
import org.sigmah.shared.domain.category.CategoryType;
import org.sigmah.shared.domain.element.*;
import org.sigmah.shared.domain.layout.Layout;
import org.sigmah.shared.domain.layout.LayoutConstraint;
import org.sigmah.shared.domain.layout.LayoutGroup;
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
