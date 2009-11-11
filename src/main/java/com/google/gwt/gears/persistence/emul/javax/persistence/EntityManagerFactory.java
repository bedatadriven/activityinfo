package javax.persistence;

import java.util.Map;

public interface EntityManagerFactory {

    EntityManager createEntityManager();

    EntityManager createEntityManager(Map map);

    void close();

    public boolean isOpen();
}
