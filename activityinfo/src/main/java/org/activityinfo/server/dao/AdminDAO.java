package org.activityinfo.server.dao;

import org.activityinfo.server.domain.AdminEntity;
import org.activityinfo.server.domain.AdminLevel;

import java.util.List;

public interface AdminDAO {

	/**
	 *
	 * 
	 * @param levelId The id of the administrative level for which to return the entities
	 * @return  A list of administrative entities that constitute an administrative
	 * level. (e.g. return all provinces, return all districts, etc)
	 */
	public List<AdminEntity> getEntities(int levelId);

	/**
	 * Returns
	 * 
	 * @param levelId See {@link AdminLevel}
	 * @param parentId 
	 * @return A list of the children of a given admin entity for at a given level.
	 */
	public List<AdminEntity> getEntities(int levelId, Integer parentId);

    /**
     * 
     * @param levelId
     * @param parentId
     * @param activityId
     * @return  A list of administrative entities at a given level, of a given parent,
     * AND where activities of the given type have taken place
     */
    public List<AdminEntity> getEntities(int levelId, Integer parentId, int activityId);


}