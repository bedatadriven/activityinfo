package org.activityinfo.server.util.monitoring;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;

import com.google.common.base.Strings;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class DatadogClient {

	private static Logger LOGGER = Logger.getLogger(DatadogClient.class);

	private Provider<AuthenticatedUser> user;

	private HttpClient httpClient;
	private Gson gson;

	private String apiKey;
	private String appKey;
	
	private ExecutorService executorService;

	@Inject
	public DatadogClient(Provider<AuthenticatedUser> user, DeploymentConfiguration config) {
		this.user = user;
		this.apiKey = config.getProperty("datadog.apiKey");
		this.appKey = config.getProperty("datadog.appKey");

		httpClient = new HttpClient();
		gson = new GsonBuilder()
			.setPrettyPrinting()
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.create();
		
		executorService = Executors.newFixedThreadPool(1);
	}

	
	public void errorEvent(String message, Throwable caught) {
		StringBuilder title = new StringBuilder();
		title.append(caught.getClass().getSimpleName());
		if(!Strings.isNullOrEmpty(caught.getMessage())) {
			title.append(": ").append(caught.getMessage());
		}
		
		StringBuilder text = new StringBuilder();
		AuthenticatedUser user = getCurrentUser();
		if(user != null) {
			text.append("User: ").append(user.getEmail()).append("\n");
		}
		text.append("Stack trace:\n").append(toString(caught));
		
		Event event = new Event();
		event.setTitle(title.toString());
		event.setText(text.toString());
		event.setAlertType(Event.ERROR);

		postEvent(event);
	}
	
	private AuthenticatedUser getCurrentUser() {
		try {
			return user.get();
		} catch(Exception e) {
			return null;
		}
	}

	public void postEvent(Event event) {
		post("events", event);
	}

	public void postSeries(SeriesCollection series) {
		post("series", series);
	}

	private void post(String collectionName, Object item) {
		executorService.execute(new Request(collectionName, item));
	}
	
	public void shutdown() {
		executorService.shutdown();
	}

	private String toString(Throwable caught) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		caught.printStackTrace(pw);
		return sw.toString(); 
	}
	
	private class Request implements Runnable {
		private String collectionName;
		private Object item;
		
		public Request(String collectionName, Object item) {
			super();
			this.collectionName = collectionName;
			this.item = item;
		}

		@Override
		public void run() {
			try {
				System.out.println(gson.toJson(item));
				
				String url = "https://app.datadoghq.com/api/v1/" + collectionName + "?api_key=" + 
						apiKey + "&application_key=" + appKey;
				PostMethod post = new PostMethod(url);
				post.setRequestEntity(new StringRequestEntity(gson.toJson(item), "application/json", "UTF-8"));
				
				int result = httpClient.executeMethod(post);
				if(result != 202) {
					LOGGER.warn("Post to datadog return status code " + result);
				}
				
				System.out.println(post.getResponseBodyAsString());
				
			} catch(Exception e) {
				LOGGER.warn("Exception posting to datadog");
			}
		}		
	}
}
