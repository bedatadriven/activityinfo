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
import java.util.List;
import java.util.Collection;
import java.util.ListIterator;

/**
 * @author Alex Bertram
 */
public class PersistentList<T,K> extends AbstractPersistentCollection<T,K>
    implements List<T> {

  List<T> inner;

  public PersistentList(Delegate<T, K> tkDelegate, Query query) {
    super(tkDelegate, query);
  }

  public boolean addAll(int index, Collection<? extends T> c) {
    assureLoaded();
    return inner.addAll(index,c);
  }

  public T get(int index) {
    assureLoaded();
    return inner.get(index);
  }

  public T set(int index, T element) {
    assureLoaded();
    return inner.set(index, element);
  }

  public void add(int index, T element) {
    assureLoaded();
    inner.add(index,element);
  }

  public T remove(int index) {
    assureLoaded();
    return inner.remove(index);
  }

  public int indexOf(Object o) {
    assureLoaded();
    return inner.indexOf(o);
  }

  public int lastIndexOf(Object o) {
    assureLoaded();
    return inner.lastIndexOf(o);
  }

  public ListIterator<T> listIterator() {
    assureLoaded();
    return inner.listIterator();
  }

  public ListIterator<T> listIterator(int index) {
    assureLoaded();
    return inner.listIterator(index);
  }

  public List<T> subList(int fromIndex, int toIndex) {
    assureLoaded();
    return inner.subList(fromIndex, toIndex);
  }
}
