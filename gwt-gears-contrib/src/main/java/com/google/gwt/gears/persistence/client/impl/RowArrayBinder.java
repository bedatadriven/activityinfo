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

package com.google.gwt.gears.persistence.client.impl;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Alex Bertram
 */
public class RowArrayBinder  {

  private int fieldCount = -1;

  public Object[] newInstance(ResultSet rs) {
    try {
      if (fieldCount == -1) {
        fieldCount = rs.getMetaData().getColumnCount();
      }
      Object[] row = new Object[fieldCount];
      for (int i = 0; i != fieldCount; ++i) {
        row[i] = rs.getObject(i + 1);
      }
      return row;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
