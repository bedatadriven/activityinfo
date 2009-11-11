<#-- @ftlvariable name="" type="com.google.gwt.gears.persistence.mapping.UnitMapping" -->

package ${packageName};

import java.util.Map;
import java.util.HashMap;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.*;

import com.google.gwt.gears.persistence.client.impl.Delegate;
import com.google.gwt.gears.persistence.client.ConnectionProvider;

class ${entityManagerClass} implements EntityManager {

    // we want to avoid dynamic lookups as much as possible
    // so store references to the delegates here with package
    // access so we can reference them when we know the type
    // at compile time
    <#list entities as mapping>
    Delegate<${mapping.qualifiedClassName}, ${mapping.idBoxedClass}> ${mapping.delegateField};
    </#list>

    // There will however be times when we need to
    // do runtime lookup
    protected Map<Class, Delegate> delegates;

    private boolean open = true;
    private ConnectionProvider provider;
    private Connection connection;

    public ${entityManagerClass}(ConnectionProvider provider) throws SQLException {
        this.provider = provider;
        this.connection = provider.getConnection();

        this.delegates = new HashMap<Class, Delegate>();
        <#list entities as entity>
        // create our instance
        ${entity.delegateField} = new ${packageName}.${entity.delegateClass}(this, connection);

        // store in the lookup array for when we
        // need dynamic delegation
        delegates.put(${entity.delegateClass}.class, ${entity.delegateField});
        </#list>
    }

    public Delegate delegateByClass(Class entityClass) {
        // rather than looking up using our map, use a series
        // of if-statements that *should* enable the GWT compiler
        // to optimize out many cases of lookup

        <#list entities as entity>
        if(entityClass == ${entity.qualifiedClassName}.class)
            return ${entity.delegateField};
        </#list>

        throw new IllegalArgumentException(entityClass.getName() + " is not managed by this PersistenceContext");
    }


  @Override
  public boolean isOpen() {
    return open;
  }

  @Override
  public void close() {
    open = false;
    try {
      provider.closeConnection(connection);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private Delegate delegate(Object entity) {
    return delegateByClass(entity.getClass());
  }

  @Override
  public void persist(Object entity) {
    delegate(entity).persist(entity);
  }

  @Override
  public <T> T merge(T entity) {
    return (T) delegate(entity).merge(entity);
  }

  @Override
  public void remove(Object entity) {
    delegate(entity).remove(entity);
  }

  @Override
  public void refresh(Object entity) {
    delegate(entity).refresh(entity);
  }

  @Override
  public boolean contains(Object entity) {
    return delegate(entity).contains(entity);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey) {
    return (T) delegateByClass(entityClass).find(primaryKey);
  }

  @Override
  public <T> T getReference(Class<T> entityClass, Object primaryKey) {
    return (T) delegateByClass(entityClass).find(primaryKey);
  }

  @Override
  public void flush() {
    for (Delegate delegate : delegates.values()) {
      //mgr.flush();
    }
  }

  @Override
  public void setFlushMode(FlushModeType flushMode) {

  }

  @Override
  public FlushModeType getFlushMode() {
    return null;
  }

  @Override
  public void lock(Object entity, LockModeType lockMode) {

  }

  @Override
  public void clear() {
    for (Delegate delegate : delegates.values()) {
      delegate.clear();
    }
  }

  @Override
  public Query createQuery(String qlString) {
    return null;
  }

  @Override
  public Query createNamedQuery(String name) {
    return null;
  }

  @Override
  public Query createNativeQuery(String sqlString) {
    return new NativeUpdateQuery(this, connection, sqlString);
  }

  @Override
  public Query createNativeQuery(String sqlString, Class resultClass) {
    return delegateByClass(resultClass).createNativeBoundQuery(sqlString);
  }

  @Override
  public Query createNativeQuery(String sqlString, String resultSetMapping) {
    throw new PersistenceException("Sqlmappings not yet implemented!");
  }

  @Override
  public void joinTransaction() {

  }

  @Override
  public Object getDelegate() {
    return null;
  }

  @Override
  public EntityTransaction getTransaction() {
    return null;
  }
}


