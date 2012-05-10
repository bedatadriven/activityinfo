/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.endpoint.export;

import java.io.IOException;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.database.hibernate.dao.AuthenticationDAO;
import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.server.database.hibernate.entity.DomainFilters;
import org.activityinfo.server.database.hibernate.entity.ReportDefinition;
import org.activityinfo.server.report.ReportParserJaxb;
import org.activityinfo.server.report.generator.ReportGenerator;
import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.server.report.renderer.RendererFactory;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.Report;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * Generates, Renders, and serves a full report based on ID
 *
 * @author Alex Bertram
 */
@Singleton
public class ReportServlet extends HttpServlet {

    private final Injector injector;


    @Inject
    public ReportServlet(Injector injector) {
        this.injector = injector;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // first, authenticate the response
        AuthenticationDAO authDAO = injector.getInstance(AuthenticationDAO.class);
        Authentication auth = authDAO.findById(req.getParameter("auth"));
        if (auth == null) {
            resp.setStatus(500);
            return;
        }

        // now apply the user filters
        EntityManager em = injector.getInstance(EntityManager.class);
        DomainFilters.applyUserFilter(auth.getUser(), em);

        // load the report definition by id
        ReportDefinition template = em.find(ReportDefinition.class, Integer.parseInt(req.getParameter("id")));
        Report report = null;
        try {
            report = ReportParserJaxb.parseXml(template.getXml());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }

        // parse parameters
        DateRange dateRange = new DateRange();
        if (req.getParameter("minDate") != null) {
            dateRange.setMinDate(new Date(Long.parseLong(req.getParameter("minDate"))));
        }
        if (req.getParameter("maxDate") != null) {
            dateRange.setMaxDate(new Date(Long.parseLong(req.getParameter("maxDate"))));
        }

        // generate the report content
        ReportGenerator gtor = injector.getInstance(ReportGenerator.class);
        gtor.generate(auth.getUser(), report, null, dateRange);

        // render in the requested format
        RenderElement.Format format = RenderElement.Format.valueOf(req.getParameter("format"));
        RendererFactory factory = injector.getInstance(RendererFactory.class);
        Renderer renderer = factory.get(format);

        // composefile name

        String filename = report.getContent().getFileName() + renderer.getFileSuffix();
        resp.setContentType(renderer.getMimeType());
        resp.addHeader("Content-Disposition", "attachment; filename=" + filename);

        try {
            renderer.render(report, resp.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }
}
