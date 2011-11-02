/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.servlet;

import java.io.File;
import java.io.FileOutputStream;

import javax.persistence.EntityManagerFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.command.CommandTestCase2;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.server.endpoint.export.SiteExporter;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dao.SqlDialect;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.test.InjectionSupport;

import com.google.inject.Inject;


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
}
