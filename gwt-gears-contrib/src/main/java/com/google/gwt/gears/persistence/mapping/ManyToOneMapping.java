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

import javax.persistence.JoinColumn;
import java.util.List;
import java.util.Collections;

/**
 *
 * Encapsulates the ManyToOne and OneToOne property mappings
 *
 * @author Alex Bertram
 */
public class ManyToOneMapping extends PropertyMapping {

  /**
   * The class to which this field is bound
   */
  private TypeInfo entityType;

  /**
   * The mapping context used to obtain information about the
   * linked entity. Note: do NOT access from the constructor as this
   * may trigger an infinite loop if a recursive entity structure
   * is found.
   */
  private UnitMapping context;

  private MethodInfo getter;

  public ManyToOneMapping(UnitMapping context, MethodInfo getter) {
    super(getter);

    this.entityType = getter.getReturnType();
    this.context = context;
    this.getter = getter;

    JoinColumn columnAnno = getter.getAnnotation(JoinColumn.class);
    if (columnAnno != null) {
      updatable = columnAnno.updatable();
      insertable = columnAnno.insertable();
    }
  }


  @Override
  public EntityMapping getRelatedEntity() {
    return context.getMapping(entityType);
  }

  @Override
  public boolean isToOne() {
    return true;
  }

  @Override
  public List<ColumnMapping> getColumns() {

    PropertyMapping relatedEntityId = getRelatedEntity().getId();

    if(relatedEntityId.getColumns().size() > 1) {

        throw new MappingException("Multiple join columns not yet supported. Terribly sorry. " +
            "If you've got some time on your hands, implement me in ManyToOneMapping.java");

    } else {

      /* Column name:
       * (Default only applies if a single join column is used.) The concatenation
       * of the following:
       *  - the name of the referencing relationship property or field of the referencing entity;
       *  - "_";
       *  - the name of the referenced primary key column.
       *
       * If there is no such referencing relationship property or field in
       * the entity, the join column name is formed as the concatenation
       * of the following:
       *  - the name of the entity;
       *  - "_";
       *  - the name of the referenced primary key column.
       *
       * Source: Table 8, Section 9.1.6
       */

      ColumnMapping referencedPrimaryKeyColumn = relatedEntityId.getColumns().get(0);

      String columnName = getName() + "_" +
          referencedPrimaryKeyColumn.getName();

      JoinColumn joinColumn = getter.getAnnotation(JoinColumn.class);
      if (joinColumn != null && joinColumn.name() != null && joinColumn.name().length() != 0)
          columnName = joinColumn.name();

      return Collections.singletonList(new ColumnMapping(columnName,
          referencedPrimaryKeyColumn.getType(),
          referencedPrimaryKeyColumn.getStmtSetter(),
          referencedPrimaryKeyColumn.getName()
      ));
    }
  }

  @Override
  public void writeColumnValues(ColumnStreamWriter writer, Object entity) {

    PropertyMapping joinColumn = getRelatedEntity().getId();

    // first use reflection to get the entity
    Object linkedEntity = getValue(entity);

    if (linkedEntity == null) {
      writer.writeNulls(joinColumn.getColumns().size());
    } else {
      // otherwise, write the join columns
      joinColumn.writeColumnValues(writer, linkedEntity);
    }
  }
}

