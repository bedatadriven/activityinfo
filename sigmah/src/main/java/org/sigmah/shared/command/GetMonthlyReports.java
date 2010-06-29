/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import org.sigmah.shared.command.result.MonthlyReportResult;

/**
 * Returns {@link org.sigmah.shared.dto.IndicatorRowDTO} for a given site and for a given
 * range of months.
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
        this.endMonth = new Month(startMonth.getYear(), startMonth.getMonth()+monthCount-1);
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

