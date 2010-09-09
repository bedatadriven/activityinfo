/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report;

import com.google.inject.Inject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.hibernate.HibernateModule;
import org.sigmah.shared.domain.User;
import org.sigmah.server.report.generator.ReportGenerator;
import org.sigmah.server.report.renderer.Renderer;
import org.sigmah.server.report.renderer.RendererFactory;
import org.sigmah.shared.command.RenderElement;
import org.sigmah.shared.report.model.Report;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.Modules;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

@Ignore("Needs to be rewritten -- figure out what to do with dependency on the map icons folder")
@RunWith(InjectionSupport.class)
@Modules({ReportModule.class, HibernateModule.class})
public class FullTest {


    @Inject
    private EntityManager em;

    @Inject
    private ReportGenerator rgtor;

    @Inject
    private RendererFactory factory;



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
        User user = (User) em.createQuery("select u from User u where u.email = :email")
                .setParameter("email", "akbertram@gmail.com").getResultList().get(0);

        /*
         * Generate
         */
        rgtor.generate(user, report, null, null);

        File file = new File("target/report-test");
        file.mkdirs();

        /*
           * Render
           */
        for (RenderElement.Format format : RenderElement.Format.values()) {
            if (format != RenderElement.Format.Excel_Data) {
                Renderer renderer = factory.get(format);
                FileOutputStream fos = new FileOutputStream("target/report-tests/full-test" + renderer.getFileSuffix());

                renderer.render(report, fos);
                fos.close();
            }
        }
    }
}
