package org.sigmah.server.endpoint.content;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.common.io.ByteStreams;
import com.google.inject.Singleton;

/**
 * Proxies content from the ActivityInfo WordPress site. 
 */
@Singleton
public class ContentServlet extends HttpServlet {
	
	public static final String ROOT = "content/";

	@Override
	protected void doGet(HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		
		
        HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://activityinfo.dreamhosters.com" + req.getRequestURI());

        httpclient.execute(httpget, new ResponseHandler<Void>() {

			@Override
			public Void handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				
				resp.setStatus(response.getStatusLine().getStatusCode());

				HttpEntity entity = response.getEntity();
				resp.setContentType(entity.getContentType().getValue());
				InputStream in = entity.getContent();
				try {
					ByteStreams.copy(in, resp.getOutputStream());
				} finally {
					in.close();
				}
				return null;
			}
		});
	}
}
