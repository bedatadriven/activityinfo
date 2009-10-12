/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.servlet;

import org.activityinfo.server.domain.Authentication;
import org.activityinfo.server.domain.DomainFilters;
import org.activityinfo.server.domain.ReportTemplate;
import org.activityinfo.server.domain.util.EntropicToken;
import org.activityinfo.server.dao.AuthDAO;
import org.activityinfo.server.report.generator.ReportGenerator;
import org.activityinfo.server.report.ReportParser;
import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.server.report.renderer.RendererFactory;
import org.activityinfo.server.report.renderer.itext.RtfReportRenderer;
import org.activityinfo.server.report.renderer.itext.PdfReportRenderer;
import org.activityinfo.server.report.renderer.ppt.PPTRenderer;
import org.activityinfo.server.report.renderer.excel.ExcelReportRenderer;
import org.activityinfo.server.report.renderer.excel.ExcelMapDataExporter;
import org.activityinfo.server.report.renderer.image.ImageReportRenderer;
import org.activityinfo.server.command.handler.RenderElementHandler;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.report.model.Parameter;
import org.activityinfo.shared.report.model.MapElement;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.exception.UnexpectedCommandException;
import org.activityinfo.shared.date.DateRange;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

import com.google.inject.Singleton;
import com.google.inject.Injector;
import com.google.inject.Inject;

/**
 *
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
        AuthDAO authDAO = injector.getInstance(AuthDAO.class);
        Authentication auth = authDAO.getSession(req.getParameter("auth"));

        if(auth == null) {
            resp.setStatus(500);
            return;
        }

        // now apply the user filters
        EntityManager em = injector.getInstance(EntityManager.class);
        DomainFilters.applyUserFilter(auth.getUser(), em);

        // load the report definition by id
        ReportTemplate template = em.find(ReportTemplate.class, Integer.parseInt(req.getParameter("id")));
        Report report = null;
        try {
            report = ReportParser.parseXml(template.getXml());
        } catch (SAXException e) {
            e.printStackTrace();
            resp.setStatus(500);
        }

        // parse parameters
        DateRange dateRange = new DateRange();
        if(req.getParameter("minDate") != null) {
            dateRange.setMinDate( new Date(Long.parseLong(req.getParameter("minDate"))));
        }
        if(req.getParameter("maxDate") != null) {
            dateRange.setMinDate( new Date(Long.parseLong(req.getParameter("maxDate"))));
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
