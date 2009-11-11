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

import com.google.gwt.gears.persistence.client.domain.SimpleUnit;
import com.google.gwt.gears.persistence.client.domain.Simple;
import com.google.gwt.gears.persistence.client.ConnectionProvider;
import com.google.gwt.gears.persistence.client.PersistenceUnit;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.Before;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;

import junit.framework.Assert;

/**
 * @author Alex Bertram
 */
public class SimpleTest extends PersistenceUnitTestCase {


  @Override
  protected Class<? extends PersistenceUnit> getPersistenceUnit() {
    return SimpleUnit.class;
  }

  @Test
  public void testSimplePersist() throws Exception {

    EntityManager em = emf.createEntityManager();

    Simple entity = new Simple(3, "Bob", true);
    em.persist(entity);

    em.close();

    // Verify that it has in fact been written to the db

    Connection conn = connectionProvider.getConnection();
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("select id, name, available_flag from Simple");
    Assert.assertTrue(rs.next());
    Assert.assertEquals("id", 3, rs.getInt(1));
    Assert.assertEquals("name", "Bob", rs.getString(2));
    Assert.assertEquals("available", true, rs.getBoolean(3));

    conn.close();
  }

  @Test
  public void testObjectIdentity() throws Exception {

    EntityManager em = emf.createEntityManager();

    Simple bob = new Simple(1, "bob", false);
    em.persist(bob);

    Simple rebob = em.find(Simple.class, 1);

    Assert.assertTrue("object identity preserved", bob == rebob);

    Simple refbob = em.getReference(Simple.class, 1);
    Assert.assertTrue("object identity preserved", bob == refbob);


    em.close();
  }



  @Test
  public void testSimpleFind() throws Exception {

    EntityManager em = emf.createEntityManager();

    Simple entity = new Simple(3, "Bob", false);
    em.persist(entity);

    em.close();

    em = emf.createEntityManager();

    Simple rebob = em.find(Simple.class, 3);
    Assert.assertNotNull(rebob);
    Assert.assertEquals("id", 3, rebob.getId());
    Assert.assertEquals("name", "Bob", rebob.getName());
    Assert.assertEquals("available", false, rebob.isAvailable());

    em.close();
  }

  @Test
  public void testCreateNativeQuery() throws Exception {

    // prefill the database with a few rows
    EntityManager em = emf.createEntityManager();
    String[] fruit = new String[] { "Apple", "Pear", "Orange", "Mango"};
    for(int i = 0; i!=fruit.length; ++i) {
      em.persist(new Simple(i, fruit[i], i%2==0));
    }
    em.close();

    // now try to query for fruit that is availble
    em = emf.createEntityManager();

    Query query = em.createNativeQuery("select * from Simple where available_flag=1", Simple.class);
    List<Simple> results = query.getResultList();

    Assert.assertEquals(results.size(), 2);
    Assert.assertEquals(fruit[0], results.get(0).getName());                  
    Assert.assertEquals(fruit[2], results.get(1).getName());

  }
}
