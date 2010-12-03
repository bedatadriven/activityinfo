/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.shared.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.element.handler.ValueEvent;
import org.sigmah.shared.dto.element.handler.ValueEvent.ChangeType;
import org.sigmah.shared.dto.element.handler.ValueEventWrapper;
import org.sigmah.shared.dto.value.ListEntityDTO;

/**
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class UpdateProject implements Command<VoidResult> {

    private static final long serialVersionUID = 3926814696490160032L;

    private int projectId;
    private ArrayList<ValueEventWrapper> values = new ArrayList<ValueEventWrapper>();

    public UpdateProject() {
    }

    public UpdateProject(int projectId, List<ValueEvent> values) {
        this.projectId = projectId;

        this.values.clear();

        final HashMap<Integer, ValueEvent> basicValues = new HashMap<Integer, ValueEvent>();
        final HashMap<ListEntityDTOKey, ValueEvent> listValues = new HashMap<ListEntityDTOKey, ValueEvent>();
        final HashMap<ListEntityDTOKey, ValueEvent> editedValues = new HashMap<ListEntityDTOKey, ValueEvent>();

        for (final ValueEvent event : values) {

            // Manages basic values changes.
            if (event.getListValue() == null) {
                // Keep only the last modification to avoid events repetition.
                basicValues.put(event.getSourceElement().getId(), event);
            }
            // Manages the elements which are a part of a list.
            else {
                final ListEntityDTO element = event.getListValue();

                // Manages only elements which are not stored on the data layer
                // to keep only the last state of each element before sending
                // events to the server.
                if (element.getId() == 0) {

                    switch (event.getChangeType()) {
                    case ADD:
                        listValues.put(new ListEntityDTOKey(event.getSourceElement().getId(), element.getIndex()),
                                event);
                        break;
                    case REMOVE:
                        listValues.remove(new ListEntityDTOKey(event.getSourceElement().getId(), element.getIndex()));
                        break;
                    case EDIT:
                        listValues.put(new ListEntityDTOKey(event.getSourceElement().getId(), element.getIndex()),
                                event);
                        break;
                    default:
                        break;
                    }
                } else {

                    // Keep only the last state of each edited element before
                    // sending events to the server.
                    switch (event.getChangeType()) {
                    case EDIT:
                        editedValues.put(new ListEntityDTOKey(event.getSourceElement().getId(), element.getIndex()),
                                event);
                        break;
                    default:
                        this.values.add(wrapEvent(event));
                        break;
                    }
                }
            }
        }

        for (ValueEvent event : basicValues.values()) {
            this.values.add(wrapEvent(event));
        }

        // Store each event for new elements as an 'add' event with the last
        // state of the element.
        for (ValueEvent event : listValues.values()) {
            this.values.add(wrapEvent(new ValueEvent(event.getSourceElement(), event.getListValue(), ChangeType.ADD)));
        }

        for (ValueEvent event : editedValues.values()) {
            this.values.add(wrapEvent(new ValueEvent(event.getSourceElement(), event.getListValue(), ChangeType.EDIT)));
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
        this.values.addAll(wrapEvents(values));
    }

    /**
     * Wraps a list of events.
     * 
     * @param events
     *            The events list.
     * @return The events wrapped list.
     */
    private static List<ValueEventWrapper> wrapEvents(List<ValueEvent> events) {
        final ArrayList<ValueEventWrapper> wrappers = new ArrayList<ValueEventWrapper>();
        for (ValueEvent event : events) {
            wrappers.add(wrapEvent(event));
        }
        return wrappers;
    }

    /**
     * Wraps a event.
     * 
     * @param event
     *            The event.
     * @return The event wrapped.
     */
    private static ValueEventWrapper wrapEvent(ValueEvent event) {
        final ValueEventWrapper wrapper = new ValueEventWrapper();
        wrapper.setSourceElement(event.getSourceElement());
        wrapper.setSingleValue(event.getSingleValue());
        wrapper.setListValue(event.getListValue());
        wrapper.setChangeType(event.getChangeType());
        return wrapper;
    }

    private static class ListEntityDTOKey {

        private int flexibleElement;
        private int index;

        public ListEntityDTOKey(int flexibleElement, int index) {
            super();
            this.flexibleElement = flexibleElement;
            this.index = index;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + flexibleElement;
            result = prime * result + index;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ListEntityDTOKey other = (ListEntityDTOKey) obj;
            if (flexibleElement != other.flexibleElement)
                return false;
            if (index != other.index)
                return false;
            return true;
        }

    }
}
