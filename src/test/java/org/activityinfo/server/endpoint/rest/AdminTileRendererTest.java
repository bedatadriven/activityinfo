package org.activityinfo.server.endpoint.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

import com.google.common.io.Files;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.hibernate.entity.AdminLevel;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.MockHibernateModule;
import org.activityinfo.test.Modules;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@Modules({ MockHibernateModule.class })
@OnDataSet("/dbunit/jordan.db.xml")
public class AdminTileRendererTest {

    @Inject
    private Session session;

    @Test
    public void testRenderTiles() throws IOException {
        AdminTileRenderer renderer = new AdminTileRenderer(session, (AdminLevel) session.get(AdminLevel.class, 1360));
        // byte[] image = renderer.render(6, 38, 26);
        Files.write(renderer.render(4, 9, 6), new File("target/jordan4.png"));
        Files.write(renderer.render(7, 76, 52), new File("target/jordan7.png"));

    }


    public static void main(String[] args) throws Exception {

        Class.forName("com.mysql.jdbc.Driver");

        Connection jdbcConnection = DriverManager.getConnection("jdbc:mysql://localhost/activityinfo",
            "root", "adminpwd");
        IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

        QueryDataSet partialDataSet = new QueryDataSet(connection);

        // Mention all the tables here for which you want data to be extracted
        // take note of the order to prevent FK constraint violation when
        // re-inserting
        partialDataSet.addTable("country", "select * from country where iso2='JO'");
        partialDataSet.addTable("adminlevel", "select * from adminlevel where countryid=360");
        partialDataSet.addTable("adminentity", "select * from adminentity where adminlevelid=1360");

        // XML file into which data needs to be extracted
        FlatXmlDataSet.write(partialDataSet, new FileOutputStream("src/test/resources/dbunit/jordan.db.xml"));
        System.out.println("Dataset written");
    }

}
