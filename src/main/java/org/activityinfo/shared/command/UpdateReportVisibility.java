package org.activityinfo.shared.command;

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

import java.util.List;

import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.ReportVisibilityDTO;

public class UpdateReportVisibility implements MutatingCommand<VoidResult> {
    private int reportId;
    private List<ReportVisibilityDTO> list;

    public UpdateReportVisibility() {
    }

    public UpdateReportVisibility(int reportId, List<ReportVisibilityDTO> list) {
        super();
        this.reportId = reportId;
        this.list = list;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public List<ReportVisibilityDTO> getList() {
        return list;
    }

    public void setList(List<ReportVisibilityDTO> list) {
        this.list = list;
    }

}
