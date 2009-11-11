<#-- @ftlvariable name="" type="com.google.gwt.gears.persistence.mapping.UnitMapping" -->

package ${packageName};

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManagerFactory;

import com.google.gwt.gears.jdbc.client.DriverManager;
import com.google.gwt.gears.persistence.client.ConnectionProvider;
import com.google.gwt.gears.persistence.client.GearsConnectionProvider;

public class ${persistenceContextImplClass} implements ${type.qualifiedName} {

   /**
     *
     * @param databaseName
     * @return
     */
    public EntityManagerFactory createEntityManagerFactory(ConnectionProvider provider) {
        return new ${entityManagerFactoryClass}(provider);
    }

    public Map<String, Object> getColumnMap(Object entity) {
        <#list entities as entity>
            if(entity instanceof ${entity.qualifiedClassName})
                return ${entity.delegateClass}.getColumnMap(entity);
        </#list>
        throw new PersistenceException("The class " + entity.getClass().getName() + " is not managed by " +
                "this persistence unit.");
    }

    public Map<String, Object> getColumnMap(Map<String,Object> propertyMap) {
        <#list entities as entity>
            if(entity instanceof ${entity.qualifiedClassName})
                return ${entity.delegateClass}.getColumnMap(entity);
        </#list>
        throw new PersistenceException("The class " + entity.getClass().getName() + " is not managed by " +
                "this persistence unit.");
    }


}
