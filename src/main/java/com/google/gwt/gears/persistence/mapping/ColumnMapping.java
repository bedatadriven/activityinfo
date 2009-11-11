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

import freemarker.template.TemplateMethodModel;

/**
 * @author Alex Bertram
 */
public class ColumnMapping {

  private String name;
  private String stmtSetter;
  private String referencedName;
  private SqliteType type;

  public ColumnMapping(String name, SqliteType type, String stmtSetter) {
    this.name = name;
    this.stmtSetter = stmtSetter;
    this.type = type;
  }

  public ColumnMapping(String name, SqliteType type, String stmtSetter, String referencedName) {
    this.name = name;
    this.referencedName = referencedName;
    this.type = type;
    this.stmtSetter = stmtSetter;
  }

  public String getName() {
    return name;
  }

  public SqliteType getType() {
    return type;
  }

  public String getTypeName() {
    return type.toString();
  }

  public String getReferencedName() {
    return referencedName;
  }

  public String getStmtSetter() {
    return stmtSetter;
  }

  public String getIndexVar() {
    return name + "Index";
  }
}
