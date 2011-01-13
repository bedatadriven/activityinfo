package org.sigmah.shared.domain.profile;

/**
 * List of the global permissions.
 * 
 * @author tmi
 * 
 */
public enum GlobalPermissionEnum {

    /**
     * View the projects list and the project page.
     */
    VIEW_PROJECT,

    /**
     * Edit and save the project details, the project phases, the project
     * funding, the log frame and the calendar.
     */
    EDIT_PROJECT,

    /**
     * Create a new project or a new funding.
     */
    CREATE_PROJECT,

    /**
     * Close or activate a phase.
     */
    CHANGE_PHASE,

    /**
     * View the admin link.
     */
    VIEW_ADMIN,

    /**
     * View the admin page to manage users.
     */
    MANAGE_USER,

    /**
     * View the admin page to manage the org units.
     */
    MANAGE_UNIT,

    /**
     * Remove a file (in the files list flexible element).
     */
    REMOVE_FILE;
}
