package org.sigmah.server.database.dao;

import java.util.List;

import org.sigmah.server.database.hibernate.dao.DAO;
import org.sigmah.server.database.hibernate.entity.SiteAttachment;


public interface SiteAttachmentDAO extends DAO <SiteAttachment, Integer> {

	List<SiteAttachment> findSiteAttachments(String siteid);
	
}
