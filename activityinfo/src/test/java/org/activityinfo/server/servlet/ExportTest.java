/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.servlet;

import org.activityinfo.server.BeanMappingModule;
import org.activityinfo.server.dao.SchemaDAO;
import org.activityinfo.server.dao.SiteTableDAO;
import org.activityinfo.server.dao.hibernate.SchemaDAOJPA;
import org.activityinfo.server.dao.hibernate.SiteTableDAOHibernate;
import org.activityinfo.server.domain.DomainFilters;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.endpoint.gwtrpc.handler.GetSchemaHandler;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.dozer.Mapper;
import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @author Alex Bertram
 */
public class ExportTest {

    @Ignore("Needs to be rewritten to use test database")
    @Test
    public void fullTest() throws Throwable {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("activityInfo");
        EntityManager em = emf.createEntityManager();

        User user = (User) em.createQuery("select u from User u where u.email = :email")
                .setParameter("email", "akbertram@gmail.com")
                .getResultList()
                .get(0);

        DomainFilters.applyUserFilter(user, em);

        SchemaDAO schemaDAO = new SchemaDAOJPA(em);
        Mapper mapper = BeanMappingModule.getMapper();

        GetSchemaHandler schemaHandler = new GetSchemaHandler(schemaDAO, mapper);

        Schema schema = (Schema) schemaHandler.execute(new GetSchema(), user);

        SiteTableDAO siteDAO = new SiteTableDAOHibernate(em);

        Export export = new Export(user, siteDAO);
        for (UserDatabaseDTO db : schema.getDatabases()) {
            for (ActivityModel activity : db.getActivities()) {
                export.export(activity);
            }
        }

        File outputDir = new File("target/report-test/");
        outputDir.mkdirs();

        FileOutputStream fos = new FileOutputStream("target/report-test/ExportTest.xls");
        export.getBook().write(fos);
        fos.close();

    }


}
