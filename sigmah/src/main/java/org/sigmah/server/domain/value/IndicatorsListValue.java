package org.sigmah.server.domain.value;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.sigmah.shared.domain.Indicator;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "indicators_list_value")
public class IndicatorsListValue implements Serializable {

	private static final long serialVersionUID = -8267821835924810690L;

	private IndicatorsListValueId id;
	private Long idList;
	private Indicator indicator;

	@EmbeddedId
	@AttributeOverrides({
							@AttributeOverride(name = "idList", column = @Column(name = "id_indicators_list", nullable = false)),
							@AttributeOverride(name = "indicatorId", column = @Column(name = "id_indicator", nullable = false)) })
	public IndicatorsListValueId getId() {
		return this.id;
	}

	public void setId(IndicatorsListValueId id) {
		this.id = id;
	}

	public void setIdList(Long id) {
		this.idList = id;
	}

	@Column(name = "id_indicators_list", nullable = false, insertable = false, updatable = false)
	public Long getIdList() {
		return idList;
	}

	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_indicator", nullable = false, insertable = false, updatable = false)
	public Indicator getIndicator() {
		return indicator;
	}
}
