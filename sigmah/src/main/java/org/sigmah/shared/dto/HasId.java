package org.sigmah.shared.dto;

/*
 * Entities having an unique identifier over the complete set of instances of it's
 * own type should implement this interface
 */
public interface HasId {
	public int getId();
}