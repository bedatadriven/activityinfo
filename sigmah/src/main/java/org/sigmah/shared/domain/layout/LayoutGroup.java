package org.sigmah.shared.domain.layout;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "layout_group")
public class LayoutGroup implements Serializable {

	private static final long serialVersionUID = -5138315416849070907L;

	private Long id;
	private Layout parentLayout;
	private Integer row;
	private Integer column;
	private String title;
	private List<LayoutConstraint> constraints = new ArrayList<LayoutConstraint>();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_layout_group")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setParentLayout(Layout parentLayout) {
		this.parentLayout = parentLayout;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_layout", nullable = false)
	public Layout getParentLayout() {
		return parentLayout;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	@Column(name = "row_index", nullable = false)
	public Integer getRow() {
		return row;
	}

	public void setColumn(Integer column) {
		this.column = column;
	}

	@Column(name = "column_index", nullable = false)
	public Integer getColumn() {
		return column;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "title", nullable = true, length = 1024)
	public String getTitle() {
		return title;
	}

	public void setConstraints(List<LayoutConstraint> constraints) {
		this.constraints = constraints;
	}

	@OneToMany(mappedBy = "parentLayoutGroup", cascade = CascadeType.ALL)
	@OrderBy("sortOrder asc")
	public List<LayoutConstraint> getConstraints() {
		return constraints;
	}

	/**
	 * Adds a constraint to the current layout group if it is not null.
	 *
	 * @param constraint
	 *            The constraint to add.
	 */
	public void addConstraint(LayoutConstraint constraint) {

		if (constraint == null) {
			return;
		}

		constraint.setParentLayoutGroup(this);
		constraints.add(constraint);
	}
}
