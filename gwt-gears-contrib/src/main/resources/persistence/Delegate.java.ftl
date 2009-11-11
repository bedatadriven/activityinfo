<#-- @ftlvariable name="" type="com.google.gwt.gears.persistence.mapping.EntityMapping" -->

<#include 'SetParameters.java.ftl'>
<#include 'ReadRecordSet.java.ftl'>

<@autoindent>

package ${context.packageName};

import com.google.gwt.gears.persistence.client.impl.Delegate;
import com.google.gwt.gears.persistence.client.impl.Readers;
import com.google.gwt.gears.persistence.client.impl.AbstractNativeEntityBoundQuery;

import com.google.gwt.core.client.GWT;

import java.util.Map;
import java.util.HashMap;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

/**
 * Delegates do all the work for an EntityManager, but for a single class.
 */
class ${delegateClass}
    implements Delegate<${qualifiedClassName}, ${idBoxedClass}> {

    protected final ${context.entityManagerClass} em;
    protected final Connection conn;

    /**
     * Maps entity id to the persistent object.
     *  Keeps a list of live, attached persistent entities
     */
    private Map<${idBoxedClass}, ${qualifiedClassName}> cache;


    public ${delegateClass}(${context.entityManagerClass} em, Connection conn) {
        this.em = em;
        this.conn = conn;
        this.cache = new HashMap<${idBoxedClass},${qualifiedClassName}>();
    }

   /**
	 * Make an entity instance managed and persistent.
	 *
	 * @param entity
	 * @throws javax.persistence.EntityExistsException        if the entity already exists.
	 *                                      (The EntityExistsException may be thrown when the persist
	 *                                      operation is invoked, or the EntityExistsException or
	 *                                      another PersistenceException may be thrown at commit
	 *                                      time.)
	 * @throws IllegalStateException if this EntityManager has been closed.
	 * @throws IllegalArgumentException	 if not an entity
	 * @throws javax.persistence.TransactionRequiredException if invoked on a
	 *                                      container-managed entity manager of type
	 *                                      PersistenceContextType.TRANSACTION and there is
	 *                                      no transaction.
	 */
    public void persist(${qualifiedClassName} entity) {

        // Validate arguments / state
        if(entity==null) throw new IllegalArgumentException("Arguments to persist cannot be null");
        if(!em.isOpen()) throw new IllegalStateException("Entity manager is closed");

        // Insert into the database
        try {
            PreparedStatement stmt = conn.prepareStatement("<@singleline>
                insert into ${tableName} (
                       <@csv><#list properties as property>
                            <#if property.insertable>
                                <#list property.columns as column>
                                    ${column.name},        
                                </#list>
                            </#if>
                       </#list></@csv>
                        ) values (
                       <@csv>
                       <#list properties as property>
                            <#if property.insertable>
                                <#list property.columns as column>
                                    ?,        
                                </#list>
                            </#if>
                       </#list>
                       </@csv>
                       )
                </@singleline>");

            // TODO: set parameters
            <#assign index = 1>
            <#list properties as property>
                <#if property.insertable>
                    <@setParametersFromEntity entity="entity" property=property/>
                </#if>
            </#list>

            stmt.executeUpdate();

            <#if id.autoincrement == true>
            ResultSet keys = stmt.getGeneratedKeys();
            keys.next();
            entity.${id.setterName}(keys.getInt(1));
            </#if>

            cache.put(entity.${id.getterName}(), entity);

            if(GWT.isScript()) attach(entity, em, this);


        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

	/**
	 * Find by primary key.
	 *
	 * @param id
	 * @return the found entity instance or null
	 *         if the entity does not exist
	 * @throws IllegalStateException if this EntityManager has been closed
	 * @throws IllegalArgumentException if the first argument does
	 *                                  not denote an entity type or the second
	 *                                  argument is not a valid type for that
	 *                                  entity’s primary key
	 */
    public ${qualifiedClassName} find(${idBoxedClass} id) {

        // validate arguments / state
        if(!em.isOpen()) throw new IllegalStateException("Entity Manager is closed");
        if(id == null) throw new IllegalArgumentException("Primary key cannot be null");

        // first see if this entity is already loaded in the persistence
        // context
        ${qualifiedClassName} cachedEntity = cache.get(id);
        if(cachedEntity!=null)
            return cachedEntity;

        // Nope, need to load from database
        ResultSet rs = null;
        try {
             PreparedStatement stmt = prepareSelectByIdQuery(id);
             rs = stmt.executeQuery();
             if(!rs.next())
                return null;

             // create a new managed entity
             ${managedClass} entity = new ${managedClass}(em, id);

             // set the id we were passed in
             entity.${id.setterName}(id);

             readPropertiesFromResultSet(entity, rs);

             // add to our cache
             cache.put(id, entity);

             // if we're JavaScript, then we embed ourselve in the object
             // so calls to contains() are quick
             if(GWT.isScript()) attach(entity, em, this);

             // return our persistent entity
             return entity;

        } catch(SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(rs != null) { try { rs.close(); } catch(SQLException ignored) {} }
        }
    }

    private PreparedStatement prepareSelectByIdQuery(${idBoxedClass} id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("<@singleline>
                select
                    <@csv>
                    <#list properties as property>
                        <#if property.id == false>
                            <#list property.columns as column>
                                ${column.name},
                            </#list>
                        </#if>
                    </#list>
                    </@csv>
                from ${tableName}
                where
                    <@csv>
                    <#list id.columns as column>
                        ${column.name} = ?,
                    </#list>
                    </@csv>
             </@singleline>");

        // Set Query parameters
        <#assign index = 1>
        <@setParametersFromProperty "id" id/>

        return stmt;
    }

    private void readPropertiesFromResultSet(${qualifiedClassName} entity, ResultSet rs) throws SQLException {

        // define our index variables

        // read the properties from the recordset
        <#assign index=1>
        <#list properties as property>
            <#if property.id == false>
                <#list property.columns as column>
                    int ${column.indexVar} = ${index};
                    <#assign index = index + 1>
                </#list>
            </#if>
        </#list>

        <@readRecordSet/>
    }


    /**
     * Here is the exact semantic of merge():
     *
     * <ul>
     * <li>If there is a managed instance with the same identifier currently associated with the persistence context,
     *      copy the state of the given object onto the managed instance</li>
     * <li>If there is no managed instance currently associated with the persistence context, try to load it from the
     *  database, or create a new managed instance</li>
     * <li>The managed instance is returned</li>
     * <li>the given instance does not become associated with the persistence context, it remains detached and is
     * usually discarded</li>
     * </ul>
     *
     * @param detachedEntity
     * @return a managed entity instance (with the same identifier)
     */
    public ${qualifiedClassName} merge(${qualifiedClassName} detachedEntity) {

        ${qualifiedClassName} managedEntity = cache.get(detachedEntity.${id.getterName}());
        if(managedEntity == null) {
            // try to load it from the database
            managedEntity = find(detachedEntity.${id.getterName}());
        }
        if(managedEntity != null) {
            // copy the state of the given object onto the managed instance
            <#list properties as property>
                <#if property.id == false>
                    managedEntity.${property.setterName}(detachedEntity.${property.getterName}());
                </#if>
            </#list>
            return managedEntity;
        }

        // ok, the entity is not yet part of the database so just persist what we've been given.
        // this is slightly different than the semantics according to Gavin, but it's easier
        persist(detachedEntity);
        return detachedEntity;
    }


	public ${qualifiedClassName} getReference(${idBoxedClass} id) {

        // validate arguments / state
        if(!em.isOpen()) throw new IllegalStateException("EntityManager is closed");
        if(id == null) throw new IllegalArgumentException("Primary key cannot be null");

        // first try the first-level cache
        ${qualifiedClassName} entity = cache.get(id);
        if(entity != null)
            return entity;

        // nope, create a lazy entity
        entity = new ${lazyClass}(em, id);

        // add to our cache
        cache.put(id, entity);
        
        // if we're JavaScript, then we embed ourselve in the object
        // so calls to contains() are quick
        if(GWT.isScript()) attach(entity, em, this);

        return entity;
    }


   /**
	 * Check if the instance belongs to the current persistence
	 * context.
	 *
	 * @param entity
	 * @return <code>true</code> if the instance belongs to the current persistence context.
	 * @throws IllegalStateException if this EntityManager has been closed
	 * @throws IllegalArgumentException if not an entity
	 */
    public boolean contains(${qualifiedClassName} entity) {
        if(entity==null) throw new IllegalArgumentException("Arguments to contains() cannot be null");
        if(!em.isOpen()) throw new IllegalStateException("EntityManager is closed");
        return uncheckedContains(entity);
     }

    public boolean uncheckedContains(${qualifiedClassName} entity) {
        if(GWT.isScript()) {
            // this our preferred way using javascript
            return isAttached(entity);
        } else {
            return cache.containsKey(entity.${id.getterName}());
        }
    }

	/**
	 * Refresh the state of the instance from the database,
	 * overwriting changes made to the entity, if any.
	 *
	 * @param entity
	 * @throws IllegalStateException if this EntityManager has been closed
	 * @throws IllegalArgumentException	 if not an entity
	 *                                      or entity is not managed
	 * @throws javax.persistence.TransactionRequiredException if invoked on a
	 *                                      container-managed entity manager of type
	 *                                      PersistenceContextType.TRANSACTION and there is
	 *                                      no transaction.
	 * @throws javax.persistence.EntityNotFoundException      if the entity no longer
	 *                                      exists in the database
	 */
    public void refresh(${qualifiedClassName} entity) {
        if(!em.isOpen()) throw new IllegalStateException("EntityManager is closed");
        if(!uncheckedContains(entity)) throw new IllegalArgumentException("Entity is not managed");

        ResultSet rs = null;
        try {
             PreparedStatement stmt = prepareSelectByIdQuery(entity.${id.getterName}());

             rs = stmt.executeQuery();
             if(!rs.next())
                throw new EntityNotFoundException();

             // read the properties from the recordset
             readPropertiesFromResultSet(entity, rs);

        } catch(SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(rs != null) { try { rs.close(); } catch(SQLException ignored) {} }
        }
    }


    public Query createNativeBoundQuery(String sql) {

        return new AbstractNativeRuntimeQuery<${qualifiedClassName},${idBoxedClass}>(conn, sql) {

            // store the column mappings in a
            // fields
            <#list columns as column>
                private int ${column.name}Index;
            </#list>

            @Override
            protected void initColumnMapping(ResultSet rs) {
                try {
                    <#list columns as column>
                        ${column.name}Index = rs.findColumn("${column.name}");
                    </#list>
                } catch(SQLException e) {
                    throw new IllegalArgumentException("Your query must contain ALL the columns for an entity in the" +
                        " select list.");
                }
            }

            @Override
            protected ${qualifiedClassName} newResultInstance(ResultSet rs) throws SQLException {

                // first read the id
                ${idBoxedClass} id = ${id.readerName}(rs, ${id.column.name}Index);

                // try and get it from the cache first, to maintain object
                // identiy equality
                ${qualifiedClassName} entity = cache.get(id);
                if(entity == null) {
                    // nope, nice try, need to create/register a new instance
                    entity = new ${managedClass}(em, id);

                    cache.put(id, entity);

                     // if we're JavaScript, then we embed ourselve in the object
                     // so calls to contains() are quick
                     if(GWT.isScript()) attach(entity, em, ${delegateClass}.this);
                }

                // now set the properties from the recordset

                <@readRecordSet/>

                return entity;
            };

        };


    }

	/**
	 * Remove the entity instance.
     *
     * Spec 3.2.2: A managed entity instance becomes removed by invoking the remove method on it or by cascading the
     *  remove operation.
     *
     *  The semantics of the remove operation, applied to an entity X are as follows:
     *  -  If X is a new entity, it is ignored by the remove operation. However, the remove operation is
     *     cascaded to entities referenced by X, if the relationship from X to these other entities is annotated
     *     with the cascade=REMOVE or cascade=ALL annotation element value.
     *  -  If X is a managed entity, the remove operation causes it to become removed. The remove operation
     *     is cascaded to entities referenced by X, if the relationships from X to these other entities
     *     is annotated with the cascade=REMOVE or cascade=ALL annotation element value.
     *  -  If X is a detached entity, an IllegalArgumentException will be thrown by the remove
     *     operation (or the transaction commit will fail).
     *  -  If X is a removed entity, it is ignored by the remove operation.
     *
     *  <p>A removed entity X will be removed from the database at or before transaction commit or as a
     *  result of the flush operation.
     *  <p>After an entity has been removed, its state (except for generated state) will be that of the entity at the
     *   point at which the remove operation was called.
     *
	 *
	 * @param entity
	 * @throws IllegalStateException if this EntityManager has been closed
	 * @throws IllegalArgumentException	 if not an entity
	 *                                      or if a detached entity
	 * @throws javax.persistence.TransactionRequiredException if invoked on a
	 *                                      container-managed entity manager of type
	 *                                      PersistenceContextType.TRANSACTION and there is
	 *                                      no transaction.
	 */
	public void remove(${qualifiedClassName} entity) {


        if(!em.isOpen()) throw new IllegalStateException("EntityManager is closed");

        // seems to be some contradiction here in the javadoc/spec
        if(!uncheckedContains(entity))
            return;
            //throw new IllegalArgumentException("The entity is not attached (persistent), and" +
            //    "so cannot be removed.");

        // remove the entity from the database
        try {
            PreparedStatement stmt = conn.prepareStatement("<@singleline>

                delete from ${tableName} where
                <@csv>
                <#list id.columns as column>
                    ${column} = ?,
                </#list>
                </@csv>

            </@singleline>");

            <#assign index = 1>
            <@setParametersFromEntity "entity" id/>

            stmt.executeUpdate();

        } catch(SQLException e) {
            throw new RuntimeException(e);
        }

        // remove the entity from this session
        cache.remove(entity.${id.getterName}());

        // in javascript mode, we can do whatever we want the object and use that
        // to store a reference to ourselves
        if(GWT.isScript()) detach(entity);
    }

	/**
	 * Clear the persistence context, causing all managed
	 * entities to become detached. Changes made to entities that
	 * have not been flushed to the database will not be
	 * persisted.
	 *
	 * @throws IllegalStateException if this EntityManager has been closed
	 */
	public void clear() {
        if(!em.isOpen()) throw new IllegalStateException("EntityManager is closed");

        if(GWT.isScript()) {
            for(${qualifiedClassName} entity : cache.values()) {
                detach(entity);
            }
        }
        cache.clear();
    }

    private static native void attach(Object entity, EntityManager em, ${delegateClass} delegate) /*-{
        entity["__em"] = em;
        entity["__del"] = delegate;
    }-*/;

    private native void detach(${qualifiedClassName} entity) /*-{
        entity.__em = null;
        entity.__del = null;
    }-*/;

	private native boolean isAttached(${qualifiedClassName} entity) /*-{
        return entity.hasOwnProperty("__del") && entity.__del == this;
    }-*/;


    static Map<String,Object> getColumnMap(Object entity) {
        Map<String,Object> map = new HashMap<String,Object>();
        <#list properties as property>
            <#if property.id == false>
                <@putColumnValuesFromEntity "entity", property/>
            </#if>
        </#list>

        return map;
    }

    static Map<String,Object> getColumnMap(Map<String,Object> propertyMap) {
        Map<String,Object> map = new HashMap<String,Object>();

        for(Map.Entry<String,Object> p : propertyMap.entrySet()) {
            <#list properties as property>
                <#if property.id == false>
                    if("${property.name}".equals(p.getKey())) {
                        <@putColumnValuesFromProperty "p.getValue()", property/>
                    }
                </#if>
            </#list>
        }
        return map;
    }

}
</@autoindent>


