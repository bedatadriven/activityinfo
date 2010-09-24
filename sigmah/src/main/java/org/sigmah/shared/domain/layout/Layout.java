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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.sigmah.shared.domain.element.FlexibleElement;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "layout")
public class Layout implements Serializable {

	private static final long serialVersionUID = 3567671639080023704L;

	private Long id;
	private Integer rowsCount;
	private Integer columnsCount;
	private List<LayoutGroup> groups = new ArrayList<LayoutGroup>();

	public Layout() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_layout")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setRowsCount(Integer rowsCount) {
		this.rowsCount = rowsCount;
	}

	@Column(name = "rows_count", nullable = false)
	public Integer getRowsCount() {
		return rowsCount;
	}

	public void setColumnsCount(Integer columnsCount) {
		this.columnsCount = columnsCount;
	}

	@Column(name = "columns_count", nullable = false)
	public Integer getColumnsCount() {
		return columnsCount;
	}

	public void setGroups(List<LayoutGroup> groups) {
		this.groups = groups;
	}

	@OneToMany(mappedBy = "parentLayout", cascade = CascadeType.ALL)
	public List<LayoutGroup> getGroups() {
		return groups;
	}

	/**
	 * Creates a {@link LayoutGroup} for each cell generated from the given numbers of rows and columns.
	 *
	 * @param rows
	 *            The number of rows in the layout.
	 * @param cols
	 *            The number of columns in the layout.
	 */
	public Layout(int rows, int cols) {
		rowsCount = rows;
		columnsCount = cols;

		for (int row = 0; row < rowsCount; row++) {
			for (int col = 0; col < columnsCount; col++) {

				final LayoutGroup group = new LayoutGroup();
				group.setRow(row);
				group.setColumn(col);
				group.setTitle("Group " + groups.size());

				group.setParentLayout(this);
				groups.add(group);
			}
		}
	}

	/**
	 * Adds a constraint to position an element in a current layout's group.
	 *
	 * @param row
	 *            The row of the group.
	 * @param col
	 *            The column of the group
	 * @param elem
	 *            The element constrained.
	 * @param order
	 *            The constraint.
	 */
	public void addConstraint(int row, int col, FlexibleElement elem, int order) {

		// Checks cell index constraints.
		if (row < 0 || row > rowsCount || col < 0 || col > columnsCount) {
			return;
		}

		// Creates the constraint.
		final LayoutConstraint constraint = new LayoutConstraint();
		constraint.setElement(elem);
		constraint.setSortOrder(order);

		// Adds it to the correct group.
		for (final LayoutGroup group : groups) {
			if (group.getRow() == row && group.getColumn() == col) {
				group.addConstraint(constraint);
				return;
			}
		}
	}
}
