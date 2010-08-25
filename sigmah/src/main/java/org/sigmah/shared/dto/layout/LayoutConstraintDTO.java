/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.layout;

import org.sigmah.shared.dto.EntityDTO;
import org.sigmah.shared.dto.element.FlexibleElementDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 * 
 */
public class LayoutConstraintDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 8520711106031085130L;

    @Override
    public String getEntityName() {
        // Gets the entity name mapped by the current DTO starting from the
        // "server.domain" package name.
        return "layout.LayoutConstraint";
    }

    // Layout group constraint id
    @Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    // Sort order
    public int getSortOrder() {
        return (Integer) get("sortOrder");
    }

    public void setSortOrder(int sortOrder) {
        set("sortOrder", sortOrder);
    }

    // Reference to the layout group parent
    public LayoutGroupDTO getParentLayoutGroupDTO() {
        return get("parentLayoutGroupDTO");
    }

    public void setParentLayoutGroupDTO(LayoutGroupDTO parentLayoutGroupDTO) {
        set("parentLayoutGroupDTO", parentLayoutGroupDTO);
    }

    // Reference to the flexible element
    public FlexibleElementDTO getFlexibleElementDTO() {
        return get("flexibleElementDTO");
    }

    public void setFlexibleElementDTO(FlexibleElementDTO flexibleElementDTO) {
        set("flexibleElementDTO", flexibleElementDTO);
    }

}
