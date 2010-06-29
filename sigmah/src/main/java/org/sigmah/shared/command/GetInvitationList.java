/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import org.sigmah.shared.command.result.InvitationList;
/*
 * @author Alex Bertram
 */

public class GetInvitationList extends PagingGetCommand<InvitationList> {

    private int reportTemplateId;

    public GetInvitationList() {

    }

    public GetInvitationList(int reportTemplateId) {
        this.reportTemplateId = reportTemplateId;
    }

    public int getReportTemplateId() {
        return reportTemplateId;
    }

    public void setReportTemplateId(int reportTemplateId) {
        this.reportTemplateId = reportTemplateId;
    }
}
