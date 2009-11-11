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

import com.google.gwt.xml.client.*;

import java.util.Date;

/**
 * @author Alex Bertram
 */
public abstract class AbstractXmlBinding<T> implements XmlBinding<T> {

  protected String marshal_java_lang_String(String s) {
    return s;
  }

  protected String unmarshal_java_lang_String(String s) {
    return s;
  }

  protected String marshal_int(int i) {
    return Integer.toString(i);
  }

  protected int unmarshal_int(String s) {
    if(s==null)
      return 0;
    return Integer.parseInt(s);
  }


  protected void addSimpleElementValue(Document doc, Element beanElement, String name, String value) {
    if(value != null) {
      Element memberElement = doc.createElement(name);
      memberElement.appendChild(doc.createTextNode(value));
      beanElement.appendChild(memberElement);
    }
  }

  protected String getSimpleElementValue(Element beanElement, String name) {
    NodeList list = beanElement.getElementsByTagName(name);
    if(list.getLength() == 0) {
      return null;
    } else {
      Element memberElement = (Element) list.item(0);
      StringBuilder sb = new StringBuilder();
      for(int i=0,len=memberElement.getChildNodes().getLength(); i!=len;++i) {
        Node node = memberElement.getChildNodes().item(0);
        if(node.getNodeType() == Node.TEXT_NODE ||
           node.getNodeType() == Node.CDATA_SECTION_NODE) {

          sb.append(node.getNodeValue());

        } else {
          break;   // TODO: should consider throwing an error here
        }
      }
      return sb.toString();
    }
  }



}
