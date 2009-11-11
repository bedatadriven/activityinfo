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

/**
 * @author Alex Bertram
 */
public class FieldUtil {


  /**
   * Returns true if <code>name</code> is a camel-cased name prefixed
   * by <code>prefix</code>. For example, prefixedBy("getCount", "get")
   * returns true, but prefixedBy("getcount","get") returns false.
   *
   * @param name   The name to check
   * @param prefix The prefix
   * @return True if name is camel-cased and starts with <code>prefix</code>
   */
  public static boolean prefixedBy(String name, String prefix) {
    if (!name.startsWith(prefix))
      return false;

    if ((name.length() == prefix.length()) ||
        !Character.isUpperCase(name.charAt(prefix.length()))) {
      return false;
    }

    return true;
  }
}
