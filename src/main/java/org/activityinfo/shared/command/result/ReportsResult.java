/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command.result;

import java.util.List;

import org.activityinfo.shared.dto.ReportMetadataDTO;

/**
 * 
 * @author Alex Bertram
 */
public class ReportsResult extends ListResult<ReportMetadataDTO> {
    
    public ReportsResult() {
    }

    public ReportsResult(List<ReportMetadataDTO> data) {
        super(data);
    }

	public ReportMetadataDTO forId(int id) {
		for(ReportMetadataDTO report : getData()) {
			if(report.getId() == id) {
				return report;
			}
		}
		throw new IllegalArgumentException();
	}

}