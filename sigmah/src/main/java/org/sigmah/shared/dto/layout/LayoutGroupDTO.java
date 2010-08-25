/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.layout;

import java.util.List;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 * 
 */
public class LayoutGroupDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 8520711106031085130L;

    @Override
    public String getEntityName() {
        // Gets the entity name mapped by the current DTO starting from the
        // "server.domain" package name.
        return "layout.LayoutGroup";
    }

    // Layout group id
    @Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    // Layout group title
    public String getTitle() {
        return get("title");
    }

    public void setTitle(String title) {
        set("title", title);
    }

    // Row index
    public int getRow() {
        return (Integer) get("row");
    }

    public void setRow(int row) {
        set("row", row);
    }

    // Column index
    public int getColumn() {
        return (Integer) get("column");
    }

    public void setColumn(int column) {
        set("column", column);
    }

    // Reference to layoutDTO
    public LayoutDTO getParentLayoutDTO() {
        return get("parentLayoutDTO");
    }

    public void setParentLayoutDTO(LayoutDTO parentLayoutDTO) {
        set("parentLayoutDTO", parentLayoutDTO);
    }

    // Reference to layout group constraints list
    public List<LayoutConstraintDTO> getLayoutConstraintsDTO() {
        return get("layoutConstraintsDTO");
    }

    public void setLayoutConstraintsDTO(List<LayoutConstraintDTO> layoutConstraintsDTO) {
        set("layoutConstraintsDTO", layoutConstraintsDTO);
    }

    public Widget getWidget() {
        FieldSet fieldSet = new FieldSet();
        fieldSet.setHeading(getTitle());

        FormPanel formPanel = new FormPanel();
        formPanel.addStyleName(I18N.CONSTANTS.projectPhaseFormStyle());
        formPanel.setHeaderVisible(false);
        formPanel.setBorders(false);
        formPanel.setBodyBorder(false);
        formPanel.setPadding(0);
        formPanel.setLabelWidth(150);

        fieldSet.add(formPanel);
        return fieldSet;
    }

}
