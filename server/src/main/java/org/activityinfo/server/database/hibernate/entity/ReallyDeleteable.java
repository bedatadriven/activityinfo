package org.activityinfo.server.database.hibernate.entity;

/*
 * Marks an entity to remove it physically from the database, versus marking it as
 * deleted using a dateDeleted flag
 */
public interface ReallyDeleteable {
	public void deleteReferences();
}
