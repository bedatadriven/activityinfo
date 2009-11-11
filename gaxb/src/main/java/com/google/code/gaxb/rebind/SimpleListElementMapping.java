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

package com.google.code.gaxb.rebind;

import com.google.gwt.user.rebind.SourceWriter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @author Alex Bertram
 */
public class SimpleListElementMapping extends SimpleElementMapping {

  private String elementName;
  private String wrapperElementName;
  private BeanMember member;

  public SimpleListElementMapping(BeanMember member ) {
    super(member);

    // check for a wrapper element
    XmlElementWrapper wrapper = member.getAnnotation(XmlElementWrapper.class);
    if(wrapper != null && wrapper.name() != null) {
      wrapperElementName = wrapper.toString();
    }
  }

  @Override
  public void writeMarshallingStatements(SourceWriter writer) {
    writer.println("if(" + member.getAccessorExpression() + " != null) {");
    writer.indent();
    writer.println("for(" + type.getQualifiedSourceName() + " : " );
    writer.println("addSimpleElementValue(document, beanElement, \"" + elementName + "\", " +
       marshalExpr(member.getAccessorExpression()) + ");");

  }

  @Override
  public void writeUnmarshallCode(SourceWriter writer) {

  }
}
