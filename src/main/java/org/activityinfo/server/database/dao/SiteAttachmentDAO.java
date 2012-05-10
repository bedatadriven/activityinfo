package org.activityinfo.server.database.dao;

import java.util.List;

import org.activityinfo.server.database.hibernate.dao.DAO;
import org.activityinfo.server.database.hibernate.entity.SiteAttachment;


public interface SiteAttachmentDAO extends DAO <SiteAttachment, Integer> {

	List<SiteAttachment> findSiteAttachments(String siteid);
	
	
}
