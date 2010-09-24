package org.sigmah.shared.domain.layout;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.sigmah.shared.domain.element.FlexibleElement;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "layout_constraint")
public class LayoutConstraint implements Serializable {

	private static final long serialVersionUID = -5150783265586227961L;

	private Long id;
	private LayoutGroup parentLayoutGroup;
	private FlexibleElement element;
	private Integer sortOrder;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_layout_constraint")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setParentLayoutGroup(LayoutGroup parentLayoutGroup) {
		this.parentLayoutGroup = parentLayoutGroup;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_layout_group", nullable = false)
	public LayoutGroup getParentLayoutGroup() {
		return parentLayoutGroup;
	}

	public void setElement(FlexibleElement element) {
		this.element = element;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_flexible_element", nullable = false)
	public FlexibleElement getElement() {
		return element;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Column(name = "sort_order", nullable = true)
	public Integer getSortOrder() {
		return sortOrder;
	}
}
