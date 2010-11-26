/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.element;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.dto.EntityDTO;
import org.sigmah.shared.dto.element.handler.RequiredValueEvent;
import org.sigmah.shared.dto.element.handler.RequiredValueHandler;
import org.sigmah.shared.dto.element.handler.ValueEvent;
import org.sigmah.shared.dto.element.handler.ValueHandler;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.event.shared.HandlerManager;
import org.sigmah.client.EventBus;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 * 
 */
public abstract class FlexibleElementDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 8520711106031085130L;

    protected transient HandlerManager handlerManager;

    protected transient Dispatcher dispatcher;

    protected transient EventBus eventBus;

    protected transient Authentication authentication;

    protected transient FlexibleElementContainer currentContainerDTO;

    protected transient int preferredWidth;

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
     * Sets the event bus to be used by {@link #getComponent(ValueResult)}.
     * @param eventBus
     *          The presenter's event bus.
     */
    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    /**
     * Sets the authentication provider to be used in the
     * {@link #getComponent(ValueResult)} method.
     * 
     * @param authentication
     *            The authentication provider.
     */
    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    /**
     * Sets the current container (not model, but instance) using this flexible
     * element to be used in the {@link #getComponent(ValueResult)} method.
     * 
     * @param currentContainerDTO
     *            The current container using this flexible element.
     */
    public void setCurrentContainerDTO(FlexibleElementContainer currentContainerDTO) {
        this.currentContainerDTO = currentContainerDTO;
    }

    /**
     * Method called just before the
     * {@link FlexibleElementDTO#getComponent(ValueResult)} method to ensure the
     * instantiation of the attributes used by the client-side.<br/>
     * This method can be override by subclasses.
     */
    public void init() {

        // Checks preconditions.
        assert dispatcher != null;
        assert authentication != null;
        assert currentContainerDTO != null;
        handlerManager = new HandlerManager(this);
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
    public Component getComponent(ValueResult valueResult) {
        return getComponent(valueResult, true);
    }

    /**
     * Gets the widget of a flexible element with its value.
     * 
     * @param valueResult
     *            value of the flexible element, or {@code null} to display the
     *            element without its value.
     * @param enabled
     *            If the component is enabled.
     * 
     * @return the widget corresponding to the flexible element.
     */
    public abstract Component getComponent(ValueResult valueResult, boolean enabled);

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

    /**
     * Gets the most adapted width to display this component.
     * 
     * @return The preferred width of this element.
     */
    public int getPreferredWidth() {
        return preferredWidth;
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

    public boolean isFilledIn() {
        return (Boolean) get("filledIn");
    }

    public void setFilledIn(boolean filledIn) {
        set("filledIn", filledIn);
    }

    /**
     * Assigns a value to a flexible element.
     * 
     * @param result
     *            The value.
     */
    public void assignValue(ValueResult result) {
        setFilledIn(isCorrectRequiredValue(result));
    }

    /**
     * Returns if a value can be considered as a correct required value for this
     * specific flexible element.
     * 
     * @param result
     *            The value.
     * @return If the value can be considered as a correct required value.
     */
    public abstract boolean isCorrectRequiredValue(ValueResult result);
}
