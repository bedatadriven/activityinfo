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
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.TreeLogger;

import java.lang.annotation.Annotation;

/**
 * @author Alex Bertram
 */
public class BeanProperty extends BeanMember {

  private String name;
  private String getterName;
  private String setterName;
  private JMethod getter;


  public BeanProperty(TreeLogger logger, JMethod getter) {
    super(getter.getReturnType());



    this.getter = getter;
    int prefixLen = this.getter.getName().startsWith("is") ? 2 : 3;

    name = getter.getName().substring(prefixLen);
    getterName = getter.getName();
    setterName = "set" + getter.getName().substring(prefixLen);

  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getAccessorExpression() {
    return "bean." + getterName + "()";
  }

  @Override
  public void writeAssignmentStatement(SourceWriter writer, String expression) {
     writer.println("bean." + setterName + "(" + expression + ")");
  }

  @Override
  public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
    return getter.getAnnotation(annotationClass);
  }

}
