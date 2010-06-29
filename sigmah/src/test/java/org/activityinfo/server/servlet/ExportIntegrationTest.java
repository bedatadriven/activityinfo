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

import com.google.inject.Inject;
import org.activityinfo.server.dao.OnDataSet;
import org.activityinfo.server.dao.SiteTableDAO;
import org.activityinfo.server.dao.hibernate.SiteTableDAOHibernate;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.endpoint.export.Export;
import org.activityinfo.server.endpoint.gwtrpc.CommandTestCase;
import org.activityinfo.server.endpoint.gwtrpc.handler.GetSchemaHandler;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.test.InjectionSupport;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @author Alex Bertram
 */
@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class ExportIntegrationTest extends CommandTestCase {

    @Inject
    private EntityManagerFactory emf;

    @Inject
    private GetSchemaHandler getSchemaHandler;


    @Test
    public void fullTest() throws Throwable {


        User user = new User();
        user.setId(1);
        user.setName("Alex");

        EntityManager em = emf.createEntityManager();
        SchemaDTO schema = (SchemaDTO) getSchemaHandler.execute(new GetSchema(), user);
        SiteTableDAO siteDAO = new SiteTableDAOHibernate(em);

        Export export = new Export(user, siteDAO);
        for (UserDatabaseDTO db : schema.getDatabases()) {
            for (ActivityDTO activity : db.getActivities()) {
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
