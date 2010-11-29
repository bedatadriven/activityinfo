/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.sigmah.shared.dto.ProjectDTOLight;

/**
 * List of projects visible to the user.
 */
public class ProjectListResult implements CommandResult {

    private static final long serialVersionUID = 749007653017696900L;

    /**
     * A comparator which sort the projects with their codes.
     */
    public static final Comparator<ProjectDTOLight> CODE_COMPARATOR = new Comparator<ProjectDTOLight>() {

        @Override
        public int compare(ProjectDTOLight p1, ProjectDTOLight p2) {

            if (p1 == null) {
                if (p2 == null) {
                    return 0;
                } else {
                    return -1;
                }
            }

            if (p2 == null) {
                return 1;
            }

            return p1.getName() != null ? p1.getName().compareToIgnoreCase(p2.getName()) : -1;
        }
    };

    /**
     * List of 'light' mapped projects.
     */
    private List<ProjectDTOLight> list;

    public ProjectListResult() {
    }

    public ProjectListResult(List<ProjectDTOLight> list) {
        this.list = list;
    }

    /**
     * Get the list of projects.
     * 
     * @return The list of projects (never <code>null</code>).
     */
    @SuppressWarnings("unchecked")
    public List<ProjectDTOLight> getList() {
        return list != null ? list : (List<ProjectDTOLight>) Collections.EMPTY_LIST;
    }

    public void setList(List<ProjectDTOLight> list) {
        this.list = list;
    }

    /**
     * Gets the projects list ordered with the given comparator.
     * 
     * @param comparator
     *            The comparator.
     * @return The ordered list of projects (never <code>null</code>).
     */
    public List<ProjectDTOLight> getOrderedList(Comparator<ProjectDTOLight> comparator) {
        final List<ProjectDTOLight> list = getList();
        Collections.sort(list, comparator);
        return list;
    }
}
