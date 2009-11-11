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

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.gears.persistence.client.PersistenceUnit;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates the mapping of a Persistence Unit
 *
 * @author Alex Bertram
 */
public class UnitMapping {

  private int nextId = 0;
  private Map<String, EntityMapping> entityMappings;
  private TypeInfo type;
  private String packageName;

  public UnitMapping() {
    entityMappings = new HashMap<String, EntityMapping>();
  }

  public UnitMapping(TypeInfo type) {
    this();
    this.type = type;
  }

  public UnitMapping(JType type) {
    this(new TypeInfo.GwtImpl(type));
    this.packageName = type.isClassOrInterface().getPackage().getName();
  }

  public UnitMapping(Class<? extends PersistenceUnit> type) {
    this(new TypeInfo.RuntimeImpl(type));
    this.packageName = type.getPackage().getName();
  }

  /**
   * Gets the name of the package of the implementation classes.
   *
   * @return the name of the package
   */
  public String getPackageName() {
    return packageName;
  }

  /**
   * Gets the type of the <code>PersistenceContext</code> interface for which
   * we are generating an implementation.
   *
   * @return the <code>PersistenceContext</code> interface for which
   *         we are generating an implementation.
   */
  public TypeInfo getType() {
    return type;
  }

  /**
   * Gets the (simple) name of the class that implements the
   * requested <code>PersistenceContext</code>
   *
   * @return the (simple) name of the class that implements the
   *         requested <code>PersistenceContext</code>
   */
  public String getPersistenceContextImplClass() {
    return type.getSimpleName() + "_Impl";
  }

  /**
   * Gets the (simple) name of the class that implements the
   * requested <code>PersistenceContext</code>
   *
   * @return the (simple) name of the class that implements the
   *         requested <code>PersistenceContext</code>
   */
  public String getPersistenceContextImplQualifiedClass() {
    return getPackageName() + "." + getPersistenceContextImplClass();
  }

  /**
   * Gets the (simple) name of the class that implements the
   * <code>EntityManagerFactory</code> interface
   *
   * @return the simple name of the class that implements the
   *         <code>EntityManagerFactory</code> interface
   */
  public String getEntityManagerFactoryClass() {
    return type.getSimpleName() + "_EmfImpl";
  }

  /**
   * Gets the (simple) name of the class that implements the
   * <code>EntityManager</code> interface
   *
   * @return the simple name of the class that implements the
   *         <code>EntityManager</code> interface
   */
  public String getEntityManagerClass() {
    return type.getSimpleName() + "_EmImpl";
  }

  /**
   * Gets the list of mappings to entities that are managed
   * by this <code>PersistenceContext</code>
   *
   * @return the list of mappings to entities that are managed
   *         by this <code>PersistenceContext</code>
   */
  public Collection<EntityMapping> getEntities() {
    return entityMappings.values();
  }

  public EntityMapping getMapping(JClassType entityClass) {
    return getMapping(new TypeInfo.GwtImpl(entityClass));
  }

  public EntityMapping getMapping(Class entityClass) {
    return getMapping(new TypeInfo.RuntimeImpl(entityClass));
  }

  public EntityMapping getMapping(TypeInfo entityType) {

    EntityMapping mapping = entityMappings.get(entityType.getQualifiedName());
    if (mapping == null) {
      mapping = new EntityMapping(this, entityType);
      if(mapping.getType().getAnnotation(Entity.class)!=null)
        entityMappings.put(entityType.getQualifiedName(), mapping);
    }
    return mapping;
  }

  public int getNextUniqueInt() {
    return nextId++;
  }


}
