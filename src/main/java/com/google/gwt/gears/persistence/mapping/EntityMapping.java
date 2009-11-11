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

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Maps an entity bean annotated with Jpa annotations to an Sqlite table.
 *
 * @author Alex Bertram
 */
public class EntityMapping {

  private String name;
  private String simpleClassName;
  private String qualifiedClassName;
  private String tableName;
  private TypeInfo type;
  private PropertyMapping id;
  private List<PropertyMapping> properties;
  private List<CollectionMapping> collections;
  private UnitMapping context;

  public EntityMapping(UnitMapping context, Class entityClass) {
    this(context, new TypeInfo.RuntimeImpl(entityClass));
  }

  public EntityMapping(UnitMapping context, TypeInfo entityType) {

    this.type = entityType;
    this.name = entityType.getSimpleName();
    this.qualifiedClassName = entityType.getQualifiedName();
    this.simpleClassName = entityType.getSimpleName();
    this.context = context;

    // determine the table name by looking at the @Table annotation or
    // the simple name of the class by default
    Table table = entityType.getAnnotation(Table.class);
    if (table != null && table.name() != null) {
      tableName = table.name();
    } else {
      tableName = entityType.getSimpleName();
    }

    id = null;
    properties = new ArrayList<PropertyMapping>();
    collections = new ArrayList<CollectionMapping>();
    for (MethodInfo method : entityType.getMethods()) {
      if (isPersistentGetter(context, method)) {

        if(method.getAnnotation(ManyToMany.class)!=null || method.getAnnotation(OneToMany.class)!=null) {
          collections.add(new CollectionMapping(context, method));
        } else {
          PropertyMapping property = createPropertyMapping(context,
              method, method.getReturnType());

          if (property != null) {
            if (property.isId()) {
              if (id == null) id = property;
              else throw new MappingException("Entity classes can have only one property marked with @Id");
            }
            properties.add(property);
          }
        }
      }
    }

    // double check that we have at least one column marked as @Id,
    // bail otherwise

    if (entityType.getAnnotation(javax.persistence.Entity.class) != null && id == null) {
      throw new MappingException("Entity classes must have exactly one property marked with @Id");
    }
  }


  /**
   * Gets the fully qualified class name of the java Entity
   *
   * @return the fully qualified class name of the java Entity
   */
  public String getQualifiedClassName() {
    return qualifiedClassName;
  }

  public String getIdBoxedClass() {
    if(id == null)
      throw new MappingException("Embedded classes don't have ids! Embedded class name = " + name);

    return id.getType().getQualifiedBoxedName();
  }

  public String getSimpleClassName() {
    return simpleClassName;
  }

  public String getQualifiedDelegateClass() {
    return qualifiedClassName + "_DelegateImpl";
  }

  public String getDelegateClass() {
    return context.getType().getSimpleName() + "_" + name + "DelegateImpl";
  }

  public String getDelegateField() {
    return simpleClassName.substring(0, 1).toLowerCase() + simpleClassName.substring(1) + "Delegate";
  }

  public String getLazyClass() {
    return context.getType().getSimpleName() + "_" + name + "LazyImpl";
  }

  public String getManagedClass() {
    return context.getType().getSimpleName() + "_" + name + "ManagedImpl";  }

  public UnitMapping getContext() {
    return context;
  }

  public TypeInfo getType() {
    return type;
  }

  /**
   * Gets the name of the SQL table to which this entity is mapped.
   *
   * @return the name of the SQL table to which this entity is mapped.
   */
  public String getTableName() {
    return tableName;
  }

  /**
   * Gets the list of mapped fields in this Entity, not including
   * the @Id field.
   *
   * @return the list of mapped fields in this entity, not including
   *         the @Id field
   */
  public List<PropertyMapping> getProperties() {
    return properties;
  }

  /**
   * Gets this Entity's @Id property. This can
   * be null for Embeddable/Embedded types, but
   * is guaranteeded to be non-null for @Entity's
   *
   * @return this Entity's @Id field
   */
  public PropertyMapping getId() {
    return id;
  }

  /**
   * Returns true if this is a persistent field getter
   *
   * @param method
   * @return
   */
  public boolean isPersistentGetter(UnitMapping context, MethodInfo method) {
    if (!FieldUtil.prefixedBy(method.getName(), "get") && !FieldUtil.prefixedBy(method.getName(), "is"))
      return false;
    if (!doesSetterExist(method))
      if (method.getParameterCount() != 0)
        return false;
    if (!method.isPublic()) {
      return false;
    }
    if (method.isStatic()) {
      return false;
    }
    if (method.getAnnotation(Transient.class) != null) {
      return false;
    }
    return true;
  }

  private boolean doesSetterExist(MethodInfo getterMethod) {

    // TODO

    return true;
//
//
//        try {
//            getterMethod.getEnclosingType().getMethod(setterName, new JType[] { getterMethod.getReturnType() });
//        } catch (NotFoundException e) {
//            logger.branch(TreeLogger.Type.ERROR, "Setter not found!", e);
//            throw new UnableToCompleteException();
//        }
  }


  public List<ColumnMapping> getColumns() {
    List<ColumnMapping> list = new ArrayList<ColumnMapping>();
    for (PropertyMapping property : properties) {
      list.addAll(property.getColumns());
    }
    return list;
  }

  public List<CollectionMapping> getCollections() {
    return collections;
  }

  public List<ColumnMapping> getAllColumns() {
    List<ColumnMapping> list = new ArrayList<ColumnMapping>();
    list.addAll(id.getColumns());
    list.addAll(getColumns());
    return list;
  }

  /**
   * Gets the total count of columns mapped to this entity,
   * including the primary key columns.
   *
   * @return the total number of columns mapped to this entity,
   *         including the primary key columns
   */
  public int getTotalColumnCount() {
    int count = 0;
    if (id != null) count += id.getColumns().size();
    count += getColumnCount();
    return count;
  }

  /**
   * Returns the number of columns mapped to this entity, excluding
   * the @Id property
   *
   * @return the number of columns mapped to this entity, excluding
   *         the @Id property
   */
  public int getColumnCount() {
    int count = 0;
    for (PropertyMapping field : properties) {
      count += field.getColumns().size();
    }
    return count;
  }

  public boolean areAllNullable() {
    for (PropertyMapping property : properties) {
      if (!property.isNullable()) {
        return false;
      }
    }
    return true;
  }

  private interface Lister<T> {
    String itemToString(T item);
  }

  private <T> String makeCSList(Collection<T> items, Lister<T> lister) {
    return makeList(items, ",", lister);
  }

  private <T> String makeList(Collection<T> items, String delim, Lister<T> lister) {
    StringBuilder sb = new StringBuilder();
    boolean needsDelim = false;
    for (T item : items) {
      if (needsDelim) {
        sb.append(delim);
      } else {
        needsDelim = true;
      }
      sb.append(lister.itemToString(item));
    }
    return sb.toString();
  }

  private String makeCSList(Collection<String> items) {
    return makeCSList(items, new Lister<String>() {
      @Override
      public String itemToString(String item) {
        return item;
      }
    });
  }


  public String getCreateTableStatement() {
    StringBuilder sb = new StringBuilder();
    sb.append("create table if not exists ").append(tableName);
    sb.append(" (");
    for (ColumnMapping column : id.getColumns()) {
      sb.append(column.getName()).append(" ").append(column.getType().toString());
    }

    // TODO: i don't think sqlite can enforce multiple primary keys. to check.
    if (id.getColumns().size() == 1) {
      sb.append(" primary key");

      if (id.isAutoincrement()) {
        sb.append(" autoincrement");
      }
    }

    for (PropertyMapping property : properties) {
      for (ColumnMapping column : property.getColumns()) {
        sb.append(",").append(column.getName()).append(" ")
            .append(column.getType().toString());

        // TODO: add unique constraints for those fields which are annotated
      }
    }
    sb.append(")");
    return sb.toString();
  }

  public String getSelectByIdQuery() {
    StringBuilder sb = new StringBuilder();
    sb.append("select ").append(makeCSList(getColumns(), new Lister<ColumnMapping>() {
      @Override
      public String itemToString(ColumnMapping item) {
        return item.getName();
      }
    }));
    sb.append(" from ")
        .append(getTableName()).append(" where ");

    sb.append(makeList(id.getColumns(), " AND ", new Lister<ColumnMapping>() {
      @Override
      public String itemToString(ColumnMapping item) {
        return item.getName() + "=?";
      }
    }));

    return sb.toString();
  }


  public String getInsertStatement() {

    StringBuilder sb = new StringBuilder();
    sb.append("insert into ").append(getTableName()).append(" (");
    sb.append(makeCSList(getAllColumns(), new Lister<ColumnMapping>() {
      @Override
      public String itemToString(ColumnMapping item) {
        return item.getName();
      }
    }));
    sb.append(") values (");
    sb.append(makeCSList(getAllColumns(), new Lister<ColumnMapping>() {
      @Override
      public String itemToString(ColumnMapping item) {
        return "?";
      }
    }));
    sb.append(")");
    return sb.toString();
  }

  public String getUpdateStatement() {

    // make a list of all columns that are updatable
    // (primary key is excluded a priori)
    List<ColumnMapping> names = new ArrayList<ColumnMapping>();
    for (PropertyMapping property : properties) {
      if (property.isUpdatable()) {
        for (ColumnMapping column : property.getColumns()) {
          names.add(column);
        }
      }
    }

    StringBuilder sb = new StringBuilder();
    sb.append("update ").append(getTableName()).append(" set ");
    sb.append(makeCSList(names, new Lister<ColumnMapping>() {
      @Override
      public String itemToString(ColumnMapping item) {
        return item + "=?";
      }
    }));

    // now add the primary key as a criteria
    sb.append(" where ").append(
        makeCSList(id.getColumns(), new Lister<ColumnMapping>() {
          @Override
          public String itemToString(ColumnMapping item) {
            return item.getName() + "=?";
          }
        }));

    return sb.toString();
  }

  public String getDeleteStatement() {
    StringBuilder sb = new StringBuilder();
    sb.append("delete from ").append(getTableName()).append(" where ");
    sb.append(makeCSList(id.getColumns(), new Lister<ColumnMapping>() {
      @Override
      public String itemToString(ColumnMapping item) {
        return item.getName() + "=?";
      }
    }));
    return sb.toString();
  }

  /**
   * Maps a member to a TypeMapping, the class which handles DDL and conversion between
   * the ResultSet and the Entity
   *
   * @param type The type of the member. (Either the return type of the getter or
   *             the type of the field)
   * @return A type mapping class
   */
  public PropertyMapping createPropertyMapping(UnitMapping context, MethodInfo getter, TypeInfo type) {

    if (type.getQualifiedName().equals(String.class.getName())) {
      return new StringMapping(getter);
    }
    if (type.getQualifiedName().equals(Date.class.getName())) {
      return new DateMapping(getter);
    }

    if (type.getQualifiedName().equals(Integer.class.getName()) ||
        type.getQualifiedName().equals(Short.class.getName()) ||
        type.getQualifiedName().equals(Byte.class.getName()) ||
        type.getQualifiedName().equals(Character.class.getName()) ||
        type.getQualifiedName().equals(Long.class.getName()) ||
        type.getQualifiedName().equals(Double.class.getName()) ||
        type.getQualifiedName().equals(Float.class.getName()) ||
        type.getQualifiedName().equals(Boolean.class.getName())) {
      return new PrimitiveMapping(getter);
    }


    Class primitive = type.isPrimitive();
    if (primitive != null) {
      return new PrimitiveMapping(getter);
    }

    if (getter.getAnnotation(ManyToOne.class) != null ||
        getter.getAnnotation(OneToOne.class) != null) {

      return new ManyToOneMapping(context, getter);
    }

    if (getter.getAnnotation(Embedded.class) != null) {
      return new EmbeddedMapping(context, getter);
    }

    return null;
  }

  public String getName() {
    return name;
  }
}
