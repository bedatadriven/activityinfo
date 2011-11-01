/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.server.endpoint.export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sigmah.server.dao.AuthenticationDAO;
import org.sigmah.server.domain.Authentication;
import org.sigmah.server.domain.DomainFilters;
import org.sigmah.server.endpoint.gwtrpc.handler.HandlerUtil;
import org.sigmah.shared.command.GetUsers;
import org.sigmah.shared.command.result.UserResult;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * Exports complete Users List to an Excel file
 * 
 * @author Muhammad Abid
 */
@Singleton
public class ExportUsersServlet extends HttpServlet {

	private Injector injector;

	@Inject
	public ExportUsersServlet(Injector injector) {
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

		int dbId = Integer.valueOf(req.getParameter("dbUsers"));

		try {

			DomainFilters.applyUserFilter(auth.getUser(), injector.getInstance(EntityManager.class));

			UserResult userResult = HandlerUtil.execute(injector, new GetUsers(dbId), auth.getUser());

			DbUserExport export = new DbUserExport(userResult.getData());
			export.createSheet();
			
			resp.setContentType("application/vnd.ms-excel");
			if (req.getHeader("User-Agent").indexOf("MSIE") != -1) {
				resp.addHeader("Content-Disposition", "attachment; filename=ActivityInfo.xls");
			} else {
				resp.addHeader(
						"Content-Disposition",
						"attachment; filename="
								+ ("ActivityInfo Export " + new Date().toString() + ".xls").replace(" ", "_"));
			}

			OutputStream os = resp.getOutputStream();
			export.getBook().write(os);

		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(500);
		}
	}
}