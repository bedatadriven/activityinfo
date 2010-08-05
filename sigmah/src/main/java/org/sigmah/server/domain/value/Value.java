package org.sigmah.server.domain.value;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.sigmah.server.domain.Project;
import org.sigmah.server.domain.User;
import org.sigmah.server.domain.element.CheckboxElement;
import org.sigmah.server.domain.element.FlexibleElement;
import org.sigmah.server.domain.element.TextAreaElement;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "value", uniqueConstraints = { @UniqueConstraint(columnNames = {
																				"id_flexible_element",
																				"id_project" }) })
public class Value implements Serializable {

	private static final long serialVersionUID = -2578586689736955636L;

	private Long id;
	private FlexibleElement element;
	private Project parentProject;
	private String value;
	private User lastModificationUser;
	private Date lastModificationDate;
	private Character lastModificationAction;

	public void setId(Long id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_value")
	public Long getId() {
		return id;
	}

	public void setElement(FlexibleElement element) {
		this.element = element;
	}

	@OneToOne(optional = false)
	@JoinColumn(name = "id_flexible_element", nullable = false)
	public FlexibleElement getElement() {
		return element;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "value", nullable = true, length = 8192)
	public String getValue() {
		return value;
	}

	public void setParentProject(Project parentProject) {
		this.parentProject = parentProject;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_project", nullable = false)
	public Project getParentProject() {
		return parentProject;
	}

	public void setLastModificationUser(User lastModificationUser) {
		this.lastModificationUser = lastModificationUser;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_user_last_modif", nullable = false)
	public User getLastModificationUser() {
		return lastModificationUser;
	}

	public void setLastModificationDate(Date lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_last_modif", nullable = false)
	public Date getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationAction(Character lastModificationAction) {
		this.lastModificationAction = lastModificationAction;
	}

	@Column(name = "action_last_modif", nullable = false)
	public Character getLastModificationAction() {
		return lastModificationAction;
	}

//	@Override
//	public String toString() {
//		final StringBuilder sb = new StringBuilder();
//		sb.append(this.getClass().getSimpleName());
//		sb.append(" (");
//		sb.append("id -> ");
//		sb.append(id);
//		sb.append(", ");
//		sb.append("element -> ");
//		sb.append(element != null ? element.getId() : "null");
//		sb.append(", ");
//		sb.append("phase -> ");
//		sb.append(parentProject != null ? parentProject.getId() : "null");
//		sb.append(", ");
//		sb.append("value -> ");
//		sb.append(value);
//		sb.append(")");
//
//		// Displays the value if it is a complex type.
//		if (element instanceof QuestionElement) {
//
//			final QuestionChoiceElement choice = DummyService.getQuestionChoiceElement(Long.valueOf(value));
//
//			sb.append(" ==> ");
//			sb.append(choice.getLabel());
//		} else if (element instanceof FilesListElement) {
//
//			final List<FilesListValue> values = DummyService.getFilesListValue(Long.valueOf(value));
//
//			sb.append(" ==> ");
//			sb.append(FilesListValue.class.getSimpleName());
//			sb.append(" (");
//			sb.append(values != null ? values.size() : 0);
//			sb.append(") (");
//
//			if (values != null) {
//				for (final FilesListValue value : values) {
//					final File file = value.getFile();
//					sb.append(File.class.getSimpleName());
//					sb.append(" (");
//					sb.append("name -> ");
//					sb.append(file.getName());
//					sb.append(", ");
//					sb.append("versions -> ");
//					sb.append(file.getVersions().size());
//					sb.append("), ");
//				}
//			}
//			sb.append(")");
//		} else if (element instanceof TripletsListElement) {
//
//			final List<TripletValue> values = DummyService.getTripletsListValue(Long.valueOf(value));
//
//			sb.append(" ==> ");
//			sb.append(TripletValue.class.getSimpleName());
//			sb.append(" (");
//			sb.append(values != null ? values.size() : 0);
//			sb.append(") (");
//
//			if (values != null) {
//				for (final TripletValue triplet : values) {
//					sb.append(" (");
//					sb.append("code -> ");
//					sb.append(triplet.getCode());
//					sb.append(", ");
//					sb.append("name -> ");
//					sb.append(triplet.getName());
//					sb.append(", ");
//					sb.append("period -> ");
//					sb.append(triplet.getPeriod());
//					sb.append("), ");
//				}
//			}
//			sb.append(")");
//		} else if (element instanceof IndicatorsListElement) {
//
//			final List<IndicatorsListValue> values = DummyService.getIndicatorsListValue(Long.valueOf(value));
//
//			sb.append(" ==> ");
//			sb.append(IndicatorsListValue.class.getSimpleName());
//			sb.append(" (");
//			sb.append(values != null ? values.size() : 0);
//			sb.append(") (");
//
//			if (values != null) {
//				for (final IndicatorsListValue value : values) {
//					final Indicator ind = value.getIndicator();
//					sb.append(Indicator.class.getSimpleName());
//					sb.append(" (");
//					sb.append("value -> ");
//					sb.append(ind.getValue());
//					sb.append(", ");
//					sb.append("quality -> ");
//					sb.append(ind.getQualityCriterion());
//					sb.append("), ");
//				}
//			}
//			sb.append(")");
//		} else if (element instanceof BudgetDistributionElement) {
//
//			final BudgetPartsListValue dist = DummyService.getBudgetPartsListValue(Long.valueOf(value));
//
//			sb.append(" ==> ");
//			sb.append(BudgetPartsListValue.class.getSimpleName());
//			sb.append(" (");
//			sb.append("budget id -> ");
//			sb.append(dist.getBudget().getId());
//			sb.append(", ");
//			sb.append("budget amount -> ");
//			sb.append(dist.getBudget().getAmount());
//			sb.append(") (");
//			sb.append(dist != null ? dist.getParts().size() : 0);
//			sb.append(") (");
//
//			if (dist != null) {
//				for (final BudgetPart part : dist.getParts()) {
//					sb.append(BudgetPart.class.getSimpleName());
//					sb.append(" (");
//					sb.append("label -> ");
//					sb.append(part.getLabel());
//					sb.append(", ");
//					sb.append("amount -> ");
//					sb.append(part.getAmount());
//					sb.append("), ");
//				}
//			}
//			sb.append(")");
//		}
//
//		return sb.toString();
//	}

	/**
	 * Returns if the value can be consider as <code>valid</code> (correctly filled) according to its type.
	 *
	 * @return if the value is valid.
	 */
	@Transient
	public boolean isValid() {

		if (!element.isValidates()) {
			return true;
		}

		if (element instanceof CheckboxElement) {
			if (value.equals("true")) {
				return true;
			}
		} else if (element instanceof TextAreaElement) {
			if (!value.equals("")) {
				return true;
			}
		}

		return false;
	}
}
