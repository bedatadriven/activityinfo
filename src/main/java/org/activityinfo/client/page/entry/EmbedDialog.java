package org.activityinfo.client.page.entry;

import java.util.Map;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.entry.place.DataEntryPlace;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.FilterUrlSerializer;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.Published;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EmbedDialog extends Dialog {

	private final Dispatcher dispatcher;
	private TextField<String> urlText;
	private TextField<String> embedText;
	
	public EmbedDialog(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		setWidth(300);
		setHeight(200);
		setHeading(I18N.CONSTANTS.embed());
		
		VBoxLayout layout = new VBoxLayout();
		layout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
		layout.setPadding(new Padding(10));
		setLayout(layout);
		
		add(new Label("Paste link:"));
		urlText = new TextField<String>();
		urlText.setReadOnly(true);
		urlText.setSelectOnFocus(true);
		add(urlText);	
		
		add(new Label(" "));
		add(new Label("Paste HTML to embed into websites:"));
		
		embedText = new TextField<String>();
		embedText.setReadOnly(true);
		embedText.setSelectOnFocus(true);
		add(embedText);	
	}
	
	public void show(String url) {
		urlText.setValue(url);
		embedText.setValue("<iframe src=\"" + url + "\" width=\"400\" height=\"200\"></iframe>");
		super.show();
	}
	
	public void show(final DataEntryPlace place) {
		final String url = "http://www.activityinfo.org/embed.html?sites=" + FilterUrlSerializer.toUrlFragment(place.getFilter());
		
		dispatcher.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(SchemaDTO result) {
				Filter filter = place.getFilter();
				if(filter.isDimensionRestrictedToSingleCategory(DimensionType.Activity)) {
					ActivityDTO singleActivity = result.getActivityById( filter.getRestrictedCategory(DimensionType.Activity));
					checkPublication(singleActivity, url);
				} else if(filter.isDimensionRestrictedToSingleCategory(DimensionType.Database)) {
					MessageBox.alert("foo", "not impl", null);
				}
			}
		});
	}

	private void checkPublication(final ActivityDTO activity, final String url) {
		if(activity.getPublished() != Published.ALL_ARE_PUBLISHED.getIndex()) {
			if(activity.getDatabase().isDesignAllowed()) {
				MessageBox.confirm(I18N.CONSTANTS.embed(), I18N.MESSAGES.promptPublishActivity(activity.getName()), 
						new Listener<MessageBoxEvent>() {
					
					@Override
					public void handleEvent(MessageBoxEvent be) {
						if(be.getButtonClicked().getItemId().equals(Dialog.YES)) {
							publishActivity(activity, url);
						}
					}

				});
			} else {
				MessageBox.alert(I18N.CONSTANTS.embed(), I18N.MESSAGES.activityNotPublic(activity.getName()), null);
			}
		} else {
			show(url);
		}
	}

	protected void publishActivity(ActivityDTO activity, final String url) {
		Map<String, Object> changes = Maps.newHashMap();
		changes.put("published", Published.ALL_ARE_PUBLISHED.getIndex());
		
		UpdateEntity update = new UpdateEntity(activity, changes);
		dispatcher.execute(update, null, new AsyncCallback<VoidResult>() {

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert(I18N.CONSTANTS.embed(), "There was an error encounted on the server while trying to publish the activity", null);
			}

			@Override
			public void onSuccess(VoidResult result) {
				show(url);
			}
		});
	}
	
}
