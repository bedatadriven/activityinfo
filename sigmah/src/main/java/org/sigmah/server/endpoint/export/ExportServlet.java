/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.export;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.sigmah.server.dao.AuthenticationDAO;
import org.sigmah.server.domain.Authentication;
import org.sigmah.server.domain.DomainFilters;
import org.sigmah.server.endpoint.gwtrpc.handler.HandlerUtil;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dao.SiteTableDAO;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.exception.CommandException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Exports complete data to an Excel file
 *
 * @author Alex Bertram
 */
@Singleton
public class ExportServlet extends HttpServlet {


    private Injector injector;

    @Inject
    public ExportServlet(Injector injector) {
        this.injector = injector;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AuthenticationDAO authDAO = injector.getInstance(AuthenticationDAO.class);
        Authentication auth = authDAO.findById(req.getParameter("auth"));
        if (auth == null) {
            // todo: offer basic authentication?
            resp.setStatus(500);
            return;
        }

        Set<Integer> activities = new HashSet<Integer>();
        for (String activity : req.getParameterValues("a")) {
            activities.add(Integer.parseInt(activity));
        }

        try {

            DomainFilters.applyUserFilter(auth.getUser(), injector.getInstance(EntityManager.class));

            SchemaDTO schema = HandlerUtil.execute(injector, new GetSchema(), auth.getUser());
            SiteTableDAO siteDAO = injector.getInstance(SiteTableDAO.class);

            Export export = new Export(auth.getUser(), siteDAO);
            for (UserDatabaseDTO db : schema.getDatabases()) {
                for (ActivityDTO activity : db.getActivities()) {
                    if (activities.size() == 0 || activities.contains(activity.getId())) {
                        export.export(activity);
                    }
                }
            }

            resp.setContentType("application/vnd.ms-excel");
            if (req.getHeader("User-Agent").indexOf("MSIE") != -1) {
                resp.addHeader("Content-Disposition", "attachment; filename=ActivityInfo.xls");
            } else {
                resp.addHeader("Content-Disposition", "attachment; filename=" +
                        ("ActivityInfo Export " + new Date().toString() + ".xls").replace(" ", "_"));
            }


            OutputStream os = resp.getOutputStream();
            export.getBook().write(os);


        } catch (CommandException e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }
}