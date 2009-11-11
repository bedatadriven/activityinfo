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

import com.google.gwt.gears.persistence.client.ConnectionProvider;
import com.google.gwt.gears.persistence.client.PersistenceUnit;
import com.google.gwt.gears.persistence.client.domain.SimpleUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Before;
import org.junit.After;

/**
 * @author Alex Bertram
 */
public abstract class PersistenceUnitTestCase {

  protected ConnectionProvider connectionProvider;
  protected EntityManagerFactory emf;

  @Before
  public void beforeTest() throws Exception {

    PersistenceUnitCompiler compiler = new PersistenceUnitCompiler();
    PersistenceUnit unit = compiler.create(getPersistenceUnit());

    connectionProvider = new MockConnectionProvider();
    emf = unit.createEntityManagerFactory(connectionProvider);
  }

  protected abstract Class<? extends PersistenceUnit> getPersistenceUnit();

  @After
  public void afterTest() {
    if(connectionProvider!=null)
      connectionProvider.close();
  }
}
