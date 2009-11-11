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

package com.google.gwt.gears.bulk.client;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.database.DatabaseException;
import com.google.gwt.gears.client.database.ResultSet;

/**
 * @author Alex Bertram
 */
public class BulkOperation extends JavaScriptObject {

  protected BulkOperation() {

  }

  /**
   * Gets the parameterized SQL statement to execute.
   * For example:
   * <p/>
   * <ul>
   * <li><code>UPDATE MyTable SET Name = ? WHERE ID = ?</code></li>
   * <li><code>INSERT INTO MyTable (ID, NAME) VALUES (?, ?)</li>
   * <li><code>
   * </ul>
   *
   * @return the para
   */
  public native String getStatement() /*-{
        return this.statement;
    }-*/;

  public native void setStatement(String sql) /*-{
        this.statement = sql;
    }-*/;

  public native JsArray<JsArrayString> getBatches() /*-{
        return this.batches;
    }-*/;

  public native void setBatches(JsArray<JsArrayString> batches) /*-{
        this.parameters = batches;
    }-*/;


  public int execute(Database database) throws DatabaseException {
    try {
      int rowsAffectedCount = 0;
      for (int i = 0; i != getBatches().length(); ++i) {
        execute(database, getStatement(), getBatches().get(0));
        rowsAffectedCount += database.getRowsAffected();
      }
      return rowsAffectedCount;
    } catch (JavaScriptException e) {
      throw new DatabaseException(e.getDescription(), e);
    }
  }

  private native ResultSet execute(Database db, String sqlStatement, JavaScriptObject args) /*-{
        return db.execute(sqlStatement, args);
    }-*/;


}
