/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.layout;

import java.util.List;

import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 * 
 */
public class LayoutDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 8520711106031085130L;

    @Override
    public String getEntityName() {
        // Gets the entity name mapped by the current DTO starting from the
        // "server.domain" package name.
        return "layout.Layout";
    }

    // Layout id
    @Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    // Rows count
    public int getRowsCount() {
        return (Integer) get("rowsCount");
    }

    public void setRowsCount(int rowsCount) {
        set("rowsCount", rowsCount);
    }

    // Columns count
    public int getColumnsCount() {
        return (Integer) get("columnsCount");
    }

    public void setColumnsCount(int columnsCount) {
        set("columnsCount", columnsCount);
    }

    // Reference to layout groups list
    public List<LayoutGroupDTO> getLayoutGroupsDTO() {
        return get("layoutGroupsDTO");
    }

    public void setLayoutGroupsDTO(List<LayoutGroupDTO> layoutGroupsDTO) {
        set("layoutGroupsDTO", layoutGroupsDTO);
    }

    public Widget getWidget() {
        final Grid grid = new Grid(getRowsCount(), getColumnsCount());
        grid.setStyleName("flexibility-layout");
        return grid;
    }

}
