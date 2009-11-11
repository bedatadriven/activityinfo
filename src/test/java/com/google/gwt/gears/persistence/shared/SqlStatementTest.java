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

package com.google.gwt.gears.persistence.shared;

import com.google.gwt.gears.persistence.client.impl.SQLStatement;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alex Bertram
 */
public class SqlStatementTest {

  @Test
  public void testSimple() {

    SQLStatement sql = new SQLStatement("  selEcT a,b from   mytable where x is not null  ");

    Assert.assertEquals("token count", 11, sql.getTokens().size());
    Assert.assertEquals("mytable", sql.getTokens().get(5).toString());
    Assert.assertEquals("null", sql.getTokens().get(10).toString());
    Assert.assertEquals("query type", SQLStatement.TYPE_SELECT, sql.getQueryType());
  }

  @Test
  public void testParams() {
    SQLStatement sql = new SQLStatement("SELECT * from mytable where a = ?1 or b= :keyId");
    Assert.assertEquals(1, sql.getTokens().get(7).getParameterPosition());
    Assert.assertEquals("keyId", sql.getTokens().get(11).getParameterName());
  }

  @Test
  public void testSymbols() {
    //                                   |      | |    |       |     || | |   || | |   |||
    SQLStatement sql = new SQLStatement("SELECT * from mytable where a<>b and a<=b and c=d");

    for (int i = 0; i != sql.getTokens().size(); ++i) {
      System.out.println(sql.getTokens().get(i).toString());
    }

    Assert.assertEquals(16, sql.getTokens().size());

  }
}
