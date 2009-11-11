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

package com.google.gwt.gears.persistence.mapping;

import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public interface MethodInfo {

  String getName();

  <T extends Annotation> T getAnnotation(Class<T> annotation);

  int getParameterCount();

  TypeInfo getReturnType();

  boolean isPublic();

  boolean isStatic();

  boolean isFinal();

  List<String> getParameterTypeNames();


  public static class GwtImpl implements MethodInfo {
    private JMethod method;

    public GwtImpl(JMethod method) {
      this.method = method;
    }

    @Override
    public String getName() {
      return method.getName();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotation) {
      return method.getAnnotation(annotation);
    }

    @Override
    public int getParameterCount() {
      return method.getParameters().length;
    }

    @Override
    public TypeInfo getReturnType() {
      return new TypeInfo.GwtImpl(method.getReturnType());
    }

    @Override
    public boolean isPublic() {
      return method.isPublic();
    }

    @Override
    public boolean isStatic() {
      return method.isStatic();
    }

    public boolean isFinal() {
      return method.isFinal();
    }

    @Override
    public List<String> getParameterTypeNames() {
      List<String> types = new ArrayList<String>();
      for (JParameter t : method.getParameters()) {
        types.add(t.getType().getParameterizedQualifiedSourceName());
      }
      return types;
    }
  }


  public static class RuntimeImpl implements MethodInfo {
    private Method method;

    public RuntimeImpl(Method method) {
      this.method = method;
    }

    @Override
    public String getName() {
      return method.getName();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotation) {
      return method.getAnnotation(annotation);
    }

    @Override
    public int getParameterCount() {
      return method.getParameterTypes().length;
    }

    @Override
    public TypeInfo getReturnType() {
      return new TypeInfo.RuntimeImpl(method.getReturnType());
    }

    @Override
    public boolean isPublic() {
      return Modifier.isPublic(method.getModifiers());
    }

    @Override
    public boolean isStatic() {
      return Modifier.isStatic(method.getModifiers());
    }

    public boolean isFinal() {
      return Modifier.isFinal(method.getModifiers());
    }

    @Override
    public List<String> getParameterTypeNames() {
      List<String> types = new ArrayList<String>();
      for (Type t : method.getGenericParameterTypes()) {
        types.add(composeTypeDeclaration(t));
      }
      return types;
    }

    private String composeTypeDeclaration(Type type) {
      if (type instanceof Class) {

        Class clazz = (Class) type;
        StringBuilder sb = new StringBuilder();
        if (clazz.getEnclosingClass() != null) {
          sb.append(composeTypeDeclaration(clazz));
          sb.append(".");
        } else if (clazz.getPackage() != null) {
          sb.append(clazz.getPackage().getName());
          sb.append(".");
        }
        sb.append(clazz.getSimpleName());
        return sb.toString();

      } else if (type instanceof GenericArrayType) {
        GenericArrayType arrayType = (GenericArrayType) type;
        return composeTypeDeclaration(arrayType.getGenericComponentType()) + "[]";

      } else if (type instanceof TypeVariable) {
        TypeVariable typeVar = (TypeVariable) type;
        return typeVar.getName();

      } else if (type instanceof WildcardType) {
        WildcardType wildcard = (WildcardType) type;
        StringBuilder sb = new StringBuilder("?");
        for (int i = 0; i != wildcard.getLowerBounds().length; ++i) {
          sb.append(i == 0 ? " super " : ", ");
          sb.append(composeTypeDeclaration(wildcard.getLowerBounds()[i]));
        }
        for (int i = 0; i != wildcard.getUpperBounds().length; ++i) {
          sb.append(i == 0 ? " extends " : ", ");
          sb.append(composeTypeDeclaration(wildcard.getUpperBounds()[i]));
        }
        return sb.toString();

      } else if (type instanceof ParameterizedType) {
        ParameterizedType ptype = (ParameterizedType) type;
        StringBuilder sb = new StringBuilder();
        sb.append(composeTypeDeclaration(ptype.getRawType()));
        sb.append("<");
        for (int i = 0; i != ptype.getActualTypeArguments().length; ++i) {
          if (i != 0) sb.append(", ");
          sb.append(composeTypeDeclaration(ptype.getActualTypeArguments()[i]));
        }
        sb.append(">");
        return sb.toString();
      } else {
        throw new Error("Unexpected subclass" + type.getClass().getName());
      }
    }
  }


}
