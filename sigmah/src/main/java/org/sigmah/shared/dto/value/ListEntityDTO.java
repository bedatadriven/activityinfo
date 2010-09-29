/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.shared.dto.value;

import org.sigmah.shared.dto.EntityDTO;

/**
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public interface ListEntityDTO extends EntityDTO {

    /**
     * Returns a temporary index (or identifier) to identify this element on the
     * client-side before it is saved.
     * 
     * @return The unique index of this element.
     */
    public int getIndex();

    /**
     * Sets the temporary index of this element.
     * 
     * @param index
     *            The unique index of this element.
     */
    public void setIndex(int index);
}
