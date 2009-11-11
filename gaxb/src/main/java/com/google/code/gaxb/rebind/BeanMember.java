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

import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.SourceWriter;

import java.lang.annotation.Annotation;

/**
 * @author Alex Bertram
 */
public abstract class BeanMember {

  protected final JType type;

  public BeanMember(JType type) {
    this.type = type;
  }

  public JType getType() {
    return type;
  }

  public abstract String getName();

  public abstract String getAccessorExpression();

  public abstract void writeAssignmentStatement(SourceWriter writer, String expression);

  public abstract <T extends Annotation> T getAnnotation(Class<T> annotationClass);

  public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
    return getAnnotation(annotationClass) != null;
  }
  
  public JType getItemType() {
    JClassType classType = type.isClass();
    if(classType == null)
      return null;

    return null; // TODO
  }

}
