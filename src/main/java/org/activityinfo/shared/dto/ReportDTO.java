package org.activityinfo.shared.dto;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
