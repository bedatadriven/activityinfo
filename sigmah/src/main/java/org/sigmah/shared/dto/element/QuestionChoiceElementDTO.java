/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.element;

import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 * 
 */
public class QuestionChoiceElementDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 8520711106031085130L;

    @Override
    public String getEntityName() {
        // Gets the entity name mapped by the current DTO starting from the
        // "server.domain" package name.
        return "element.QuestionChoiceElement";
    }

    // Question choice id
    @Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    // Question choice label
    public String getLabel() {
        return get("label");
    }

    public void setLabel(String label) {
        set("label", label);
    }

    // Question choice sort order
    public int getSortOrder() {
        return (Integer) get("sortOrder");
    }

    public void setSortOrder(int sortOrder) {
        set("sortOrder", sortOrder);
    }

    // Reference to the parent question element
    public QuestionElementDTO getParentQuestionDTO() {
        return get("parentQuestionDTO");
    }

    public void setParentQuestionDTO(QuestionElementDTO parentQuestionDTO) {
        set("parentQuestionDTO", parentQuestionDTO);
    }

    @Override
    public String toString() {
        return Integer.toString(getId());
    }
}
