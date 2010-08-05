package org.sigmah.server.domain.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "budget_parts_list_value")
public class BudgetPartsListValue implements Serializable {

	private static final long serialVersionUID = 8555153050650002232L;

	private Long id;
	private Budget budget;
	private List<BudgetPart> parts = new ArrayList<BudgetPart>();

	public void setId(Long id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_budget_parts_list")
	public Long getId() {
		return id;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_budget", nullable = false)
	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public void setParts(List<BudgetPart> parts) {
		this.parts = parts;
	}

	@OneToMany(mappedBy = "parentBugetList", cascade = CascadeType.ALL)
	public List<BudgetPart> getParts() {
		return parts;
	}

	public void addBudgetPart(BudgetPart part) {
		part.setParentBugetList(this);
		parts.add(part);
	}
}
