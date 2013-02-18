/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.ReportsResult;

public class GetReports extends GetListCommand<ReportsResult> {
    private Integer templateId;

    public GetReports() {
    }


    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public static GetReports byTemplateId(int id) {
        GetReports cmd = new GetReports();
        cmd.setTemplateId(id);

        return cmd;
    }
}
