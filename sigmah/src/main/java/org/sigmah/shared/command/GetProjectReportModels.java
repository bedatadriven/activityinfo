/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import com.extjs.gxt.ui.client.data.BaseModelData;
import org.sigmah.shared.command.result.ProjectReportModelListResult;
import org.sigmah.shared.domain.report.ProjectReportModel;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class GetProjectReportModels implements Command<ProjectReportModelListResult> {
    public static class ModelReference extends BaseModelData {
        public ModelReference() {}

        public ModelReference(ProjectReportModel model) {
            this.set("id", model.getId());
            this.set("name", model.getName());
        }

        public Integer getId() {
            return get("id");
        }
        public void setId(Integer id) {
            this.set("id", id);
        }

        public String getName() {
            return get("name");
        }
        public void setName(String name) {
            this.set("name", name);
        }
    }


    public GetProjectReportModels() {}

}
