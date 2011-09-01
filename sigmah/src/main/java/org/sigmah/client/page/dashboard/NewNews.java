package org.sigmah.client.page.dashboard;

import org.sigmah.client.dispatch.Dispatcher;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;

class NewNews extends AiPortlet {
	public NewNews(Dispatcher service) {
		super(service, "News from BeDataDriven");
		setHeight("auto");
		
		setAutoWidth(true);
		setLayout(new RowLayout(Orientation.VERTICAL));
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		add(horizontalPanel);
		
		LabelField lblfldNewLabelfield = new LabelField("New LabelField");
		horizontalPanel.add(lblfldNewLabelfield);
		
		LabelField lblfldNewLabelfield_1 = new LabelField("New LabelField");
		horizontalPanel.add(lblfldNewLabelfield_1);
		
		Html htmlnewhtmlcomponent = new Html("<b>New</b><br><i>Html<i><br><u>Component</>");
		add(htmlnewhtmlcomponent, new RowData(Style.DEFAULT, Style.DEFAULT, new Margins(5, 5, 5, 5)));
		
		Hyperlink hprlnkNewHyperlink = new Hyperlink("New hyperlink", false, "newHistoryToken");
		add(hprlnkNewHyperlink);

		StringBuilder text = new StringBuilder();
		for (int i = 0; i < 40; i++) {
			text.append("Hey! Cool new Stuff?!?! \n");
		}
		
//		RequestBuilder request = new RequestBuilder(RequestBuilder.GET, "http://bedatadriven.com/blog/wpGetPage.php");
//		request.setHeader("Content-Type", "application/json");
//		request.setRequestData("");
//		try {
//			request.sendRequest("wp.getPage", new RequestCallback() {
//				@Override
//				public void onResponseReceived(Request request, Response response) {
//					response.
//				}
//				
//				@Override
//				public void onError(Request request, Throwable exception) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
//		} catch (RequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}