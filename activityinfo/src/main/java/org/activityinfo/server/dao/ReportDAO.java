package org.activityinfo.server.dao;

import com.google.inject.ImplementedBy;
import org.activityinfo.server.dao.hibernate.ReportDAOJPA;

@ImplementedBy(ReportDAOJPA.class)
public interface ReportDAO {

    String getXmlById(int reportId);

    int createReport(Integer databaseId, String reportXml);

    void updateXml(int id, String newXml);

}