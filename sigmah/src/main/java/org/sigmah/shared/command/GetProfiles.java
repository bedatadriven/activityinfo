package org.sigmah.shared.command;

import org.sigmah.shared.command.result.ProfileListResult;

public class GetProfiles implements Command<ProfileListResult> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5226528360717160558L;
	/**
     * The type of model of the models for the current user organization (set to
     * <code>null</code> to ignore this filter).
     */
    public GetProfiles() {
        // serialization.
    }
}
