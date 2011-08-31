package org.sigmah.client.page.dashboard;

import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.command.GetLocationsWithoutGpsCoordinates;
import org.sigmah.shared.command.result.LocationsWithoutGpsResult;
import org.sigmah.shared.dto.LocationDTO;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SitesWithoutLocations extends AiPortlet {

	public SitesWithoutLocations(Dispatcher service) {
		super(service, "");

		RowLayout layout = new RowLayout();
		layout.setOrientation(Orientation.VERTICAL);
		setLayout(layout);
		setAutoHeight(true);

		setTitle(0);
		
		getSitesWithoutCoordinates();
	}

	private void setTitle(int i) {
		String title = "Sites without coordinates";
		setTitle(i == 0 ? title : title + " (" + Integer.toString(i) + ")");
	}

	private void getSitesWithoutCoordinates() {
		service.execute(
			new GetLocationsWithoutGpsCoordinates().setMaxLocations(50),
			loadingMonitor, new AsyncCallback<LocationsWithoutGpsResult>() {
				@Override
				public void onFailure(Throwable caught) {
					System.out.println();
				}

				@Override
				public void onSuccess(LocationsWithoutGpsResult result) {
					setSites(result.getData());
					setTitle(result.getTotalLocationsCount());
				}

				private void setSites(List<LocationDTO> data) {
					RowData rowData = new RowData(-1, -1, new Margins(5));
					for (LocationDTO location : data) {
						HorizontalPanel panel = new HorizontalPanel();
						panel.add(IconImageBundle.ICONS.edit().createImage());
						panel.add(IconImageBundle.ICONS.site().createImage());
						panel.add(new LabelField(location.getName()));
						add(panel, new RowData(-1, -1, new Margins(5)));
					}
					layout(true);
				}
			}
		);
	}

}