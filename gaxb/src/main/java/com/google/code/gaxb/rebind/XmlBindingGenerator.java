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

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.code.gaxb.client.XmlBinding;
import com.google.code.gaxb.client.AbstractXmlBinding;

import java.io.PrintWriter;

/**
 * @author Alex Bertram
 */
public class XmlBindingGenerator extends Generator {

  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String typeName)
      throws UnableToCompleteException {

   try {
    TypeOracle typeOracle = context.getTypeOracle();
    JClassType requestedClass = typeOracle.getType(typeName);
    JClassType beanClass = getBeanClass(logger, requestedClass);

    String packageName = requestedClass.getPackage().getName();
    String bindingClassName = requestedClass.getSimpleSourceName();
    String bindingImplClassName = bindingClassName + "Impl";
    String qualifiedBindingImplClassName = packageName + "." + bindingImplClassName;

    PrintWriter printWriter = context.tryCreate(logger, packageName, bindingImplClassName);
    if (printWriter == null) {
      //throw new UnableToCompleteException();
      // should be ok, the file already exists
      return qualifiedBindingImplClassName;
    }

    ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(packageName, bindingImplClassName);
    composerFactory.setSuperclass(AbstractXmlBinding.class.getName() +
        "<" + beanClass.getQualifiedSourceName() + ">" );

    composerFactory.addImplementedInterface(typeName);
    composerFactory.addImport("com.google.gwt.xml.client.XMLParser");
    composerFactory.addImport("com.google.gwt.xml.client.Document");
    composerFactory.addImport("com.google.gwt.xml.client.Element");

    SourceWriter writer = composerFactory.createSourceWriter(context, printWriter);

     BeanMapping mapping = new BeanMapping(logger, beanClass);
     mapping.writeMarshaller(writer);
     

    writer.commit(logger);

    return qualifiedBindingImplClassName;
    } catch(Exception e) {
      logger.log(TreeLogger.Type.ERROR, "Error generating Marshaller", e);
      throw new UnableToCompleteException();
    }

  }

   /**
     * Gets the bean type from the requested class.
     *
     * The Requested class must extend DAO&lt;BeanType&gt;, where BeanType is
     * the type that will be persistance ready.
     *
     * @param logger
     * @param requestedClass
     * @return
     * @throws UnableToCompleteException
     */
    private JClassType getBeanClass(TreeLogger logger, JClassType requestedClass) throws UnableToCompleteException {
        JClassType[] ifaces = requestedClass.getImplementedInterfaces();
        for(int i=0;i!=ifaces.length;++i) {
            if(XmlBinding.class.getName().equals(ifaces[i].getQualifiedSourceName())) {
                // the type parameter of the DAO interface gives us the name
                // of the bean that we're mapping
                JParameterizedType ptype = (JParameterizedType) ifaces[i];
                JClassType[] args = ptype.getTypeArgs();

                logger.log(TreeLogger.Type.INFO, "Generating Marshaller for bean class " + args[0].getName());

                return args[0];
            }
        }
        logger.log(TreeLogger.Type.ERROR, "The target class must implement " + XmlBinding.class.getName());
        throw new UnableToCompleteException();
    }
}
