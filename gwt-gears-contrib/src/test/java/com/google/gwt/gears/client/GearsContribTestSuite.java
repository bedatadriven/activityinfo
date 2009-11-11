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

package com.google.gwt.gears.client;


import com.google.gwt.gears.jdbc.client.JdbcTest;
import com.google.gwt.gears.persistence.client.CrudTest;
import com.google.gwt.gears.persistence.client.EntityManagerTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * @author Alex Bertram
 */
public class GearsContribTestSuite extends TestCase {

  public static Test suite() {
    TestSuite suite = new TestSuite("Gears Contrib Test Suite");

    suite.addTestSuite(JdbcTest.class);
//        suite.addTestSuite(RpcTest.class);
//        suite.addTestSuite(WorkerTest.class);
    suite.addTestSuite(CrudTest.class);
    suite.addTestSuite(EntityManagerTest.class);

    return suite;
  }

}
