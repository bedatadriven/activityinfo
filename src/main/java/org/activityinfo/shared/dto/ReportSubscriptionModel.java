package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;
/*
 * @author Alex Bertram
 */

public class ReportSubscriptionModel extends BaseModelData {

    public ReportSubscriptionModel() {
    }

    public void setReportId(int reportId) {
        set("reportId", reportId);
    }

    public int getReportId() {
        return (Integer)get("reportId");
    }

    public void setReportName(String reportName) {
        set("reportName", reportName);
    }

    public String getReportName() {
        return get("reportName");
    }

    public void setFrequency(int frequency) {
        set("frequency", frequency);
    }

    public int getFrequency() {
        return (Integer)get("frequency");
    }

    public void setDay(int day) {
        set("day", day);
    }

    public int getDay() {
        return (Integer)get("day");
    }
}
