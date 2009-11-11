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

import javax.persistence.EmbeddedId;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapuslates the mapping of an embedded class, including embedded ids.
 * <p><strong>From the specification: 2.1.5</strong></p>
 * <p>An entity may use other fine-grained classes to represent entity state. Instances of these classes,
 * unlike entity instances themselves, do not have persistent identity. Instead, they exist only as
 * embedded objects of the entity to which they belong. Such embedded objects belong strictly to
 * their owning entity, and are
 * not sharable across persistent entities. Attempting to share an embedded object across
 * entities has undefined
 * semantics. Because these objects have no persistent identity, they are typically mapped together
 * with the entity instance to which they belong.[10]
 * Embeddable classes must adhere to the requirements specified in Section 2.1 for entities with the
 * exception
 * that embeddable classes are not annotated as Entity. Embeddable classes must be annotated as
 * Embeddable or denoted in the XML descriptor as such. The access type for an embedded object is
 * determined by the access type of the entity in which it is embedded. <strong>Support for only one level of
 * embedding is required by this specification.</strong>
 * Additional requirements on embeddable classes are described in section 9.1.34.</p>
 *
 * <p><strong>From the specification 9.1.34:</strong></p>
 * <p>The Embeddable annotation is used to specify a class whose instances are stored as an intrinsic part
 * of an owning entity and share the identity of the entity. Each of the persistent properties or fields of the
 * embedded object is mapped to the database table for the entity. Only Basic, Column, Lob, Temporal,
 * and Enumerated mapping annotations may portably be used to map the persistent fields or
 * properties of classes annotated as Embeddable</p>
 *
 * @author Alex Bertram
 */
public class EmbeddedMapping extends PropertyMapping {

  private boolean id;
  private UnitMapping context;
  private int propertyId;

  public EmbeddedMapping(UnitMapping context, MethodInfo getter) {
    super(getter);
    this.context = context;
    this.propertyId = context.getNextUniqueInt();

    /* @EmbeddedId
       9.1.4 - The EmbeddedId annotation is applied to a persistent field or property of an entity class
               or mapped superclass to denote a composite primary key that is an embeddable class.
               The embeddable class must be annotated as Embeddable.[44]
     */

    if(getter.getAnnotation(EmbeddedId.class)!=null) {
      id = true;
    }

  }

  @Override
  public EntityMapping getEmbeddedClass() {
    return context.getMapping(getType());
  }

  @Override
  public boolean isEmbedded() {
    return true;
  }

  @Override
  public List<ColumnMapping> getColumns() {
    // TODO: Handle @AttributeOverride and @AttributeOverrides (See 9.1.35)

    List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
    for (PropertyMapping property : getEmbeddedClass().getProperties()) {
      if(! (property instanceof SingleColumnPropertyMapping)) {
        throw new MappingException("Only Basic, Column, Lob, Temporal,\n" +
            " and Enumerated mapping annotations may portably be used to map the persistent fields or\n" +
            " properties of classes annotated as Embeddable");
      }
      columns.addAll(property.getColumns());
    }
    return columns;
  }

  @Override
  public void writeColumnValues(ColumnStreamWriter writer, Object entity) {

    // get the embedded instance
    Object embedded = getValue(entity);

    if (embedded == null) {
      writer.writeNulls(getEmbeddedClass().getColumnCount());
    } else {
      for (PropertyMapping prop : getEmbeddedClass().getProperties()) {
        prop.writeColumnValues(writer, embedded);
      }
    }
  }
}
