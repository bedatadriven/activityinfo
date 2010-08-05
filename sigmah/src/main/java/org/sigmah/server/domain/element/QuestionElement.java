package org.sigmah.server.domain.element;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.sigmah.server.domain.QualityCriterion;

/**
 * Question element entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "question_element")
public class QuestionElement extends FlexibleElement {

	private static final long serialVersionUID = 2228874550756274138L;

	private QualityCriterion qualityCriterion;
	private List<QuestionChoiceElement> choices = new ArrayList<QuestionChoiceElement>();

	public void setQualityCriterion(QualityCriterion qualityCriterion) {
		this.qualityCriterion = qualityCriterion;
	}

	@ManyToOne(optional = true)
	@JoinColumn(name = "id_quality_criterion", nullable = true)
	public QualityCriterion getQualityCriterion() {
		return qualityCriterion;
	}

	@OneToMany(mappedBy = "parentQuestion", cascade = CascadeType.ALL)
	public List<QuestionChoiceElement> getChoices() {
		return choices;
	}

	public void setChoices(List<QuestionChoiceElement> choices) {
		this.choices = choices;
	}

//	@Override
//	public String toString() {
//		final StringBuilder sb = new StringBuilder();
//		sb.append(this.getClass().getSimpleName());
//		sb.append(" (");
//		sb.append(super.toString());
//		sb.append(", ");
//		sb.append("choices -> ");
//
//		int i = 1;
//		for (final QuestionChoiceElement choice : choices) {
//			sb.append("choice ");
//			sb.append(i);
//			sb.append(" (");
//			sb.append(choice);
//			sb.append("), ");
//			i++;
//		}
//
//		sb.append(")");
//		return sb.toString();
//	}

	/**
	 * Adds a choice to the current question.
	 *
	 * @param label
	 *            The choice's label.
	 */
	public void addChoice(String label) {

		// Creates the choice and puts it at the end of the choices ordered list.
		final QuestionChoiceElement choice = new QuestionChoiceElement();
		choice.setLabel(label);
		choice.setSortOrder(choices.size());

		choice.setParentQuestion(this);
		choices.add(choice);
	}

}
