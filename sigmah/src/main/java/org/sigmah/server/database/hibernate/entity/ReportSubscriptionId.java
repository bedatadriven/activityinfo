/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.database.hibernate.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ReportSubscriptionId implements Serializable {

    private int reportId;
    private int userId;

    public ReportSubscriptionId() {
    }

    public ReportSubscriptionId(int reportId, int userId) {
        this.reportId = reportId;
        this.userId = userId;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
