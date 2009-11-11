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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * Verifies that a module containing jaxb annotations
 * can correctly compile
 *
 * @author Alex Bertram
 */
public class AnnotationTest extends GWTTestCase {

  public static class MyMap {

    @XmlAttribute
    public String name;

    @XmlAttribute
    public String age;
  }

  public static class MyAdapter extends XmlAdapter<Map<Integer,String>, MyMap> {
    public MyMap unmarshal(Map<Integer, String> v) throws Exception {
      return null;
    }
    public Map<Integer, String> marshal(MyMap v) throws Exception {
      return null;
    }
  }

  @XmlRootElement(name="mybean")
  public static class MyBean {

    @XmlElement
    public String name;

    @XmlAttribute
    public int id;

    @XmlElement(name="child")
    @XmlElementWrapper(name="Children")
    @XmlJavaTypeAdapter(MyAdapter.class)
    public Map<String, Integer> children;

  }

  @Override
  public String getModuleName() {
    return "com.google.code.gaxb.Annotations";
  }

  public void testCompile() {

    MyBean bean = new MyBean();
    bean.name = "Fred";
    bean.id = 1;
    bean.children = new HashMap<String, Integer>();
    bean.children.put("Pebbles", 3);

    MyAdapter adapter = new MyAdapter();

    assertTrue(true);
  }
}
