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

import java.lang.reflect.Method;
import java.util.List;

/**
 * Defines a mapping between an entity's property (one value)
 * and one or more SQL columns.
 *
 * @author Alex Bertram
 */
public abstract class PropertyMapping {

  private String name;
  private String getterName;
  private String setterName;
  private TypeInfo type;

  protected boolean unique = false;
  protected boolean insertable = true;
  protected boolean updatable = true;
  protected boolean nullable = true;

  public PropertyMapping(MethodInfo getterMethod) {

    this.getterName = getterMethod.getName();
    this.type = getterMethod.getReturnType();

    int prefixLen = getterName.startsWith("is") ? 2 : 3;
    setterName = "set" + getterName.substring(prefixLen);
    name = Character.toLowerCase(getterName.charAt(prefixLen)) +
        getterName.substring(prefixLen + 1);
  }

  public boolean isNullable() {
    return nullable;
  }

  public boolean isUnique() {
    return unique;
  }

  /**
   * Gets the name of the persistent property or field
   *
   * @return the name of the persistent property or field
   */
  public String getName() {
    return name;
  }

  public String getGetterName() {
    return getterName;
  }

  public TypeInfo getType() {
    return type;
  }

  public String getSetterName() {
    return setterName;
  }

  public boolean isId() {
    return false;
  }

  public boolean isAutoincrement() {
    return false;
  }

  /**
   * Returns true if the property/field is an embedded class
   *
   * @return
   */
  public boolean isEmbedded() {
    return false;
  }

  public boolean isPrimitive() {
    return false;
  }


  /**
   * Returns true if this property/field is a related entity with a One-To-One or
   * Many-To-One relationship
   *
   * @return
   */
  public boolean isToOne() {
    return false;
  }

  /**
   * Gets the mapping
   *
   * @return
   */
  public EntityMapping getEmbeddedClass() {
    return null;
  }

  public EntityMapping getRelatedEntity() {
    return null;
  }


  public String callGetter(String varName) {
    return varName + "." + getterName + "()";
  }

  public boolean isInsertable() {
    return insertable;
  }

  public boolean isUpdatable() {
    return updatable;
  }
  
  public String getReaderName() {
    return null;
  }

  /**
   * Returns a list of SQL column types used to store this entity member.
   * (Most entity members are stored as a single column, but embedded columns, for example,
   * span several columns, and collections are not store in columns at all)
   *
   * @return the list of SQL column types in which to store this field.
   */
  public abstract List<ColumnMapping> getColumns();

  /**
   * Returns information on the column in which this persistent property/field is stored,
   * or null if this property/field is stored in several columns.
   *
   * @return information on the column in which this persistent property/field is stored,
   * or null if this property/field is stored in several columns.
   */
  public ColumnMapping getColumn() {
    List<ColumnMapping> columns = getColumns();
    if(columns.size() == 1)
      return columns.get(0);
    
    return null;
  }

  public abstract void writeColumnValues(ColumnStreamWriter writer, Object entity);


  /**
   * Uses reflection to read the value of this property from an object
   * instance.
   *
   * @param instance The instance of a class to which this mapping is a member
   * @return The value of this field in the given instance
   */
  public Object getValue(Object instance) {
    Object value;
    try {
      Method getter = instance.getClass().getMethod(getGetterName());

      value = getter.invoke(instance);

    } catch (NullPointerException e) {
      value = null;
    } catch (Exception e) {
      throw new MappingException("Exception thrown while getting the value.\n" +
          "Field: " + getName() + "\n" +
          "Mapping:" + this.toString(), e);
    }
    return value;
  }


}
