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

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;
import java.util.Collections;

/**
 * Maps a single database column to an entity property.
 *
 * @author Alex Bertram
 */
public abstract class SingleColumnPropertyMapping extends PropertyMapping {

  private boolean id;
  private boolean autoincrement;
  private String columnName;

  protected SingleColumnPropertyMapping(MethodInfo getterMethod) {
    super(getterMethod);

    columnName = getName();

    // @Id annotation makes this field an id
    id = (getterMethod.getAnnotation(Id.class)!=null);

    // @GeneratedValue(strategy = GeneratedType.Auto)
    if(id) {
      GeneratedValue generatedValue = getterMethod.getAnnotation(GeneratedValue.class);
      if(generatedValue!=null) {
        if(generatedValue.strategy() == GenerationType.AUTO) {
          autoincrement = true;
        }
      }
    }

    // @Column annotation -  insertable, updateable etc
    Column column = getterMethod.getAnnotation(Column.class);
    if (column != null) {
      insertable = !autoincrement && column.insertable();
      updatable = !autoincrement && column.updatable();
      nullable = !id && column.nullable();
      unique = id || column.unique();

      if (column.name() != null && column.name().length() != 0) {
        columnName = column.name();
      }
    }
  }

  @Override
  public List<ColumnMapping> getColumns() {
    return Collections.singletonList(new ColumnMapping(columnName, getSqlTypeName(), getStmtSetter()));
  }

  @Override
  public boolean isId() {
    return id;
  }

  @Override
  public boolean isAutoincrement() {
    return autoincrement;
  }

  protected abstract SqliteType getSqlTypeName();

  protected abstract String getStmtSetter();


  @Override
  public void writeColumnValues(ColumnStreamWriter writer, Object entity) {

    Object value = getValue(entity);

    writer.writeColumn(value == null ? null : convertToString(value));
  }

  /**
   * Converts the object read from the entity object to a String ready to be
   * used in a Gears execute() call. Calls value.toString() by default.
   *
   * @param value The value read from the object, guaranteed not be null
   * @return a string value ready to be inserted in to the database
   */
  protected String convertToString(Object value) {
    return value.toString();
  }

}
