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
public class Readers {

  public static String readString(ResultSet rs, int index) throws SQLException {
    return rs.getString(index);
  }

  public static Boolean readBoolean(ResultSet rs, int index) throws SQLException {
    boolean value = rs.getBoolean(index);
    return rs.wasNull() ? null : value;
  }

  public static Double readDouble(ResultSet rs, int index) throws SQLException {
    double value = rs.getDouble(index);
    return rs.wasNull() ? null : value;
  }

  public static Float readFloat(ResultSet rs, int index) throws SQLException {
    float value = rs.getFloat(index);
    return rs.wasNull() ? null : value;
  }

  public static Integer readInt(ResultSet rs, int index) throws SQLException {
    int value = rs.getInt(index);
    return rs.wasNull() ? null : value;
  }

  public static Short readShort(ResultSet rs, int index) throws SQLException {
    short value = rs.getShort(index);
    return rs.wasNull() ? null : value;
  }


}
