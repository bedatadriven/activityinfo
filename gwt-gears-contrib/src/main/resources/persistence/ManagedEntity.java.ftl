<#-- @ftlvariable name="" type="com.google.gwt.gears.persistence.mapping.EntityMapping" -->

package ${context.packageName};

/**
  * A subclass of an entity that supports automatic updates
  * persistent classes, etc
  */
class ${managedClass} extends ${qualifiedClassName} {

    private ${context.entityManagerClass} em;

    public ${managedClass}(${context.entityManagerClass} em, ${idBoxedClass} id) {
        ${id.setterName}(id);
    }



}