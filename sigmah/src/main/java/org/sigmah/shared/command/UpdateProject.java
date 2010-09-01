/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.shared.command;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.element.FlexibleElementDTO;
import org.sigmah.shared.dto.element.handler.ValueEvent;
import org.sigmah.shared.dto.element.handler.ValueEventWrapper;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class UpdateProject implements Command<VoidResult> {
    private int projectId;
    private ArrayList<ValueEventWrapper> values = new ArrayList<ValueEventWrapper>();

    public UpdateProject() {
    }

    public UpdateProject(int projectId, List<ValueEvent> values) {
        this.projectId = projectId;
        final HashMap<FlexibleElementDTO, Serializable> edits = new HashMap<FlexibleElementDTO, Serializable>();
        for(final ValueEvent value : values) {
            // Merging edits
            if(value.getValue() != null && value.getChangeType() == ValueEvent.ChangeType.EDIT) {
                edits.put(value.getSourceElement(), value.getValue());
            } else {
                // Adding other events
                final ValueEventWrapper wrapper = new ValueEventWrapper();
                wrapper.setSourceElement(value.getSourceElement());
                wrapper.setValue(value.getValue());
                wrapper.setValues(value.getValues());
                wrapper.setChangeType(value.getChangeType());
                this.values.add(wrapper);
            }
        }

        // Adding the edit events to the list
        for(final Map.Entry<FlexibleElementDTO, Serializable> edit : edits.entrySet()) {
            final ValueEventWrapper wrapper = new ValueEventWrapper();
            wrapper.setSourceElement(edit.getKey());
            wrapper.setValue(edit.getValue());
            wrapper.setChangeType(ValueEvent.ChangeType.EDIT);
            this.values.add(wrapper);
        }
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public List<ValueEventWrapper> getValues() {
        return values;
    }

    public void setValues(List<ValueEvent> values) {
        this.values.clear();

        for(ValueEvent value : values) {
            final ValueEventWrapper wrapper = new ValueEventWrapper();
            wrapper.setSourceElement(value.getSourceElement());
            wrapper.setValue(value.getValue());
            wrapper.setValues(value.getValues());
            this.values.add(wrapper);
        }
    }
}
