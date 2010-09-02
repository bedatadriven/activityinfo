package org.sigmah.shared.command;

import org.sigmah.shared.command.result.ProjectModelListResult;

/**
 * Retrieves the list of project models available to the user.
 * 
 * @author tmi
 * 
 */
public class GetProjectModels implements Command<ProjectModelListResult> {

    private static final long serialVersionUID = 6533084223987010888L;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
