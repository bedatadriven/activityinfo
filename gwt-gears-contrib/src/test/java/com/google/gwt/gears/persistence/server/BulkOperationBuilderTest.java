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

import com.google.gwt.gears.persistence.client.domain.Simple;
import org.json.JSONException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram
 */
public class BulkOperationBuilderTest {

  @Test
  public void testInsert() throws JSONException {

    List<Simple> list = new ArrayList<Simple>();
    list.add(new Simple(1, "Hello World", true));
    list.add(new Simple(3, null, false));
    list.add(new Simple(9, "Here we are", false));

    BulkStatementBuilder builder = new BulkStatementBuilder();
    builder.insert(Simple.class, list);

    System.out.println(builder.asJson());

  }

}
