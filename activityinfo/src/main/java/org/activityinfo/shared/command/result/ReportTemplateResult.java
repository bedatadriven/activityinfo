package org.activityinfo.shared.command.result;

import org.activityinfo.shared.dto.ReportDefinitionDTO;

import java.util.List;

/**
 * 
 * @author Alex Bertram
 */
public class ReportTemplateResult extends ListResult<ReportDefinitionDTO> {
    
    public ReportTemplateResult() {
    }

    public ReportTemplateResult(List<ReportDefinitionDTO> data) {
        super(data);
    }

}