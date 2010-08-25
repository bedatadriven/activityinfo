package org.sigmah.shared.dto.element;

import java.util.HashMap;

import org.sigmah.client.i18n.I18N;

/**
 * Utility class to get some properties of each type of flexible element.
 * 
 * @author tmi
 * 
 */
public final class FlexibleElementType {

    public static final HashMap<Class<? extends FlexibleElementDTO>, String> types;

    static {
        types = new HashMap<Class<? extends FlexibleElementDTO>, String>();
        types.put(BudgetDistributionElementDTO.class, I18N.CONSTANTS.flexibleElementBudgetDistribution());
        types.put(CheckboxElementDTO.class, I18N.CONSTANTS.flexibleElementCheckbox());
        types.put(FilesListElementDTO.class, I18N.CONSTANTS.flexibleElementFilesList());
        types.put(IndicatorsListElementDTO.class, I18N.CONSTANTS.flexibleElementIndicatorsList());
        types.put(MessageElementDTO.class, I18N.CONSTANTS.flexibleElementMessage());
        types.put(QuestionElementDTO.class, I18N.CONSTANTS.flexibleElementQuestion());
        types.put(TextAreaElementDTO.class, I18N.CONSTANTS.flexibleElementTextArea());
        types.put(TripletsListElementDTO.class, I18N.CONSTANTS.flexibleElementTripletsList());
    }

    /**
     * Provides only static methods.
     */
    private FlexibleElementType() {
    }

    /**
     * Gets a flexible element type name (translation key).
     * 
     * @param fe
     *            The flexible element.
     * @return The flexible element type name (translation key).
     */
    public static <E extends FlexibleElementDTO> String getFlexibleElementTypeName(E fe) {
        return types.get(fe.getClass());
    }
}
