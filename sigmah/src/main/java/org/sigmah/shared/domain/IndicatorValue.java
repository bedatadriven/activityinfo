/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.domain;


import javax.persistence.*;


/**
 * 
 * @author Alex Bertram
 *
 */
@Entity
@org.hibernate.annotations.Filter(
		name="hideDeleted",
		condition="(IndicatorId not in (select i.IndicatorId from Indicator i where i.dateDelete is not null))"
	)
public class IndicatorValue implements java.io.Serializable {

	private IndicatorValueId id;
	private Indicator indicator;
	private ReportingPeriod reportingPeriod;
	private Double value;

	public IndicatorValue() {
	}

	public IndicatorValue(IndicatorValueId id, Indicator indicator,
			ReportingPeriod reportingPeriod) {
		this.id = id;
		this.indicator = indicator;
		this.reportingPeriod = reportingPeriod;
	}

	public IndicatorValue(IndicatorValueId id, Indicator indicator,
			ReportingPeriod reportingPeriod, Double value) {
		this.id = id;
		this.indicator = indicator;
		this.reportingPeriod = reportingPeriod;
		this.value = value;
	}

	public IndicatorValue(ReportingPeriod period, Indicator indicator,
			double value) {
		
		this.id = new IndicatorValueId(period.getId(), indicator.getId());
		this.indicator = indicator;
		this.reportingPeriod = period;
		this.value = value;
	}


    @EmbeddedId
	@AttributeOverrides( {
		@AttributeOverride(name = "reportingPeriodId", column = @Column(name = "ReportingPeriodId", nullable = false)),
		@AttributeOverride(name = "indicatorId", column = @Column(name = "IndicatorId", nullable = false)) })
		public IndicatorValueId getId() {
		return this.id;
	}

	public void setId(IndicatorValueId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IndicatorId", nullable = false, insertable = false, updatable = false)
	public Indicator getIndicator() {
		return this.indicator;
	}

	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ReportingPeriodId", nullable = false, insertable = false, updatable = false)
	public ReportingPeriod getReportingPeriod() {
		return this.reportingPeriod;
	}

	public void setReportingPeriod(ReportingPeriod reportingPeriod) {
		this.reportingPeriod = reportingPeriod;
	}

	@Column(name = "Value", precision = 15, scale = 0, nullable=false)
	public Double getValue() {
		return this.value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object other) {
		if(this == other) {
            return true;
        }
		if(other == null) {
            return false;
        }
		if(!(other instanceof IndicatorValue)) {
            return false;
        }

		IndicatorValue that = (IndicatorValue)other;
		return this.getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

}