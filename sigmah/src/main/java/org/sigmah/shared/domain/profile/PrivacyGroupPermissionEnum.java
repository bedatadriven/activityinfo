package org.sigmah.shared.domain.profile;

/**
 * List of the permissions linked to a privacy group.
 * 
 * @author tmi
 * 
 */
public enum PrivacyGroupPermissionEnum {

    /**
     * Forbids all actions.
     */
    NONE,

    /**
     * Allows to view.
     */
    READ,

    /**
     * Allows to view and edit.
     */
    WRITE;
}
