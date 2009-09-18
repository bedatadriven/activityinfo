package org.activityinfo.server.dao;

public interface ReportDAO {

	String getXmlById(int reportId);

	int createReport(Integer databaseId, String reportXml);

	void updateXml(int id, String newXml);

}