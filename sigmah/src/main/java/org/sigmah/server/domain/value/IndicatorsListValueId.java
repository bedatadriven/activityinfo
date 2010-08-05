package org.sigmah.server.domain.value;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Embeddable
public class IndicatorsListValueId implements java.io.Serializable {

	private static final long serialVersionUID = 7309173168011463617L;

	private Long idList;
	private int indicatorId;

	public IndicatorsListValueId() {
	}

	public IndicatorsListValueId(Long idList, int indicatorId) {
		this.idList = idList;
		this.indicatorId = indicatorId;
	}

	@Column(name = "id_indicators_list", nullable = false)
	public Long getIdList() {
		return this.idList;
	}

	public void setIdList(Long idList) {
		this.idList = idList;
	}

	@Column(name = "id_indicator", nullable = false)
	public int getIndicatorId() {
		return this.indicatorId;
	}

	public void setIndicatorId(int indicatorId) {
		this.indicatorId = indicatorId;
	}

}
