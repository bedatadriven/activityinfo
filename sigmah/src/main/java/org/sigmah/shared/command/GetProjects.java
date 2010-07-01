/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import org.sigmah.shared.command.result.ProjectList;

/**
 * Retrieves the list of Projects available to the user
 */
public class GetProjects implements Command<ProjectList> {

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GetProjects;
    }
}

