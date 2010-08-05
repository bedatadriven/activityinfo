package org.sigmah.server.domain.element;

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
 * Question choice element entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "question_choice_element")
public class QuestionChoiceElement implements Serializable {

	private static final long serialVersionUID = 8162125961144891315L;

	private Long id;
	private QuestionElement parentQuestion;
	private String label;
	private int sortOrder;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_choice")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_question", nullable = false)
	public QuestionElement getParentQuestion() {
		return parentQuestion;
	}

	public void setParentQuestion(QuestionElement parentQuestion) {
		this.parentQuestion = parentQuestion;
	}

	@Column(name = "label", nullable = false, length = 2048)
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Column(name = "sort_order", nullable = true)
	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("id -> ");
		sb.append(id);
		sb.append(", ");
		sb.append("label -> ");
		sb.append(label);
		sb.append(", ");
		sb.append("parent -> ");
		sb.append(parentQuestion != null ? parentQuestion.getId() : "null");
		sb.append(", ");
		sb.append("order -> ");
		sb.append(sortOrder);
		return sb.toString();
	}
}
