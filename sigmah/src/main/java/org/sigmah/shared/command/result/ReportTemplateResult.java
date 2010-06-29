/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

import org.sigmah.shared.dto.ReportDefinitionDTO;

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