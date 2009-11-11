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
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.HasAnnotations;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Alex Bertram
 */
public abstract class BeanMemberMapping {


  protected final JType type;

  protected BeanMemberMapping(JType type) {
    this.type = type;
  }

  public abstract void writeMarshallingStatements(SourceWriter writer);

  public abstract void writeUnmarshallCode(SourceWriter writer);

  private String getFnSuffix(JType type) {
    JPrimitiveType primitive = type.isPrimitive();
    if(primitive != null) {
      return primitive.getSimpleSourceName();
    }
    return type.getQualifiedSourceName().replace(".", "_");
  }

  protected String marshalExpr(String accessorExpr) {
    return "marshal_" + getFnSuffix(type) + "(" + accessorExpr + ")";
  }

  protected String unmarshalExpr(String accessorExpr) {
    return "unmarshal_" + getFnSuffix(type) + "(" + accessorExpr + ")";
  }


}
