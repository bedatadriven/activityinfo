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

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Alex Bertram
 */
public class AttributeMapping extends BeanMemberMapping {

  /**
   * The name of the XML attribute
   */
  private String attribName;
  private final BeanMember member;

  public AttributeMapping(BeanMember member) {
    super(member.getType());

    this.member = member;

    // determine the attribute name
    XmlAttribute anno = member.getAnnotation(XmlAttribute.class);
    if(anno != null && anno.name() != null)
      attribName =  anno.name();
    else
      attribName = member.getName();
  }


  @Override
  public void writeMarshallingStatements(SourceWriter writer) {

    // will look like:
    // beanElement.setAttribute(name, marshal_java_lang_String(bean.getValue()));

    writer.println("beanElement.setAttribute(\"" + attribName + "\", " +
        marshalExpr(member.getAccessorExpression()) +
     ")");

  }

  @Override
  public void writeUnmarshallCode(SourceWriter writer) {

    // will look like:
    // bean.name = unmarshal_java_lang_Integer("name")

    member.writeAssignmentStatement(writer,
        unmarshalExpr( "beanElement.getAttribute(\"" + attribName + "\")" ));

  }
}
