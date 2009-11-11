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

package com.google.gwt.gears.persistence.rebind;

import com.google.gwt.gears.persistence.client.PersistenceUnit;
import com.google.gwt.gears.persistence.client.domain.ChildParentUnit;
import com.google.gwt.gears.persistence.client.domain.Parent;
import com.google.gwt.gears.persistence.client.domain.Child;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.Assert;

/**
 * @author Alex Bertram
 */
public class ChildParentTest extends PersistenceUnitTestCase {


  @Override
  protected Class<? extends PersistenceUnit> getPersistenceUnit() {
    return ChildParentUnit.class;
  }

  @Test
  public void testPersistChild() throws SQLException {

    EntityManager em = emf.createEntityManager();
    Parent parent = new Parent(99, "Fred");
    em.persist(parent);

    Child child = new Child(1, "Kiddo", parent);
    em.persist(child);

    em.close();

    Connection conn = connectionProvider.getConnection();
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("select parentId from Child");

    Assert.assertTrue(rs.next());
    Assert.assertEquals(99, rs.getInt(1));
    
  }

  @Test
  public void testLazyParent() throws SQLException {

    EntityManager em = emf.createEntityManager();
    Parent parent = new Parent(99, "Fred");
    em.persist(parent);

    Child child = new Child(1, "Kiddo", parent);
    em.persist(child);

    em.close();

    em = emf.createEntityManager();
    Child rekid = em.find(Child.class, 1);
    Parent rep = rekid.getParent();

    Assert.assertEquals(parent.getName(), rep.getName());
    Assert.assertEquals(parent.getId(), rep.getId());
  }

  @Test
  public void testCollectionsLoading() throws SQLException {

    // Add our test data to the database

    EntityManager em = emf.createEntityManager();
    Parent parent = new Parent(99, "Fred");
    em.persist(parent);

    em.persist(new Child(301, "Lora lee", parent));
    em.persist(new Child(302, "Billybob", parent));
    em.persist(new Child(302, "Zoella", parent));

    em.close();

    // Verify that the children are found in the collection

    em = emf.createEntityManager();
    parent = em.find(Parent.class, 99);

    Assert.assertEquals(3, parent.getChildren().size());
  }

}
