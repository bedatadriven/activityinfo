package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.PagingResult;
import org.activityinfo.shared.command.result.ReportTemplateResult;
import org.activityinfo.shared.dto.ReportTemplateDTO;

public class GetReportTemplates extends GetListCommand<ReportTemplateResult> {

    private Integer databaseId;
    private Integer templateId;

    public GetReportTemplates() {
    }


    public Integer getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Integer databaseId) {
        this.databaseId = databaseId;

    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public static GetReportTemplates byTemplateId(int id) {
        GetReportTemplates cmd = new GetReportTemplates();
        cmd.setTemplateId(id);

        return cmd;
    }




}
