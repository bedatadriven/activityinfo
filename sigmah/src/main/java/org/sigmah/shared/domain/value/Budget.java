package org.sigmah.shared.domain.value;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "budget")
public class Budget implements Serializable {

	private static final long serialVersionUID = 5862207206719474311L;

	private Long id;
	private Float amount;

	public void setId(Long id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_budget")
	public Long getId() {
		return id;
	}

	@Column(name = "total_amount", nullable = false, precision = 2)
	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}
}
