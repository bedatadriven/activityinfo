package org.activityinfo.server.util.monitoring;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.common.io.ByteStreams;
import com.google.inject.Singleton;

@Singleton
public class ClientMetricsServlet extends HttpServlet {

	private static Logger LOGGER = Logger.getLogger(ClientMetricsServlet.class);
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String body = new String(ByteStreams.toByteArray(req.getInputStream()));

//		LOGGER.info("UA: " + req.getHeader("User-agent") + "\n" +
//					"Country: " + req.getHeader("CF-IPCountry") + "\n" + 
//					body.replace(' ', '\n'));
//		

	}
	
	
}
