/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.client.page.project.dashboard;

import java.util.HashMap;

/**
 * Useful internal class to manage the required elements completions list.
 *
 * @author tmi
 *
 */
public class RequiredValueStateList {

    /**
     * Map the required element, its saved value completion (in db) and its
     * actual value completion (not yet saved).
     */
    private final HashMap<Integer, RequiredValueState> list;

    public RequiredValueStateList() {
        list = new HashMap<Integer, RequiredValueState>();
    }

    /**
     * Clears the list of required elements.
     */
    public void clear() {
        list.clear();
    }

    /**
     * Copies a list of required elements from another manager.
     *
     * @param other
     *            The other required elements manager.
     */
    public void putAll(RequiredValueStateList other) {
        list.putAll(other.list);
    }

    /**
     * Sets a required element saved value completion (in db). Adds it if
     * necessary.
     *
     * @param elementDTOId
     *            The required element id.
     * @param savedState
     *            The saved value completion.
     */
    public void putSaved(Integer elementDTOId, Boolean savedState) {

        RequiredValueState state = list.get(elementDTOId);

        if (state == null) {
            state = new RequiredValueState();
            list.put(elementDTOId, state);
        }

        state.setSavedState(savedState);
    }

    /**
     * Sets a required element actual value completion (in local). Adds it
     * if necessary.
     *
     * @param elementDTOId
     *            The required element id.
     * @param actualState
     *            The actual value completion.
     */
    public void putActual(Integer elementDTOId, Boolean actualState) {

        RequiredValueState state = list.get(elementDTOId);

        if (state == null) {
            state = new RequiredValueState();
            list.put(elementDTOId, state);
        }

        state.setActualState(actualState);
    }

    /**
     * Informs that all actual values completions has been saved to the data
     * layer.
     */
    public void saveState() {
        for (final RequiredValueState state : list.values()) {
            state.saveState();
        }
    }

    /**
     * Informs that all actual values completions has been discarded.
     */
    public void clearState() {
        for (final RequiredValueState state : list.values()) {
            state.clearState();
        }
    }

    /**
     * Returns if all saved values completions are valid.
     *
     * @return If all saved values completions are valid.
     */
    public boolean isTrue() {
        for (final RequiredValueState state : list.values()) {
            if (!state.isTrue()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
