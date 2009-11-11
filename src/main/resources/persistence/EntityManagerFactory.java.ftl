<#-- @ftlvariable name="" type="com.google.gwt.gears.persistence.mapping.UnitMapping" -->

<#include 'DDL.sql.ftl'>

package ${packageName};

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

import com.google.gwt.gears.persistence.client.ConnectionProvider;


class ${entityManagerFactoryClass} implements EntityManagerFactory {

    private ConnectionProvider provider;
    private boolean schemaCreated = false;
    private boolean closed = false;
    private List<${entityManagerClass}> managers = new ArrayList<${entityManagerClass}>();

    public ${entityManagerFactoryClass}(ConnectionProvider provider) {
        this.provider = provider;
    }

    public EntityManager createEntityManager(Map map) {
        return createEntityManager();
    }

    public EntityManager createEntityManager() {

        try {

            Connection conn = provider.getConnection();

            if(!schemaCreated)
                createSchema(conn);

            ${entityManagerClass} em = new ${entityManagerClass}(provider);
            managers.add(em);
            return em;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        for(${entityManagerClass} manager : managers) {
            manager.close();
        }
        managers = null;
        closed = true;
    }

    public boolean isOpen() {
        return !closed;
    }

    private void createSchema(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            <#list entities as entity>
            stmt.executeUpdate("<@createtable entity/>");
            </#list>
            stmt.close();
            schemaCreated=true;
        } catch(SQLException e) {
            throw new RuntimeException("EntityManagerFactory Failed to create schema: ", e);
        }
    }

}