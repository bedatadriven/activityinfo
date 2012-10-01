package org.activityinfo.shared.dto;

import org.activityinfo.shared.report.model.Report;

public class ReportDTO implements DTO {

	private Report report;
	private ReportMetadataDTO reportMetadataDTO;

	public ReportDTO() {
	}

	public ReportDTO(Report report) {
		this.report = report;
	}

	public ReportDTO(Report report, ReportMetadataDTO reportMetadataDTO) {
		this.report = report;
		this.reportMetadataDTO = reportMetadataDTO;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public ReportMetadataDTO getReportMetadataDTO() {
		return reportMetadataDTO;
	}

	public void setReportMetadataDTO(ReportMetadataDTO reportMetadataDTO) {
		this.reportMetadataDTO = reportMetadataDTO;
	}
}
