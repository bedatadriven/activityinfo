package org.activityinfo.shared.command.result;

import org.activityinfo.shared.dto.ReportTemplateDTO;

import java.util.List;

/**
 * 
 * @author Alex Bertram
 */
public class ReportTemplateResult extends ListResult<ReportTemplateDTO> {
    
    public ReportTemplateResult() {
    }

    public ReportTemplateResult(List<ReportTemplateDTO> data) {
        super(data);
    }

}