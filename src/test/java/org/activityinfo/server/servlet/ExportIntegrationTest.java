package org.activityinfo.server.servlet;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.File;
import java.io.FileOutputStream;

import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.endpoint.export.DbUserExport;
import org.activityinfo.server.endpoint.export.SiteExporter;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.DTOs;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.test.InjectionSupport;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class ExportIntegrationTest extends CommandTestCase2 {

    @Test
    public void fullTest() throws Throwable {

        User user = new User();
        user.setId(1);
        user.setName("Alex");

        SchemaDTO schema = execute(new GetSchema());

        SiteExporter export = new SiteExporter(getDispatcherSync());
        for (UserDatabaseDTO db : schema.getDatabases()) {
            for (ActivityDTO activity : db.getActivities()) {
                export.export(activity, new Filter());
            }
        }

        File outputDir = new File("target/report-test/");
        outputDir.mkdirs();

        FileOutputStream fos = new FileOutputStream(
            "target/report-test/ExportTest.xls");
        export.getBook().write(fos);
        fos.close();
    }

    @Test
    public void DbUserExportTest() throws Throwable {

        DbUserExport export = new DbUserExport(DTOs.rrmUsers().getData());
        export.createSheet();

        File outputDir = new File("target/report-test/");
        outputDir.mkdirs();

        FileOutputStream fos = new FileOutputStream(
            "target/report-test/DbUserExportTest.xls");
        export.getBook().write(fos);
        fos.close();
    }
}
