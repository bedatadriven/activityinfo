package org.sigmah.client.page.dashboard;

import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.GetSitesWithoutCoordinates;
import org.sigmah.shared.command.result.SitesWithoutLocationsResult;
import org.sigmah.shared.dto.SiteDTO;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SitesWithoutLocations extends VerticalPanel {
	private Dispatcher service;
	//private AsyncMonitor loadingMonitor = new MaskingAsyncMonitor(this, I18N.CONSTANTS.loading());
	
	public SitesWithoutLocations(Dispatcher service) {
		this.service=service;
		setTitle(0);
		getSitesWithoutCoordinates();
	}

	private void setTitle(int i) {
		String title = "Sites without coordinates";
		setTitle(i == 0 ? title : title + " (" + Integer.toString(i) + ")");
	}

	private void getSitesWithoutCoordinates() {
		service.execute(
			new GetSitesWithoutCoordinates()
				.setMaxLocations(10),
			null, 
			new AsyncCallback<SitesWithoutLocationsResult>() {
				@Override
				public void onFailure(Throwable caught) {
					System.out.println();
				}

				@Override
				public void onSuccess(SitesWithoutLocationsResult result) {
					setSites(result.getData());
					setTitle(result.getTotalLocationsCount());
				}

				private void setSites(List<SiteDTO> data) {
					for (SiteDTO site : data) {
						HorizontalPanel panel = new HorizontalPanel();
						panel.setSpacing(5);
//						panel.add(IconImageBundle.ICONS.edit().createImage());
//						panel.add(IconImageBundle.ICONS.site().createImage());
						panel.add(new Label(site.getLocationName()));
						panel.add(new Label(DateTimeFormat.getFormat("yyyy-MMM-dd").format(site.getDateEdited())));
						add(panel);
					}
				}
			}
		);
	}

}