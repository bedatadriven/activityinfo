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

import com.google.gwt.gears.persistence.client.impl.SQLStatement;

import java.util.Map;
import java.util.Collection;

/**
 * @author Alex Bertram
 */
public class NativeQueryMapping {

  private UnitMapping unit;
  private String name;
  private SQLStatement sql;
  private Map<String, ParameterMapping> namedParameters;
  private Map<Integer, ParameterMapping> positionalParameters;

  public NativeQueryMapping(UnitMapping unit, String name, String sql) {
    this.unit = unit;
    this.name = name;
    this.sql = new SQLStatement(sql);

    int index = 1;
    for(SQLStatement.Token token : this.sql.getTokens()) {
      if(token.isParameter()) {
        if(token.getParameterName()!=null) {
          ParameterMapping param = namedParameters.get(token.getParameterName());
          if(param == null) {
            param = new ParameterMapping(token.getParameterName());
            namedParameters.put(token.getParameterName(), param);
          }
          param.addIndex(index++);
        } else {
          ParameterMapping param = positionalParameters.get(token.getParameterPosition());
          if(param == null) {
            param = new ParameterMapping(token.getParameterPosition());
            positionalParameters.put(token.getParameterPosition(), param);
          }
          param.addIndex(index++);
        }
      }
    }
  }

  public UnitMapping getUnit() {
    return unit;
  }

  public String getName() {
    return name;
  }

  public SQLStatement getSql() {
    return sql;
  }

  public Collection<ParameterMapping> getNamedParameters() {
    return namedParameters.values();
  }

  public Collection<ParameterMapping> getPositionalParameters() {
    return positionalParameters.values();
  }

}
