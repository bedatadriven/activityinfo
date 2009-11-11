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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author Alex Bertram
 */
public class AbstractPersistentCollection<T, K> implements Collection<T> {

  private final Delegate<T, K> delegate;
  private final Query query;

  /**
   * The actual set, loaded on demand.
   */
  private Collection<T> collection = null;

  public AbstractPersistentCollection(Delegate<T, K> delegate, Query query) {
    this.delegate = delegate;
    this.query = query;
  }

  protected void assureLoaded() {
    if (collection == null) {
      collection = new HashSet<T>(
          query.getResultList());
    }
  }


  @Override
  public int size() {
    assureLoaded();
    return collection.size();
  }

  @Override
  public boolean isEmpty() {
    assureLoaded();
    return collection.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    assureLoaded();
    return collection.contains(o);
  }

  @Override
  public Iterator<T> iterator() {
    assureLoaded();
    return collection.iterator();
  }

  @Override
  public Object[] toArray() {
    assureLoaded();
    return collection.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    assureLoaded();
    return collection.toArray(a);
  }

  @Override
  public boolean add(T t) {
    assureLoaded();
    return collection.add(t);
  }

  @Override
  public boolean remove(Object o) {
    assureLoaded();
    return collection.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    assureLoaded();
    return collection.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    assureLoaded();
    return collection.addAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    assureLoaded();
    return collection.retainAll(c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    assureLoaded();
    return collection.removeAll(c);
  }

  @Override
  public void clear() {
    assureLoaded();
    collection.clear();
  }


}
