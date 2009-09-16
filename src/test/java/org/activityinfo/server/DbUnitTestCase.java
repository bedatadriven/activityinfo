package org.activityinfo.server;


import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.mssql.InsertIdentityOperation;
import org.dbunit.ext.mssql.MsSqlConnection;
import org.hibernate.ejb.HibernateEntityManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class DbUnitTestCase {

	public static EntityManagerFactory emf;
    private static FlatXmlDataSet loadedDataSet;


    @BeforeClass
	public static void runBeforeClass()  {
		
		if(emf == null) {
			emf = Persistence.createEntityManagerFactory("activityInfo-test");
		}
	}
	
	
	public static void populate(String name) {

		if(emf == null) {
			emf = Persistence.createEntityManagerFactory("activityInfo-test");
		}
		
		InputStream testData = DbUnitTestCase.class.getResourceAsStream("/dbunit/" + name + ".db.xml");

		HibernateEntityManager hem = (HibernateEntityManager) emf.createEntityManager();
       
		try {


			IDatabaseConnection con = new MsSqlConnection(hem.getSession().connection());
	        con.getConfig().setProperty("http://www.dbunit.org/properties/escapePattern", "[?]");

            if(loadedDataSet != null) {
                InsertIdentityOperation.DELETE_ALL.execute(con, loadedDataSet);
            }

            loadedDataSet = new FlatXmlDataSet(
                    new InputStreamReader(testData), false, true, false);


//	        DatabaseOperation.DELETE_ALL.execute(con, dataSet)       
//	        DatabaseOperation.execute(con, dataSet);

			InsertIdentityOperation.INSERT.execute(con, loadedDataSet);
	        
	        con.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error(e);
		} 
	}
	
	@AfterClass
	public static void runAfter() {
		if(emf!=null) {
			emf.close();
			emf = null;
		}
	}
}
