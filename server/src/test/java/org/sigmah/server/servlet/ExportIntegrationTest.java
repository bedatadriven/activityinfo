/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.servlet;

import java.io.File;
import java.io.FileOutputStream;

import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.DTOs;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.command.CommandTestCase2;
import org.sigmah.server.database.OnDataSet;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.endpoint.export.DbUserExport;
import org.sigmah.server.endpoint.export.SiteExporter;
import org.sigmah.test.InjectionSupport;


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
                export.export(activity);
            }
        }

        File outputDir = new File("target/report-test/");
        outputDir.mkdirs();

        FileOutputStream fos = new FileOutputStream("target/report-test/ExportTest.xls");
        export.getBook().write(fos);
        fos.close();
    }
    
    @Test
    public void DbUserExportTest()throws Throwable{
    	
    	DbUserExport export = new DbUserExport(DTOs.RRM_Users().getData());
        export.createSheet();

        File outputDir = new File("target/report-test/");
        outputDir.mkdirs();

        FileOutputStream fos = new FileOutputStream("target/report-test/DbUserExportTest.xls");
        export.getBook().write(fos);
        fos.close();
    }
}
