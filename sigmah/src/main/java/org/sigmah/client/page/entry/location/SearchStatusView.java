package org.sigmah.client.page.entry.location;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.command.result.LocationResult;

import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.widget.Html;

public class SearchStatusView extends Html {

	public SearchStatusView(LocationSearchPresenter presenter) {
		presenter.getStore().getLoader().addLoadListener(new LoadListener() {

			
			@Override
			public void loaderBeforeLoad(LoadEvent le) {
				setHtml("");
			}

			@Override
			public void loaderLoadException(LoadEvent le) {
				setHtml(I18N.CONSTANTS.connectionProblem());
			}

			@Override
			public void loaderLoad(LoadEvent le) {
				LocationResult data = le.getData();
				if(data.getData().isEmpty()) {
					setHtml("Your search did not match any existing locations. Make your search more general or add a " +
							"new location.");
				} else if(data.getTotalLength() > data.getData().size()) {
					setHtml("Your search matches " + data.getTotalLength() + " locations, please narrow your search above");
				} 
			}
		});
	}
	
}
