package org.sigmah.shared.command;

import org.sigmah.shared.command.result.UserListResult;

/**
 * Retrieves the organization users list.
 * 
 * @author nrebiai
 * 
 */
public class GetUsersWithProfiles implements Command<UserListResult> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3006666313649601894L;

	/**
     * The type of model of the models for the current user organization (set to
     * <code>null</code> to ignore this filter).
     */
    public GetUsersWithProfiles() {
        // serialization.
    }
}
