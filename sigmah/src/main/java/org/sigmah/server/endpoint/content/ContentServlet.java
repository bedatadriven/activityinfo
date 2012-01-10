package org.sigmah.server.endpoint.content;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.google.inject.Singleton;

/**
 * Proxies content from the ActivityInfo WordPress site. 
 */
@Singleton
public class ContentServlet extends HttpServlet {
	
	public static final String ROOT = "content/";
	
	public static final String[] HEADERS_TO_COPY = new String[] { "Cookie", "Accept-Language", 
		"Accept-Encoding"};

	@Override
	protected void doGet(HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {

		String url = "http://activityinfo.dreamhosters.com" + req.getRequestURI();
		if(!Strings.isNullOrEmpty(req.getQueryString())) {
			url += "?" + req.getQueryString();
		}
		
        HttpClient httpclient = createClient();
		HttpGet httpget = new HttpGet(url);
		
		passThroughHeaders(req, httpget);
        proxy(httpclient, httpget, resp);
	}

	@Override
	protected void doPost(final HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
	    HttpClient httpclient = createClient();
		HttpPost httppost = new HttpPost("http://activityinfo.dreamhosters.com" + req.getRequestURI());
		passThroughHeaders(req, httppost);
		httppost.setEntity(new InputStreamEntity(req.getInputStream(), -1));
		proxy(httpclient, httppost, resp);
     
	}


	private HttpClient createClient() {
		HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
        httpclient.getParams().setParameter(ClientPNames.VIRTUAL_HOST, new HttpHost("www.activityinfo.org"));
		return httpclient;
	}

	
	private void passThroughHeaders(HttpServletRequest req, HttpRequestBase method) {
		for(String header : HEADERS_TO_COPY) {
			String headerValue = req.getHeader(header);
			if(!Strings.isNullOrEmpty(headerValue)) {
				method.addHeader(header, headerValue);
			}		
		}
		method.addHeader("Host", "www.activityinfo.org");
	}
	
	private void proxy(HttpClient httpclient, HttpRequestBase method,
			final HttpServletResponse resp) throws IOException,
			ClientProtocolException {
		
		httpclient.execute(method, new ResponseHandler<Void>() {

			@Override
			public Void handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
			
				resp.setStatus(response.getStatusLine().getStatusCode());
				
				for(Header header : response.getAllHeaders()) {
					resp.addHeader(header.getName(), header.getValue());
				}
				
				HttpEntity entity = response.getEntity();
		
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
