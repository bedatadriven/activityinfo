package org.sigmah.shared.domain.element;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.sigmah.shared.domain.category.CategoryType;
import org.sigmah.shared.domain.quality.QualityCriterion;

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
    private Boolean isMultiple = Boolean.FALSE;
    private List<QuestionChoiceElement> choices = new ArrayList<QuestionChoiceElement>();
    private CategoryType categoryType;

    public void setQualityCriterion(QualityCriterion qualityCriterion) {
        this.qualityCriterion = qualityCriterion;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_quality_criterion", nullable = true)
    public QualityCriterion getQualityCriterion() {
        return qualityCriterion;
    }

    @OneToMany(mappedBy = "parentQuestion", cascade = CascadeType.ALL)
    @OrderBy("sortOrder asc")
    public List<QuestionChoiceElement> getChoices() {
        return choices;
    }

    public void setChoices(List<QuestionChoiceElement> choices) {
        this.choices = choices;
    }

    @Column(name = "is_multiple", nullable = true)
    public Boolean getIsMultiple() {
        return isMultiple;
    }

    public void setIsMultiple(Boolean isMultiple) {
        this.isMultiple = isMultiple;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_category_type", nullable = true)
    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("QuestionElement");
        sb.append(" (");
        sb.append(super.toString());
        sb.append(", ");
        sb.append("choices -> ");

        int i = 1;
        for (final QuestionChoiceElement choice : choices) {
            sb.append("choice ");
            sb.append(i);
            sb.append(" (");
            sb.append(choice);
            sb.append("), ");
            i++;
        }

        sb.append(")");
        return sb.toString();
    }

    /**
     * Adds a choice to the current question.
     * 
     * @param label
     *            The choice's label.
     */
    public void addChoice(String label) {

        // Creates the choice and puts it at the end of the choices ordered
        // list.
        final QuestionChoiceElement choice = new QuestionChoiceElement();
        choice.setLabel(label);
        choice.setSortOrder(choices.size());

        choice.setParentQuestion(this);
        choices.add(choice);
    }

    @Override
    @Transient
    public boolean isHistorable() {
        return true;
    }
}
