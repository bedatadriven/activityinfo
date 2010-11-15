package org.sigmah.shared.domain;

import java.util.HashMap;

import org.sigmah.client.i18n.I18N;

/**
 * The different types of projects.
 * 
 * @author tmi
 * 
 */
public enum ProjectModelType {

    NGO, FUNDING, LOCAL_PARTNER;

    /**
     * The translation map.
     */
    private static HashMap<ProjectModelType, String> translationMap;

    /**
     * Gets the translation value for the given type. To use only on the
     * client-side.
     * 
     * @param type
     *            The type.
     * @return The translation value for the given type.
     */
    public static String getName(ProjectModelType type) {

        if (translationMap == null) {
            translationMap = new HashMap<ProjectModelType, String>();
            translationMap.put(NGO, I18N.CONSTANTS.createProjectTypeNGO());
            translationMap.put(FUNDING, I18N.CONSTANTS.createProjectTypeFunding());
            translationMap.put(LOCAL_PARTNER, I18N.CONSTANTS.createProjectTypePartner());
        }

        return translationMap.get(type);
    }
}
