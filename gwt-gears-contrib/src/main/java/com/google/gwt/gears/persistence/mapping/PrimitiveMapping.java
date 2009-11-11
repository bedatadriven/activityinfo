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

import com.google.gwt.user.rebind.SourceWriter;

/**
 * @author Alex Bertram
 */
public class PrimitiveMapping extends SingleColumnPropertyMapping {

  private SqliteType sqlTypeName;
  private String readerName;
  private String stmtSetter;

  private boolean boxed;

  public PrimitiveMapping(MethodInfo getter) {
    super(getter);

    TypeInfo type = getter.getReturnType();
    Class primitive = type.isPrimitive();

    this.boxed = (primitive == null);

    if (primitive != null) {
      this.nullable = false;
    }

    if (primitive == Integer.TYPE || type.getQualifiedName().equals(Integer.class.getName()) ||
        primitive == Short.TYPE || type.getQualifiedName().equals(Short.class.getName()) ||
        primitive == Long.TYPE || type.getQualifiedName().equals(Long.class.getName()) ||
        primitive == Byte.TYPE || type.getQualifiedName().equals(Byte.class.getName()) ||
        primitive == Boolean.TYPE || type.getQualifiedName().equals(Boolean.class.getName())) {

      sqlTypeName = SqliteType.integer;

    } else if (primitive == Float.TYPE || type.getQualifiedName().equals(Float.class.getName()) ||
        primitive == Double.TYPE || type.getQualifiedName().equals(Double.class.getName())) {

      sqlTypeName = SqliteType.real;

    } else if (primitive == Character.TYPE || type.getQualifiedName().equals(Character.class.getName())) {

      sqlTypeName = SqliteType.text;
    }

    String suffix = type.getSimpleName();

    if (suffix.equals("Integer")) {
      suffix = "Int";
    } else if (suffix.equals("Character")) {
      suffix = "Char";
    }
    suffix = suffix.substring(0, 1).toUpperCase() + suffix.substring(1);

    readerName = "Readers.read" + suffix;
    stmtSetter = "set" + suffix;

  }

  @Override
  public boolean isPrimitive() {
    return !boxed;
  }

  @Override
  protected SqliteType getSqlTypeName() {
    return sqlTypeName;
  }

  @Override
  public String getReaderName() {
    return readerName;
  }

  @Override
  protected String getStmtSetter() {
    return stmtSetter;
  }
}
