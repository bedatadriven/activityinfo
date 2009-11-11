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

package com.google.gwt.gears.persistence.server;

import com.google.gwt.gears.persistence.mapping.ColumnStreamWriter;
import com.google.gwt.gears.persistence.mapping.EntityMapping;
import com.google.gwt.gears.persistence.mapping.UnitMapping;
import com.google.gwt.gears.persistence.mapping.PropertyMapping;
import org.json.JSONException;
import org.json.JSONStringer;

import java.util.List;

/**
 * Generates a jsonnified BulkOperation object that can be sent
 * to and executed by the client against the local sqlite database
 *
 * @author Alex Bertram
 */
public class BulkStatementBuilder {

  private JSONStringer json;
  private UnitMapping context;

  public BulkStatementBuilder() {
    json = new JSONStringer();
    try {
      json.array();
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
    context = new UnitMapping();
  }

  private class JsonColumnStreamWriter implements ColumnStreamWriter {

    @Override
    public void writeColumn(String value) {
      try {
        json.value(value == null ? "" : value);
      } catch (JSONException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public void writeNulls(int count) {
      while (count != 0) writeColumn(null);
    }
  }

  public <T> void insert(Class<T> entityClass, List<T> entities) throws JSONException {

    if (entities.size() == 0) {
      return; // nothing to do
    }
    // get/create the mapping for this entity
    EntityMapping mapping = context.getMapping(entityClass);

    // start a new BulkOperation object
    json.object();

    // first build the insert statement
    json.key("statement");
    json.value(mapping.getInsertStatement());

    json.key("batches");
    json.array();

    // loop through each of the entities in the list
    for (T entity : entities) {

      json.array();

      ColumnStreamWriter writer = new JsonColumnStreamWriter();

      // write the id columns
      if (mapping.getId().isInsertable())
        mapping.getId().writeColumnValues(writer, entity);

      // write the other columns
      for (PropertyMapping property : mapping.getProperties()) {
        if (property.isInsertable()) {
          property.writeColumnValues(writer, entity);
        }
      }
      json.endArray();
    }

    json.endArray(); // end our batches
    json.endObject(); //end the BulkOperation object
  }

  public <T> void update(Class<T> entityClass, List<T> entities) throws JSONException {

    if (entities.size() == 0) {
      return; // nothing to do
    }
    // get/create the mapping for this entity
    EntityMapping mapping = context.getMapping(entityClass);

    // start a new BulkOperation object
    json.object();

    // first build the insert statement
    json.key("statement");
    json.value(mapping.getUpdateStatement());

    json.key("batches");
    json.array();

    // loop through each of the entities in the list
    for (T entity : entities) {

      json.array();

      ColumnStreamWriter writer = new JsonColumnStreamWriter();

      // write the columns to update
      for (PropertyMapping property : mapping.getProperties()) {
        if (property.isUpdatable()) {
          property.writeColumnValues(writer, entity);
        }
      }

      // write the ids used to select the row
      mapping.getId().writeColumnValues(writer, entity);

      json.endArray();
    }

    json.endArray(); // end our batches
    json.endObject(); //end the BulkOperation object
  }

  public <T> void delete(Class<T> entityClass, List<T> entities) throws JSONException {

    if (entities.size() == 0) {
      return; // nothing to do
    }
    // get/create the mapping for this entity
    EntityMapping mapping = context.getMapping(entityClass);

    // start a new BulkOperation object
    json.object();

    // first build the insert statement
    json.key("statement");
    json.value(mapping.getDeleteStatement());

    json.key("batches");
    json.array();

    // loop through each of the entities in the list
    for (T entity : entities) {

      json.array();

      ColumnStreamWriter writer = new JsonColumnStreamWriter();
      mapping.getId().writeColumnValues(writer, entity);

      json.endArray();
    }

    json.endArray(); // end our batches
    json.endObject(); //end the BulkOperation object
  }


  public String asJson() throws JSONException {
    json.endArray();
    return json.toString();
  }

}
