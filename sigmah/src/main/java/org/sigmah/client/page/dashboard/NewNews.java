package org.sigmah.client.page.dashboard;

import org.sigmah.client.dispatch.Dispatcher;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NewNews extends VerticalPanel {
	private HTML hyperlinkReadMore;
	private HTML htmlContent;
	private Label labelAuthor;
	private Label labelDateTimePublished;
	private Dispatcher service;

	public NewNews(Dispatcher service) {
		this.service=service;
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		add(horizontalPanel);
		
		labelDateTimePublished = new Label("Published at");
		horizontalPanel.add(labelDateTimePublished);
		
		labelAuthor = new Label("Ruud Poutsma");
		horizontalPanel.add(labelAuthor);
		
		htmlContent = new HTML("<b>New</b><br><i>Html<i><br><u>Component</>"); 
		add(htmlContent);
		
		hyperlinkReadMore = new HTML("woei woei woei ");
		add(hyperlinkReadMore);

		StringBuilder text = new StringBuilder();
		for (int i = 0; i < 40; i++) {
			text.append("Hey! Cool new Stuff?!?! \n");
		}
		
		getNews();

	}

	private void getNews() {
		String url = "http://activityinfo.wordpress.com/xmlrpc.php";
		
		RequestBuilder request = new RequestBuilder(RequestBuilder.POST, url);
		request.setHeader("Content-Type", "application/xml");
		request.setRequestData("blog_id=activityinfo");
		request.setPassword("testpassword");
		request.setUser("activityinfo");
		try {
			request.sendRequest("wp.getPages", new RequestCallback() {
				@Override
				public void onError(Request request, Throwable exception) {
					System.out.println();
				}

				@Override
				public void onResponseReceived(Request request, Response response) {
					System.out.println();
				}


			});
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setHyperLinkHtml(String link) {
		//hyperlinkReadMore.setHtml("<a href=\"" + link + ">" + "Read more..." + "</a>");
	}
}