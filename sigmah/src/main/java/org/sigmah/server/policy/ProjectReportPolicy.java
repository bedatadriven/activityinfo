/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.policy;

import org.sigmah.server.dao.Transactional;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.report.ProjectReport;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ProjectReportPolicy implements EntityPolicy<ProjectReport> {

    @Override
    public Object create(User user, PropertyMap properties) {
        return createReport().getId();
    }

    @Transactional
    public ProjectReport createReport() {
        return null;
    }

    @Override
    public void update(User user, Object entityId, PropertyMap changes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
