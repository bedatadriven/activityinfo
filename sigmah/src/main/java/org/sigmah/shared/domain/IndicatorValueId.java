/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.domain;


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * @author Alex Bertram
 *
 */
@Embeddable
public class IndicatorValueId implements java.io.Serializable {

	private int reportingPeriodId;
	private int indicatorId;

	public IndicatorValueId() {
	}

	public IndicatorValueId(int reportingPeriodId, int indicatorId) {
		this.reportingPeriodId = reportingPeriodId;
		this.indicatorId = indicatorId;
	}

	@Column(name = "ReportingPeriodId", nullable = false)
	public int getReportingPeriodId() {
		return this.reportingPeriodId;
	}

	public void setReportingPeriodId(int reportingPeriodId) {
		this.reportingPeriodId = reportingPeriodId;
	}

	@Column(name = "IndicatorId", nullable = false)
	public int getIndicatorId() {
		return this.indicatorId;
	}

	public void setIndicatorId(int indicatorId) {
		this.indicatorId = indicatorId;
	}

	public boolean equals(Object other) {
		if ((this == other)) {
            return true;
        }
		if ((other == null)) {
            return false;
        }
		if (!(other instanceof IndicatorValueId)) {
            return false;
        }
		IndicatorValueId castOther = (IndicatorValueId) other;

		return (this.getReportingPeriodId() == castOther.getReportingPeriodId())
		&& (this.getIndicatorId() == castOther.getIndicatorId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getReportingPeriodId();
		result = 37 * result + this.getIndicatorId();
		return result;
	}

}