package org.activityinfo.client.page.entry;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.entry.form.SiteRenderer;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DetailTab extends TabItem {

	private final Html content;
	private final Dispatcher dispatcher;
	
	public DetailTab(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		setText(I18N.CONSTANTS.details());
		//setIcon(IconImageBundle.ICONS.mapped());
		
		content = new Html();
        content.setStyleName("details");
		add(content);
	
	}
	
	public void setSite(final SiteDTO site) {
		content.setHtml(I18N.CONSTANTS.loading());
		dispatcher.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(SchemaDTO result) {
				render(result, site);
				
			}
		});
	}

	private void render(SchemaDTO schema, SiteDTO site) {	
		SiteRenderer renderer = new SiteRenderer();
		content.setHtml(renderer.renderSite(site, schema.getActivityById(site.getActivityId()), false, true));		
	}
	
}
