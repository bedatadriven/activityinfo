/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.report.generator.ReportGenerator;
import org.sigmah.server.report.renderer.Renderer;
import org.sigmah.server.report.renderer.RendererFactory;
import org.sigmah.shared.command.RenderElement;
import org.sigmah.shared.report.content.PivotContent;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportElement;
import org.sigmah.shared.report.model.TableElement;
import org.sigmah.shared.report.model.labeling.ArabicNumberSequence;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;
import org.sigmah.shared.report.model.layers.ScalingType;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;
import org.sigmah.test.ServletStubModule;

import com.google.inject.Inject;

//@Ignore("Needs to be rewritten -- figure out what to do with dependency on the map icons folder")
@RunWith(InjectionSupport.class)
@Modules({ReportStubModule.class, ServletStubModule.class, MockHibernateModule.class}) //, , 
public class ReportsTest {

    @Inject
    private EntityManager em;

    @Inject
    private ReportGenerator reportGenerator;

    @Inject
    private RendererFactory factory;

    @Inject
	private User user;

	@Before
	public void setup() {
        user = getUser();
    	createDirectoriesIfNecessary();    	
    }

	@Test
    public void testFullReport() throws Throwable {
        Report report = getReport("full-test.xml");
        
        reportGenerator.generate(user, report, null, null);
        for (RenderElement.Format format : RenderElement.Format.values()) {
            if (format != RenderElement.Format.Excel && format != RenderElement.Format.Excel_Data) {
                Renderer renderer = factory.get(format);
                FileOutputStream fos = new FileOutputStream("target/report-tests/full-test" + renderer.getFileSuffix());
                renderer.render(report, fos);
                fos.close();
            }
        }
    }
	
	@Test
	public void parseAllReports() {

	}
	
	@Test
	public void testApplesReport() throws Throwable {
        Report report = getReport("realworld/ApplesReport.xml");

        reportGenerator.generate(user, report, null, null);

        ReportElement element = report.getElements().get(0);
        PivotContent pivotTable = (PivotContent) element.getContent();

        assertEquals("Expected different report title", report.getTitle(), "Phase one apple report");
        
        assertEquals("Expected different report description", 
        		report.getDescription(), "Apples come in different shapes, colors and taste");
        
        assertEquals("Expected only one filter", 
        		report.getFilter().getRestrictedDimensions().size(), 1);
        
        assertEquals("Expected one element", report.getElements().size(), 1); 
        
        assertTrue("Expected pivottable element", element.getContent() instanceof PivotContent);
        
        assertEquals("Expected title: 'Apples, bananas and oranges'", 
        		element.getTitle(), "Apples, bananas and oranges");
	}
	
	@Test
	public void testConsolideDesActivitesReport() throws Throwable {

		// Setup test
        Report report = getReport("realworld/ConsolideDesActivites.xml");

        reportGenerator.generate(user, report, null, null);

        TableElement pivotTable = (TableElement) report.getElements().get(2);
        MapReportElement map1 = (MapReportElement) pivotTable.getMap();
        BubbleMapLayer bubbleMap = (BubbleMapLayer) map1.getLayers().get(0);
        
        assertTrue("Arabic numbering expected", 
        		bubbleMap.getLabelSequence() instanceof ArabicNumberSequence);
        
        assertEquals("MinRadius of 8 expected", bubbleMap.getMinRadius(), 8);
        assertEquals("MaxRadius of 14 expected", bubbleMap.getMaxRadius(), 14);
        assertEquals("Graduated scaling expected", bubbleMap.getScaling(), ScalingType.Graduated);
	}
    
    private void createDirectoriesIfNecessary() {
        File file = new File("target/report-test");
        file.mkdirs();
	}

	private User getUser() {
		User u = new User();
		u.setId(3); //akbertram
		return u;
//        return (User) em.createQuery("select u from User u where u.email = :email")
//        		.setParameter("email", "akbertram@gmail.com").getResultList().get(0);
	}

	public Report getReport(String reportNameWithPath) throws JAXBException {
    	return ReportParserJaxb.parseXML(new InputStreamReader(
                getClass().getResourceAsStream("/report-def/" + reportNameWithPath)));
    }
	
	public Report getReport(int id) {
		return null;
	}
}
