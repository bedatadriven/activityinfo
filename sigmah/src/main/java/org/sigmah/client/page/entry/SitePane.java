package org.sigmah.client.page.entry;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.entry.form.SiteRenderer;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SitePane extends ContentPanel {

	private final Html content;
	private final Dispatcher dispatcher;
	
	public SitePane(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		setHeading(I18N.CONSTANTS.site());
		setIcon(IconImageBundle.ICONS.mapped());
		
		content = new Html();
        content.setStyleName("details");
		add(content);
		
		ActionToolBar toolBar = new ActionToolBar();
		toolBar.addEditButton();
		
		setTopComponent(toolBar);
		
	}
	
	public void setSite(final SiteDTO site) {
		setHeading(site.getLocationName());
		content.setHtml(I18N.CONSTANTS.loading());
		dispatcher.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {

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
