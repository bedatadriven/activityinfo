/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.servlet;

import com.google.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.server.dao.SiteTableDAO;
import org.sigmah.server.dao.hibernate.SiteTableDAOHibernate;
import org.sigmah.server.domain.User;
import org.sigmah.server.endpoint.export.Export;
import org.sigmah.server.endpoint.gwtrpc.CommandTestCase;
import org.sigmah.server.endpoint.gwtrpc.handler.GetSchemaHandler;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.test.InjectionSupport;

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
