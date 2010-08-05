package org.sigmah.server.domain.value;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "budget_part")
public class BudgetPart implements Serializable {

	private static final long serialVersionUID = 3320704577030726087L;

	private Long id;
	private BudgetPartsListValue parentBugetList;
	private Float amount;
	private String label;

	public void setId(Long id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_budget_part")
	public Long getId() {
		return id;
	}

	@Column(name = "amount", nullable = false, precision = 2)
	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_budget_parts_list", nullable = false)
	public BudgetPartsListValue getParentBugetList() {
		return parentBugetList;
	}

	public void setParentBugetList(BudgetPartsListValue parentBugetList) {
		this.parentBugetList = parentBugetList;
	}

	@Column(name = "label", nullable = false, length = 2048)
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
