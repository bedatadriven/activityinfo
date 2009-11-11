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

import org.junit.Test;
import com.google.gwt.gears.persistence.client.domain.*;
import com.google.gwt.gears.persistence.client.PersistenceUnit;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import java.sql.*;

import junit.framework.Assert;

/**
 * @author Alex Bertram
 */
public class EmbeddedTest extends PersistenceUnitTestCase{


  @Override
  protected Class<? extends PersistenceUnit> getPersistenceUnit() {
    return ContactUnit.class;
  }

  @Test
  public void testEmbeddedPersist() throws Exception {

    EntityManager em = emf.createEntityManager();

    Contact boss = new Contact();
    boss.setId("AB64");
    boss.setName("Ralph");
    boss.setAddress(new Address("13 kirby road", null, "Sterling Hghts", "MI", 16901, 4));

    em.persist(boss);
    em.close();

  // Verify that it has in fact been written to the db

    Connection conn = connectionProvider.getConnection();
    PreparedStatement stmt = conn.prepareStatement("select id, name, state from Contact");
    ResultSet rs = stmt.executeQuery();

    Assert.assertTrue(rs.next());
    Assert.assertEquals("AB64", rs.getString(1));
    Assert.assertEquals("Ralph", rs.getString(2));
    Assert.assertEquals("MI", rs.getString(3));
//
//    Contact reboss = new Contact();
//    reboss = em.find(Contact.class, boss.getId());
//    Assert.assertEquals(boss, reboss);
//
//    em.close();
  }


  @Test
  public void testEmbeddedFind() throws Exception {

    EntityManager em = emf.createEntityManager();

    Contact boss = new Contact();
    boss.setId("AB64");
    boss.setName("Ralph");
    boss.setAddress(new Address("13 kirby road", null, "Sterling Hghts", "MI", 16901, 4));

    em.persist(boss);
    em.close();

    em = emf.createEntityManager();
    Contact reboss = em.find(Contact.class, boss.getId());

    Assert.assertEquals(boss, reboss);
  }


}
