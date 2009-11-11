
package javax.persistence;

public interface EntityTransaction {

    public void begin();

    public void commit();

    public void rollback();

    public void setRollbackOnly();

    public boolean getRollbackOnly();

    public boolean isActive();
}
