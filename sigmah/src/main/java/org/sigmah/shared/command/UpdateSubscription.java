package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;

/**
 * Updates the frequency with which a given report is mailed
 * to a given user.
 *
 * Normally, only the users themselves are permitted to change
 * subscription preferences. However, the owner of a report can
 * "invite" other users to subscribe to their report, if the user
 * has not already set a subscription preference.
 *
 *
 * @author Alex Bertram
 */
public class UpdateSubscription implements Command<VoidResult> {

    int reportTemplateId;
    boolean subscribed;

    Integer userId;

    public UpdateSubscription() {
    }

    public UpdateSubscription(int reportTemplateId, boolean subscribed) {
        this.reportTemplateId = reportTemplateId;
        this.subscribed = subscribed;
    }

    public int getReportTemplateId() {
        return reportTemplateId;
    }

    public void setReportTemplateId(int reportTemplateId) {
        this.reportTemplateId = reportTemplateId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
