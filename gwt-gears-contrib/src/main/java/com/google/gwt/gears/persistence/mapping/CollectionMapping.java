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

package com.google.gwt.gears.persistence.mapping;

import com.google.gwt.gears.persistence.client.impl.PersistentSet;
import com.google.gwt.gears.persistence.client.impl.PersistentList;

import javax.persistence.CascadeType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.List;

/**
 * @author Alex Bertram
 */
public class CollectionMapping {


  private UnitMapping context;
  private String propertyName;
  private TypeInfo entityType;
  private TypeInfo collectionType;
  private String persistentCollectionClass;
  private String getterName;
  private String setterName;

  private boolean cascadePersist;
  private boolean cascadeMerge;
  private boolean cascadeRemove;
  private boolean cascadeRefresh;

  private JoinTable joinTable;
  private String mappedBy;

  public CollectionMapping(UnitMapping context, MethodInfo getter) {
    this.context = context;
    this.collectionType = getter.getReturnType();
    this.entityType = collectionType.getItemType();

    this.getterName = getter.getName();
    this.setterName = "set" + getter.getName().substring(3);
    this.propertyName = getterName.substring(0,1).toLowerCase() + getterName.substring(1);

    if(this.collectionType.getQualifiedName().equals(List.class.getName()))
      persistentCollectionClass = PersistentList.class.getName();
    else
      persistentCollectionClass = PersistentSet.class.getName();

    CascadeType[] cascadeTypes = null;
    ManyToMany manyToMany = getter.getAnnotation(ManyToMany.class);
    if (manyToMany != null) {
      cascadeTypes = manyToMany.cascade();
      mappedBy = manyToMany.mappedBy();
      joinTable = getter.getAnnotation(JoinTable.class);
    } else {
      OneToMany oneToMany = getter.getAnnotation(OneToMany.class);
      if (oneToMany != null) {
        cascadeTypes = oneToMany.cascade();
        mappedBy = oneToMany.mappedBy();
      }
    }

    if (cascadeTypes != null) {
      for (CascadeType type : cascadeTypes) {
        if (type == CascadeType.ALL) {
          cascadePersist = true;
          cascadeMerge = true;
          cascadeRefresh = true;
          cascadeRemove = true;
        } else if (type == CascadeType.MERGE) {
          cascadeMerge = true;
        } else if (type == CascadeType.PERSIST) {
          cascadePersist = true;
        } else if (type == CascadeType.REFRESH) {
          cascadeRefresh = true;
        } else if (type == CascadeType.REMOVE) {
          cascadeRemove = true;
        }
      }
    }

  }


  public TypeInfo getEntityType() {
    return entityType;
  }

  public TypeInfo getCollectionType() {
    return collectionType;
  }

  public boolean isCascadePersist() {
    return cascadePersist;
  }

  public boolean isCascadeMerge() {
    return cascadeMerge;
  }

  public boolean isCascadeRemove() {
    return cascadeRemove;
  }

  public boolean isCascadeRefresh() {
    return cascadeRefresh;
  }

  public JoinTable getJoinTable() {
    return joinTable;
  }

  public String getGetterName() {
    return getterName;
  }

  public String getSetterName() {
    return setterName;
  }

  public String getName() {
    return propertyName;
  }

  public String getMappedBy() {
    return mappedBy;
  }

  public String getPersistentCollectionClass() {
    return persistentCollectionClass;
  }
}
