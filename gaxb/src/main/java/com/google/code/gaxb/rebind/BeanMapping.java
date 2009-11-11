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

import com.google.gwt.core.ext.typeinfo.*;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.user.rebind.SourceWriter;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Alex Bertram
 */
public class BeanMapping {

  private XmlAccessType accessType;
  private List<BeanMemberMapping> memberMappings = new ArrayList<BeanMemberMapping>();
  private JClassType beanClass;
  private String rootName;

  public BeanMapping(TreeLogger logger, JClassType beanClass ) {
    this.beanClass = beanClass;

    // TODO: search superclasses?
    XmlAccessorType accessorType = beanClass.getAnnotation(XmlAccessorType.class);
    if(accessorType == null) {
      accessType = XmlAccessType.PUBLIC_MEMBER;
    } else {
      accessType = accessorType.value();
    }

    XmlRootElement rootElement = beanClass.getAnnotation(XmlRootElement.class);
    if(rootElement != null && rootElement.name()!=null)
        rootName = rootElement.name();
    else
        rootName = beanClass.getSimpleSourceName();

    // process the class and all of it subclasses, identifying
    // fields/properties that are bound to th
    JClassType classType = this.beanClass;
    while(classType != null) {

      for(JMethod method : classType.getMethods()) {
        if(isGetter(method) && isBound(method)) {
          addMapping(logger, new BeanProperty(logger, method));
        }
      }
      for(JField field : classType.getFields()) {
        if(isBound(field)) {
          addMapping(logger, new BeanField(logger, field));
        }
      }

      classType = classType.getSuperclass();
    }
  }

  private boolean isGetter(JMethod method) {
    if(method.isStatic() || method.isAbstract() )
      return false;

    // TODO: check case of subsequent character
    return method.getName().startsWith("get") ||
           method.getName().startsWith("is");
  }

  private boolean isBound(JMethod getter) {
    if(getter.isAnnotationPresent(XmlTransient.class))
      return false;

    // confirm that this getter has a setter or is a colleciton
    if(!isCollection(getter.getReturnType()) && !hasSetter(getter))
      return false;

    if(accessType == XmlAccessType.PROPERTY)
      return true;
    if(accessType == XmlAccessType.PUBLIC_MEMBER && getter.isPublic())
      return true;

    return getter.isAnnotationPresent(XmlElement.class) ||
           getter.isAnnotationPresent(XmlAttribute.class);
  }

  private boolean isCollection(JType type) {
    JClassType classType = type.isClass();
    if(classType == null) {
      return false;
    }
    for(JClassType i : classType.getImplementedInterfaces()) {
      if(i.getQualifiedSourceName().equals(Collection.class.getName())) {
        return true;
      }
    }
    return false;
  }

  private boolean hasSetter(JMethod getter) {
    String setter = "set" + getter.getName().substring(
      getter.getName().startsWith("is") ? 2 : 3);

    try {
      return getter.getEnclosingType().getMethod(setter, new JType[] { getter.getReturnType() }) != null;
    } catch (NotFoundException e) {
      return false;
    }
  }

  private boolean isBound(JField field) {
    if(field.isStatic())
      return false;
    if(field.isAnnotationPresent(XmlTransient.class))
      return false;
    if(accessType == XmlAccessType.PUBLIC_MEMBER && field.isPublic())
      return true;

    return field.isAnnotationPresent(XmlElement.class) ||
           field.isAnnotationPresent(XmlAttribute.class);
  }

  private void addMapping(TreeLogger logger, BeanMember member) {

    logger.log(TreeLogger.Type.DEBUG, "Adding bean member " + member.getName() + " with type " +
        member.getType().getQualifiedSourceName());

    if(member.isAnnotationPresent(XmlAttribute.class)) {
      memberMappings.add(new AttributeMapping(member));
    } else {
      memberMappings.add(new SimpleElementMapping(member));
    }
  }

  public void writeMarshaller(SourceWriter writer) {
    writer.println("@Overrides");
    writer.println("public Document marshal(" + beanClass.getQualifiedSourceName() + " bean) {");
    writer.indent();
    writer.println("Document document = XMLParser.createDocument();");
    writer.println("Element beanElement = document.createElement(\"" + rootName + "\");");
    writer.println("marshal(bean, document, beanElement);");
    writer.println("document.appendChild(beanElement);");
    writer.println("return document;");
    writer.outdent();
    writer.println("}");
    writer.println();
    writer.println("public Document marshal(" + beanClass.getQualifiedSourceName() + " bean, " +
        " Document document, Element beanElement) {");
    writer.indent();

    for(BeanMemberMapping mapping : memberMappings) {
      mapping.writeMarshallingStatements(writer);
    }

    writer.outdent();
    writer.println("}");
    writer.println();
  }

}
