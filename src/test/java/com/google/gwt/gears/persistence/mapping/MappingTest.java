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

import com.google.gwt.gears.persistence.client.domain.*;
import junit.framework.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Alex Bertram
 */
public class MappingTest {

  @Test
  public void testAssumptions() {

    Assert.assertEquals("java.lang.String", new TypeInfo.RuntimeImpl(String.class).getQualifiedName());
    Assert.assertEquals("String", new TypeInfo.RuntimeImpl(String.class).getSimpleName());
    Assert.assertEquals("int", new TypeInfo.RuntimeImpl(Integer.TYPE).getSimpleName());
  }

  @Test
  public void testSimpleMapping() throws SQLException {

    UnitMapping context = new UnitMapping(SimpleUnit.class);
    EntityMapping entity = new EntityMapping(context, Simple.class);

    Assert.assertEquals(3, entity.getProperties().size());

//        testCreateStatement(entity);
  }

  @Test
  public void testEmbeddedMapping() {
    UnitMapping context = new UnitMapping(ContactUnit.class);
    EntityMapping entity = new EntityMapping(context, Contact.class);

    dumpMapping(entity);

    Assert.assertEquals(3, entity.getProperties().size());
    Assert.assertEquals(8, entity.getColumns().size());
  }

  @Test
  public void testBoxedPrimitives() {
    UnitMapping context = new UnitMapping(AdminUnit.class);
    EntityMapping entity = new EntityMapping(context, Bounds.class);

    dumpMapping(entity);

    Assert.assertEquals(4, entity.getProperties().size());

  }

  private void dumpMapping(EntityMapping entity) {
    for (PropertyMapping mapping : entity.getProperties()) {
      System.out.println(mapping.getName() + ": " + mapping.getClass().getName());
      for (ColumnMapping column : mapping.getColumns()) {
        System.out.println("    " + column.getName() + " " + column.getType());
      }
    }
  }


}
