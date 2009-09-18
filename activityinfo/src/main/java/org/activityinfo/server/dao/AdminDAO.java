package org.activityinfo.server.dao;

import java.util.List;

import org.activityinfo.server.domain.AdminEntity;
import org.activityinfo.server.domain.AdminLevel;

public interface AdminDAO {

	/**
	 * 
	 * Returns a list of administrative entities that constitute an administrative
	 * level. (e.g. return all provinces, return all districts, etc)
	 * 
	 * @param levelId The id of the administrative level for which to return the entities
	 * @return
	 */
	public List<AdminEntity> getEntities(int levelId);

	/**
	 * Returns a list of administrative entities at a given level that 
	 * 
	 * @param levelId See {@link AdminLevel}
	 * @param parentId 
	 * @return
	 */
	public List<AdminEntity> getEntities(int levelId, Integer parentId);

    /**
     * Returns a list of administrative entities at a given level, of a given parent,
     * AND where activities of the given type have taken place
     * 
     * @param levelId
     * @param parentId
     * @param activityId
     * @return
     */
    public List<AdminEntity> getEntities(int levelId, Integer parentId, int activityId);


}