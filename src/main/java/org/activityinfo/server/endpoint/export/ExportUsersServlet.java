/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.activityinfo.server.endpoint.export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.shared.command.GetUsers;
import org.activityinfo.shared.command.result.UserResult;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Exports complete Users List to an Excel file
 * 
 * @author Muhammad Abid
 */
@Singleton
public class ExportUsersServlet extends HttpServlet {

	   private DispatcherSync dispatcher;
	    
	    @Inject
	    public ExportUsersServlet(DispatcherSync dispatcher) {
	        this.dispatcher = dispatcher;
	    }

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		int dbId = Integer.valueOf(req.getParameter("dbUsers"));

		try {

			UserResult userResult  = dispatcher.execute(new GetUsers(dbId));
		
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