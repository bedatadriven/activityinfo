/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.element;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.dto.EntityDTO;
import org.sigmah.shared.dto.ProjectModelDTO;
import org.sigmah.shared.dto.element.handler.RequiredValueEvent;
import org.sigmah.shared.dto.element.handler.RequiredValueHandler;
import org.sigmah.shared.dto.element.handler.ValueEvent;
import org.sigmah.shared.dto.element.handler.ValueHandler;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.event.shared.HandlerManager;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 * 
 */
public abstract class FlexibleElementDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 8520711106031085130L;

    protected final HandlerManager handlerManager = new HandlerManager(this);

    protected transient Dispatcher dispatcher;

    /**
     * Sets the dispatcher to be used in the {@link #getComponent(ValueResult)}
     * method.
     * 
     * @param dispatcher
     *            The presenter's dispatcher.
     */
    public void setService(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * Gets the widget of a flexible element with its value.
     * 
     * @param valueResult
     *            value of the flexible element, or {@code null} to display the
     *            element without its value.
     * 
     * @return the widget corresponding to the flexible element.
     */
    public abstract Component getComponent(ValueResult valueResult);

    /**
     * Adds a {@link ValueHandler} to the flexible element.
     * 
     * @param handler
     *            a {@link ValueHandler} object
     */
    public void addValueHandler(ValueHandler handler) {
        handlerManager.addHandler(ValueEvent.getType(), handler);
    }

    /**
     * Adds a {@link RequiredValueHandler} to the flexible element.
     * 
     * @param handler
     *            a {@link RequiredValueHandler} object
     */
    public void addRequiredValueHandler(RequiredValueHandler handler) {
        handlerManager.addHandler(RequiredValueEvent.getType(), handler);
    }

    // Flexible element id
    @Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    // Flexible element label
    public String getLabel() {
        return get("label");
    }

    public void setLabel(String label) {
        set("label", label);
    }

    // Flexible element validates
    public boolean getValidates() {
        return (Boolean) get("validates");
    }

    public void setValidates(boolean validates) {
        set("validates", validates);
    }

    // Reference to the parent project model
    public ProjectModelDTO getParentProjectModelDTO() {
        return get("parentProjectModelDTO");
    }

    public void setParentProjectModelDTO(ProjectModelDTO parentProjectModelDTO) {
        set("parentProjectModelDTO", parentProjectModelDTO);
    }

    /**
     * Handles the required flexible elements by adding a specific css style to
     * the widget. This method is called by the sub classes.
     * 
     * @return the flexible elements label style.
     */
    protected String getLabelStyle() {
        String labelStyle = "sigmah-element-label";
        if (getValidates()) {
            labelStyle += " required";
        }
        return labelStyle;
    }

    public boolean isFilledIn() {
        return (Boolean) get("filledIn");
    }

    public void setFilledIn(boolean filledIn) {
        set("filledIn", filledIn);
    }
}
