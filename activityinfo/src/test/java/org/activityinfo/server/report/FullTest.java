package org.activityinfo.server.report;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.activityinfo.server.ActivityInfoModule;
import org.activityinfo.server.ActivityInfoTestingModule;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.generator.ReportGenerator;
import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.server.report.renderer.RendererFactory;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.report.model.Report;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class FullTest {

	@Test
	public void test() throws Throwable {

		/*
		 *
		 * Parse the XML report definition
		 */
        Report report = ReportParserJaxb.parseXML(new InputStreamReader(
                getClass().getResourceAsStream("/report-def/full-test.xml")));


        /*
         * Set up our envionnement
         */
		Injector injector = Guice.createInjector(
                new ActivityInfoModule(), 
                new ActivityInfoTestingModule());
        EntityManager em = injector.getInstance(EntityManager.class);

        User user = (User) em.createQuery("select u from User u where u.email = :email")
                .setParameter("email", "akbertram@gmail.com").getResultList().get(0);

        /*
         * Generate
         */
        ReportGenerator rgtor = injector.getInstance(ReportGenerator.class);
        rgtor.generate(user, report, null, null);

        File file = new File("target/report-test");
        file.mkdirs();

		/*
		 * Render
		 */
        RendererFactory factory = injector.getInstance(RendererFactory.class);
        for(RenderElement.Format format : RenderElement.Format.values()) {
            if(format != RenderElement.Format.Excel_Data) {
                Renderer renderer = factory.get(format);
                FileOutputStream fos = new FileOutputStream("target/report-tests/full-test" + renderer.getFileSuffix());

                renderer.render(report, fos);
                fos.close();
            }
        }
	}
}
