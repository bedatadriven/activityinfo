package org.activityinfo.shared.command;

import org.activityinfo.shared.dto.ReportDTO;

/** 
 * Retrieves a report model from the server
 *
 */
public class GetReportModel implements Command<ReportDTO> {
	
	private Integer reportId;
	private boolean loadMetadata = false;
	
	public GetReportModel(){
	}

	public GetReportModel(boolean loadMetadata) {
		this.loadMetadata = loadMetadata;
	}

	public GetReportModel(Integer reportId) {
		this.reportId = reportId;
	}

	public GetReportModel(Integer reportId, boolean loadMetadata) {
		this.reportId = reportId;
		this.loadMetadata = loadMetadata;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public boolean isLoadMetadata() {
		return loadMetadata;
	}

	public void setLoadMetadata(boolean loadMetadata) {
		this.loadMetadata = loadMetadata;
	}
}
