package org.sigmah.server.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;
/*
 * @author Alex Bertram
 */

@Embeddable
public class ReportSubscriptionId implements Serializable {

    private int reportTemplateId;
    private int userId;

    public ReportSubscriptionId() {
    }

    public ReportSubscriptionId(int reportTemplateId, int userId) {
        this.reportTemplateId = reportTemplateId;
        this.userId = userId;
    }

    public int getReportTemplateId() {
        return reportTemplateId;
    }

    public void setReportTemplateId(int reportTemplateId) {
        this.reportTemplateId = reportTemplateId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
