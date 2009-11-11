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

package com.google.gwt.gears.persistence.rebind;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.gears.persistence.client.BindEntities;
import com.google.gwt.gears.persistence.client.BindPackages;
import com.google.gwt.gears.persistence.mapping.EntityMapping;
import com.google.gwt.gears.persistence.mapping.UnitMapping;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

import javax.persistence.Entity;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Alex Bertram
 */
public class PersistenceUnitGenerator extends Generator {


  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {

    logger = logger.branch(TreeLogger.Type.DEBUG, "Creating EntityManagerFactory for " + typeName);

    try {
      TypeOracle typeOracle = context.getTypeOracle();
      JClassType requestedContext = typeOracle.getType(typeName);

      UnitMapping mapping = new UnitMapping(requestedContext);

      /*
      * Step 1: Make a list of all the entites to be managed
      */

      // Check our annotations for a list of Entities to manage
      BindEntities entities = requestedContext.getAnnotation(BindEntities.class);
      if (entities != null) {
        for (Class entityClass : entities.value()) {
          if(entityClass.getAnnotation(Entity.class)==null) {
            logger.log(TreeLogger.Type.ERROR, "Cannot bind " + entityClass.getName() + " to the" +
                "persistence unit " + requestedContext.getSimpleSourceName() + ": no @Entity annotation. ");
            throw new UnableToCompleteException();
          }
          mapping.getMapping(typeOracle.findType(entityClass.getName()));
        }
      }

      // Check our annotations for a list of packages to manage
      BindPackages packages = requestedContext.getAnnotation(BindPackages.class);
      if (packages != null) {
        for (String entityPackageName : packages.value()) {
          JPackage entityPackage = typeOracle.findPackage(entityPackageName);
          for (JType type : entityPackage.getTypes()) {
            JClassType classType = type.isClass();
            if (classType != null && classType.isAnnotationPresent(Entity.class)) {
              mapping.getMapping(classType);
            }
          }
        }
      }
      writeClasses(logger, context, mapping);


      return mapping.getPackageName() + "." + mapping.getPersistenceContextImplClass();

    } catch (Exception e) {
      logger.log(TreeLogger.Type.ERROR, "Exception thrown while generating PersistenceContext", e);
      throw new UnableToCompleteException();
    }
  }

  public void writeClasses(TreeLogger logger, GeneratorContext context, UnitMapping mapping) throws TemplateException, IOException {
    /*
        * Step 2 : Open up our Template configuration
    */

    Configuration cfg = Templates.get();


    /*
        * Step 3 : Generate delegates, managed instances, and lazy instances for
    * each managed entity
    */
    
    for (EntityMapping entity : mapping.getEntities()) {

      // write the managed entity class
      writeClass(logger, context, cfg, entity, "ManagedEntity.java.ftl", entity.getManagedClass());

      // write the lazy entity class
      writeClass(logger, context, cfg, entity, "LazyEntity.java.ftl", entity.getLazyClass());

      // the delegate that does the work of the EntityManager
      writeClass(logger, context, cfg, entity, "Delegate.java.ftl", entity.getDelegateClass());
    }

    /*
        * Step 4: Write the Em, Emf, and PersistenceContext implementations
    */

    writeClass(logger, context, cfg, mapping, "EntityManager.java.ftl",
        mapping.getEntityManagerClass());

    writeClass(logger, context, cfg, mapping, "EntityManagerFactory.java.ftl",
        mapping.getEntityManagerFactoryClass());


    writeClass(logger, context, cfg, mapping, "PersistenceContext.java.ftl",
        mapping.getPersistenceContextImplClass());
  }

  private void writeClass(TreeLogger logger, GeneratorContext context,
                          Configuration templateCfg, EntityMapping entity, String templateName, String className) throws TemplateException, IOException {
    PrintWriter writer = context.tryCreate(logger, entity.getContext().getPackageName(),
        className);
    if (writer != null) {
      templateCfg.getTemplate(templateName).process(entity, writer);
      writer.close();
      context.commit(logger, writer);
    }
  }


  private void writeClass(TreeLogger logger, GeneratorContext context,
                          Configuration templateCfg, UnitMapping mapping, String templateName,
                          String className) throws TemplateException, IOException {
    PrintWriter writer = context.tryCreate(logger, mapping.getPackageName(),
        className);
    if (writer != null) {
      templateCfg.getTemplate(templateName).process(mapping, writer);
      writer.close();
      context.commit(logger, writer);
    }
  }


}
