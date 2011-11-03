/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.database.hibernate.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

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
