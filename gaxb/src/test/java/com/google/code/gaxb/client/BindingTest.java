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

package com.google.code.gaxb.client;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.core.client.GWT;
import com.google.gwt.xml.client.Document;
import com.google.code.gaxb.client.testbeans.Person;
import junit.framework.Assert;

/**
 * @author Alex Bertram
 */
public class BindingTest extends GWTTestCase {

  public interface PersonBinding extends XmlBinding<Person> {

  }

  @Override
  public String getModuleName() {
    return "com.google.code.gaxb.Gaxb";
  }

  public void testBinding() {

    PersonBinding binding = GWT.create(PersonBinding.class);
    Person p = Person.createCompleteInstance();

    Document doc = binding.marshal(p);
    assertEquals("person", doc.getDocumentElement().getNodeName());

  }
}
