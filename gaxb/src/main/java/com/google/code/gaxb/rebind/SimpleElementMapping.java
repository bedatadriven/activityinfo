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

import com.google.gwt.core.ext.typeinfo.HasAnnotations;
import com.google.gwt.user.rebind.SourceWriter;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Alex Bertram
 */
public class SimpleElementMapping extends BeanMemberMapping {

  protected String elementName;
  protected BeanMember member;

  public SimpleElementMapping(BeanMember member) {
    super(member.getType());
    this.member = member;

    // find the name of the element
    XmlElement element = member.getAnnotation(XmlElement.class);
    if(element != null && element.name()!= null) {
      elementName = element.name();
    } else {
      elementName = member.getName();
    }
  }

  @Override
  public void writeMarshallingStatements(SourceWriter writer) {
    writer.println("addSimpleElementValue(document, beanElement, \"" + elementName + "\", " +
      marshalExpr(member.getAccessorExpression()) + ");");
  }

  @Override
  public void writeUnmarshallCode(SourceWriter writer) {
    member.writeAssignmentStatement(writer,
        unmarshalExpr("getSimpleElementValue(beanElement, \"" + elementName + "\")"));
  }
}
