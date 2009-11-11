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

import java.util.ArrayList;
import java.util.List;

/**
 * A quick-and-dirty representation of an SQL statement that can
 * be used to substitute parameters and lookup aliases (kind-of).
 * <p/>
 * At some point it may be worth replacing with a real AST...
 *
 * @author Alex Bertram
 */
public class SQLStatement {

  public static final int TYPE_SELECT = 1;
  public static final int TYPE_UPDATE = 2;
  public static final int TYPE_INSERT = 3;
  public static final int TYPE_DELETE = 4;
  public static final int TYPE_OTHER = 0;


  public static class Token {

    private String text;
    private String parameterName;
    private int parameterPosition;
    private int parameterIndex;
    private boolean parameter;

    public Token(StringBuilder sb) {
      this(sb.toString());
    }

    public Token(String text) {
      this.text = text;

      if (this.text.startsWith(":")) {
        parameterName = this.text.substring(1);
        parameter = true;
      } else if (this.text.startsWith("?")) {
        parameterPosition = Integer.parseInt(this.text.substring(1));
        parameter = true;
      }
    }

    @Override
    public String toString() {
      return text;
    }

    public String getParameterName() {
      return parameterName;
    }

    public int getParameterPosition() {
      return parameterPosition;
    }

    public boolean isParameter() {
      return parameter;
    }

    public int getParameterIndex() {
      return parameterIndex;
    }

    public void setParameterIndex(int parameterIndex) {
      this.parameterIndex = parameterIndex;
    }

    public void changeText(String newText) {
      this.text = newText;
    }
  }

  /**
   * The orginal SQL statement;
   */
  private String sql;

  private List<Token> tokens;

  private int queryType;

  /**
   * The number of SQL parameters in this query. This is
   * then number of placeholders that will appear in the final
   * query, so it can be greater than the number of unique named
   * or positional parameters.
   */
  private int sqlParameterCount = 0;


  /**
   * Parses an SQL statement into basic tokens and locates positional and named
   * parameters.
   *
   * @param sql
   */
  public SQLStatement(String sql) {

    this.sql = sql;

    // sample statements to parse
    // select * from Indicators i where i.id = :?
    // select i.id,name from Indicators where bar="foo,sue"

    tokens = new ArrayList<Token>();
    StringBuilder tokenBuilder = new StringBuilder();

    boolean quoted = false;
    boolean squoted = false;
    boolean brackets = false;
    char lastc = 0;
    for (int i = 0; i != sql.length(); ++i) {
      char c = sql.charAt(i);
      if (!quoted && !squoted && !brackets &&
          (c == ' ' || c == ',' ||
              (isOperator(c) && !isOperator(lastc)) ||
              (isOperator(lastc) && !isOperator(c)))) {
        if (tokenBuilder.length() != 0) {
          addToken(tokenBuilder);
          tokenBuilder = new StringBuilder();
        }
        if (c == ',') {
          tokens.add(new Token(","));
        } else if (c != ' ') {
          tokenBuilder.append(c);
        }
      } else {
        if (!squoted && c == '\"') quoted = !quoted;
        if (!quoted && c == '\'') squoted = !squoted;

        if (!squoted && !quoted && c == '[') brackets = true;
        else if (!squoted && !quoted && c == ']') brackets = false;

        tokenBuilder.append(c);
      }
      lastc = c;
    }

    if (tokenBuilder.length() != 0) {
      addToken(tokenBuilder);
    }

    if (tokens.size() == 0) {
      throw new IllegalArgumentException("SQL query contains no text");
    }

    String firstKeyword = tokens.get(0).toString().toLowerCase();
    if ("select".equals(firstKeyword)) {
      queryType = TYPE_SELECT;
    } else if ("update".equals(firstKeyword)) {
      queryType = TYPE_UPDATE;
    } else if ("insert".equals(firstKeyword)) {
      queryType = TYPE_INSERT;
    } else if ("delete".equals(firstKeyword)) {
      queryType = TYPE_DELETE;
    } else {
      queryType = TYPE_OTHER;
    }
  }

  private boolean isOperator(char c) {
    return c == '=' || c == '<' || c == '>';
  }

  private void addToken(StringBuilder tokenBuilder) {
    Token token = new Token(tokenBuilder);
    if (token.isParameter()) {
      sqlParameterCount++;
      token.setParameterIndex(sqlParameterCount);
    }
    tokens.add(token);
  }

  public void substituteToken(int i, String newText) {
    assert !tokens.get(i).isParameter() : "You shouldn't mess with tokens, it will mess up the indices";

    tokens.get(i).changeText(newText);

  }

  /**
   * Substitues all named and positional parameters for
   * pure SQL placeholders ("?")
   *
   * @return a native SQL string
   */
  public String toNativeSQL() {

    // if this statement doesn't use named or positional
    // parameters, simply return the original statement
    if (sqlParameterCount == 0) {
      return sql;
    } else {
      StringBuilder sb = new StringBuilder();
      for (Token token : tokens) {
        sb.append(" ");
        if (token.isParameter()) {
          sb.append("?");
        } else {
          sb.append(token);
        }
      }
      return sb.toString();
    }
  }

  public List<Token> getTokens() {
    return tokens;
  }

  public int getQueryType() {
    return queryType;
  }

  public List<Integer> getParameterIndices(int position) {
    List<Integer> indices = new ArrayList<Integer>();
    for (Token token : tokens) {
      if (token.isParameter() && token.getParameterPosition() > 0) {
        indices.add(token.getParameterIndex());
      }
    }
    return indices;
  }


  public boolean hasParameter(String name) {
    for (Token token : tokens) {
      if (token.isParameter() && token.getParameterName() != null &&
          token.getParameterName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasParameter(int position) {
    for (Token token : tokens) {
      if (token.isParameter() && token.getParameterPosition() == position) {
        return true;
      }
    }
    return false;
  }

//    public String findTableAlias(String tableName) {
//        int i = 0;
//
//        // move to the begining of the from clause
//        while(!"from".equals(tokens.get(i++).toString().toLowerCase())) {  }
//
//        // look for a token that matches our tableName
//        for(; i!=tokens.size();++i) {
//            if(tokens.get(i).toString().toLowerCase().equals(tableName)) {
//
//            }
//        }
//


}
