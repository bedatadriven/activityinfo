
<#-- This macro writes a series of statements that reads properties from
     a RecordSet (named "rs") and assign the values to an entity instance
     (must be named "entity")

     The column indices are expected to be in a set of variables
     suffixed with "Index"

     For example:

     Entity:

     @Entity
     public class Person {
        private String name;
        private String organisation

        public String getName() { }
        public String setName(String name) {}

        @Column(name="org")
        public String getOrganisation() { }
     }

     Expected in teh current scope:

     int nameIndex = 1;
     int orgIndex = 2;

     Generated:

     entity.setName(Readers.readString(rs, nameIndex));
     entity.setOrganisation(Readers.readString(rs, orgIndex));

  -->

<#macro readRecordSet>

    <#list properties as property>
        <#if property.id == false>

            <#if property.embedded == true>
                <#-- Create a new instance of the embedded class and fill it's properties -->
                <#assign embed = "embedded" + property.name>
                ${property.embeddedClass.qualifiedClassName} ${embed} =
                    new ${property.embeddedClass.qualifiedClassName}();
                <#list property.embeddedClass.properties as embeddedProperty>
                    ${embed}.${embeddedProperty.setterName}(${embeddedProperty.readerName}(rs, ${embeddedProperty.column.indexVar}));
                </#list>
                entity.${property.setterName}(${embed});


            <#elseif property.toOne>
                <#-- Create a new, lazy instance of this class -->
                <#-- TODO: Eager loading ! -->

                // is the primary key null?
                if(${property.relatedEntity.id.readerName}(rs, ${property.column.indexVar}) == null) {
                    entity.${property.setterName}(null);
                } else {
                    // call get reference from the related class' delegate
                    entity.${property.setterName}(em.${property.relatedEntity.delegateField}
                                .getReference(${property.relatedEntity.id.readerName}(rs, ${property.column.indexVar})));
                }

            <#else>
                <#-- Simple one column property -->
                entity.${property.setterName}(${property.readerName}(rs, ${property.column.indexVar}));

            </#if>
        </#if>
    </#list>

</#macro>