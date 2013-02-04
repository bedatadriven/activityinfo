package org.activityinfo.server.util.tracking;

import com.google.inject.servlet.ServletModule;


public class TrackingModule extends ServletModule {

	@Override
	protected void configureServlets() {
		serve(UpdateProfilesTask.END_POINT).with(UpdateProfilesTask.class);
		serve(MixPanelSubmitTask.END_POINT).with(MixPanelSubmitTask.class);
	}

}
