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

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RPC;
import com.google.inject.Singleton;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.activityinfo.shared.report.ExportService;
import org.activityinfo.shared.report.model.ReportElement;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.command.result.RenderResult;
import org.activityinfo.shared.exception.UnexpectedCommandException;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.IllegalAccessCommandException;
import org.activityinfo.server.dao.AuthDAO;
import org.activityinfo.server.domain.Authentication;
import org.activityinfo.server.domain.DomainFilters;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.util.EntropicToken;
import org.activityinfo.server.report.renderer.RendererFactory;
import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.server.command.handler.GenerateElementHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.FileOutputStream;

/**
 *
 * Accepts a single RPC command transported on the query string, and responds with
 * an attachment.
 *
 * @author Alex Bertram
 */
@Singleton
public class CommandDownloadServlet extends RemoteServiceServlet implements ExportService {

    private final Injector injector;

    @Inject
    public CommandDownloadServlet(Injector injector) {
        this.injector = injector;
    }

  @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        try {
            // copied from AbstractRemoteServiceServlet
            synchronized (this) {
                if (perThreadRequest == null) {
                    perThreadRequest = new ThreadLocal<HttpServletRequest>();
                }
                if (perThreadResponse == null) {
                    perThreadResponse = new ThreadLocal<HttpServletResponse>();
                }
                perThreadRequest.set(req);
                perThreadResponse.set(resp);
            }


            RPCRequest rpcRequest = RPC.decodeRequest(req.getQueryString(), this.getClass(), this);

            Object[] params = rpcRequest.getParameters();
            export((String)params[0], (ReportElement)params[1], (RenderElement.Format) params[2], resp);

        } catch(Throwable caught) {
            caught.printStackTrace();
            resp.setStatus(500);
            return;

        } finally {
            // null the thread-locals to avoid holding request/response
            //
            perThreadRequest.set(null);
            perThreadResponse.set(null);
        }
    }

    public void export(String authToken, ReportElement element, RenderElement.Format format,HttpServletResponse resp) throws IOException, CommandException {

        // first authenticate the request
        AuthDAO authDAO = injector.getInstance(AuthDAO.class);
        Authentication auth = authDAO.getSession(authToken);

        if(auth==null)
            throw new IllegalAccessCommandException();

        EntityManager em = injector.getInstance(EntityManager.class);
        DomainFilters.applyUserFilter(auth.getUser(), em);

        RendererFactory rendererFactory = injector.getInstance(RendererFactory.class);
        GenerateElementHandler generator = injector.getInstance(GenerateElementHandler.class);

        generator.execute(new GenerateElement(element),auth.getUser());

        // create the renderer
        Renderer renderer = rendererFactory.get(format);
        resp.setContentType(renderer.getMimeType());
        resp.addHeader("Content-Disposition", "attachment; filename=ActivityInfo");

        // render to output
        renderer.render(element, resp.getOutputStream());

    }

    @Override
    public String export(String authToken, ReportElement element, RenderElement.Format format) {
        return null;
    }

}
