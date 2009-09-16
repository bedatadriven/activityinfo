package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.VoidResult;
/*
 * @author Alex Bertram
 */

public class UpdateSubscription implements Command<VoidResult> {

    int reportTemplateId;
    int frequency;
    int day;

    Integer userId;

    public UpdateSubscription() {
    }

    public UpdateSubscription(int reportTemplateId, int frequency, int day) {
        this.reportTemplateId = reportTemplateId;
        this.frequency = frequency;
        this.day = day;
    }

    public int getReportTemplateId() {
        return reportTemplateId;
    }

    public void setReportTemplateId(int reportTemplateId) {
        this.reportTemplateId = reportTemplateId;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
