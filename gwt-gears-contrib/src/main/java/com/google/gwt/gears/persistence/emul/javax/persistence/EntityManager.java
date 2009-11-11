package javax.persistence;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.EntityTransaction;

public interface EntityManager {

	public void persist(Object entity);

	public <T> T merge(T entity);

    public void remove(Object entity);

    public <T> T find(Class<T> entityClass, Object primaryKey);

    public <T> T getReference(Class<T> entityClass, Object primaryKey);
	
    public void flush();

    public void setFlushMode(FlushModeType flushMode);

    public FlushModeType getFlushMode();

    public void lock(Object entity, LockModeType lockMode);

    public void refresh(Object entity);

    public void clear();


    public boolean contains(Object entity);

	public Query createQuery(String ejbqlString);

	public Query createNamedQuery(String name);

	public Query createNativeQuery(String sqlString);

	public Query createNativeQuery(String sqlString, Class resultClass);

    public Query createNativeQuery(String sqlString, String resultSetMapping);

    public void joinTransaction();

    public Object getDelegate();

    public void close();


    public boolean isOpen();

    public EntityTransaction getTransaction();
}