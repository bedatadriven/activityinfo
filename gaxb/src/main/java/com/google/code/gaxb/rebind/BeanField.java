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

import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.user.rebind.SourceWriter;

import java.lang.annotation.Annotation;

/**
 * @author Alex Bertram
 */
public class BeanField extends BeanMember {

  private JField field;

  public BeanField(TreeLogger logger, JField field) {
    super(field.getType());
    this.field = field;
  }

  @Override
  public String getName() {
    return field.getName();
  }

  @Override
  public String getAccessorExpression() {
    return "bean." + field.getName();
  }

  @Override
  public void writeAssignmentStatement(SourceWriter writer, String expression) {
    writer.println("bean." + field.getName() + " = " + expression + ";");
  }

  @Override
  public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
    return field.getAnnotation(annotationClass);
  }
}
