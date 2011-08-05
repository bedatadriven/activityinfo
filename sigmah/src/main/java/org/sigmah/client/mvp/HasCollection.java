package org.sigmah.client.mvp;

import org.sigmah.shared.dto.DTO;

/*
 * This won't really work since Java does not support reified generics.
 * A method such as List<M> getItems() only works if the parent has one collection
 * of child elements, but not when the parent has multiple collections of child elements.
 * 
 * The erased types of List<SomeType> getItems() and List<SomeOtherType> getItems() 
 * would render the same method signature.
 * 
 * A solution would be some kind of (double) dispatch mechanism
 */
public interface HasCollection<M extends DTO> {
}
