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

import java.util.Date;

/**
 * @author Alex Bertram
 */
public class DateMapping extends SingleColumnPropertyMapping {


  public DateMapping(MethodInfo getterMethod) {
    super(getterMethod);
  }

  // TODO: we should probably be doing something we zeroing out times/dates
  // depending on the @Temporal annotation


  @Override
  protected SqliteType getSqlTypeName() {
    return SqliteType.integer;
  }

  @Override
  public String getReaderName() {
    return "Readers.readDate";
  }

  @Override
  protected String getStmtSetter() {
    return "setDate";
  }

  @Override
  protected String convertToString(Object value) {
    return Long.toString(((Date) value).getTime());
  }


}
