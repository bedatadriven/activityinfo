package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.InvitationList;
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
