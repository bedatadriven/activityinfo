<#-- @ftlvariable name="" type="com.google.gwt.gears.persistence.mapping.EntityMapping" -->

package ${context.packageName};

/**
  * A subclass of an entity that supports lazy loading,
  * persistent sets, etc.
  */
class ${lazyClass} extends ${managedClass} {

    private ${context.entityManagerClass} em;
    private boolean loaded;

    public ${lazyClass}(${context.entityManagerClass} em, ${idBoxedClass} id) {
        super(em, id);
        loaded = false;
    }

    public void __doload() {
        loaded = true;
        em.${delegateField}.refresh(this);
    }

    // Override all public methods to check to see if this
    // entity has been loaded before continuing

    <#list type.methods as method >
    <#if method.public &&
        !method.final &&
         method.name != id.setterName &&
         method.name != id.getterName>

    public ${method.returnType.qualifiedName} ${method.name}(<@singleline>
        <#assign i = 0>
        <#list method.parameterTypeNames as param>
            <#if i!= 0>, </#if>
            ${param} p${i}
            <#assign i = i + 1>
        </#list></@singleline>) {

        if(!loaded) __doload();

        <@singleline>
        <#if method.returnType.simpleName != "void">
        return
        </#if>
        super.${method.name}(
                <#assign i = 0>
                <#list method.parameterTypeNames as param> 
                    <#if i != 0>, </#if>
                    p${i}
                    <#assign i = i + 1>
                </#list>
               );
        </@singleline>

    }

    </#if>
    </#list>
}