package org.activityinfo.server.dao;

import com.google.inject.ImplementedBy;
import org.activityinfo.server.dao.hibernate.SchemaDAOJPA;
import org.activityinfo.server.domain.SchemaElement;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.UserDatabase;
import org.activityinfo.server.domain.UserPermission;

import java.util.Collection;
import java.util.List;

@ImplementedBy(SchemaDAOJPA.class)
public interface SchemaDAO {

    /**
     * Returns a list of databases
     *
     * @param user
     * @return
     */
    public List<UserDatabase> getDatabases(User user);

    /**
     * Returns an entity with the given id.
     *
     * @param <T>
     * @param clazz The entity class
     * @param id    The id
     * @return
     */
    public <T extends SchemaElement> T findById(Class<T> clazz, int id);


    /**
     * Returns the set of entities with the given ids.
     *
     * @param <T>
     * @param clazz
     * @param ids
     * @return
     */
    public <T> List<T> get(Class<T> clazz, Collection<Integer> ids);

    public int getLastActivitySortOrder(int databaseId);

    public void save(SchemaElement entity);

    public List<UserPermission> getUserPermissions(UserDatabase database, int offset, int limit);

    public int getUserCount(UserDatabase database);


}