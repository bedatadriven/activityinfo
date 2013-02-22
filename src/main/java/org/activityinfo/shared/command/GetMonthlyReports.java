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

import org.activityinfo.shared.command.result.MonthlyReportResult;

/**
 * Returns {@link org.activityinfo.shared.dto.IndicatorRowDTO} for a given site
 * and for a given range of months.
 * 
 * @author Alex Bertram
 */
public class GetMonthlyReports extends GetListCommand<MonthlyReportResult> {

    private int siteId;
    private Month startMonth;
    private Month endMonth;

    public GetMonthlyReports() {
    }

    public GetMonthlyReports(int siteId) {
        this.siteId = siteId;
    }

    public GetMonthlyReports(int siteId, Month startMonth, int monthCount) {
        this.siteId = siteId;
        this.startMonth = startMonth;
        this.endMonth = new Month(startMonth.getYear(), startMonth.getMonth()
            + monthCount - 1);
    }

    public GetMonthlyReports(int siteId, Month startMonth, Month endMonth) {
        this.siteId = siteId;
        this.startMonth = startMonth;
        this.endMonth = endMonth;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public Month getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(Month startMonth) {
        this.startMonth = startMonth;
    }

    public Month getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(Month endMonth) {
        this.endMonth = endMonth;
    }
}
