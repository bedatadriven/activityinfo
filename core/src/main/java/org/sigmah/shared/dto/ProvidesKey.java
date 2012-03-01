package org.sigmah.shared.dto;

/*
 * Some UI components need a unique ID over multiple entities. Using the identifier 
 * of the entity itself is not enough, because it may lead to collisions. Implementors
 * use a prefix, ID and name which should ensure unicity among instances of various 
 * entities.
 * 
 *  FIXME: Entities using a GUID as identifier may just return their ID, and render 
 *  this interface depreciated. 
 */
public interface ProvidesKey {
	public String getKey();
}
