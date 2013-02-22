package org.activityinfo.shared.dto;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
