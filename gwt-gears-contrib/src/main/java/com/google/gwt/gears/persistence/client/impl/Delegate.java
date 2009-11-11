/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package com.google.gwt.gears.persistence.client.impl;

import javax.persistence.Query;

/**
 * Accepts delegation for the EntityManager for a single Entity Class
 *
 * @see javax.persistence.EntityManager EntityManager
 */
public interface Delegate<T, K> {

  void persist(T entity);

  void remove(T entity);

  void refresh(T entity);

  T merge(T entity);

  T find(K primaryKey);

  T getReference(K primaryKey);

  boolean contains(T entity);

  void clear();

  Query createNativeBoundQuery(String sql);
}
