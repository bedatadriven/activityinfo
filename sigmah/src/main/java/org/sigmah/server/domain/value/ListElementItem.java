/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.server.domain.value;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public interface ListElementItem {
    Long getId();
    void setId(Long id);
    
    Long getIdList();
    void setIdList(Long id);

    /**
     * This method should return true when the other object looks like it is the same as this one.
     * This method should not take in account the id nor the idList of both objects if one of them is null.
     * @param e An other object.
     * @return true if both objects have the same core values, false otherwise.
     */
    boolean isLike(ListElementItem e);
}
