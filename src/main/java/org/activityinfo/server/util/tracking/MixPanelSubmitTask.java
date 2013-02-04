package org.activityinfo.server.util.tracking;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.mixpanel.mixpanelapi.MixpanelAPI;

@Singleton
public class MixPanelSubmitTask extends HttpServlet {

	public static String END_POINT = "/tasks/mixpanel/submit";
	
	private final MixpanelAPI mixpanel = new MixpanelAPI();
	
	private static final Logger LOGGER = Logger.getLogger(MixPanelSubmitTask.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		try {
			mixpanel.sendMessage(new JSONObject(req.getParameter("message")));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to submit MixPanel message:\n" + req.getParameter("message"), e);
		}
	}

}
