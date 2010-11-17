/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import org.sigmah.shared.domain.report.ProjectReport;
import org.sigmah.shared.domain.report.ProjectReportModel;
import org.sigmah.shared.domain.report.RichTextElement;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public interface ProjectReportDAO {
    void persist(ProjectReport report);
    void merge(RichTextElement element);

    public ProjectReportModel findModelById(Integer id);
    public RichTextElement findRichTextElementById(Integer id);
}
