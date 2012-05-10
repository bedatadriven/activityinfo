package org.activityinfo.client.page.config;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.FrameId;
import org.activityinfo.client.page.common.nav.NavigationPanel;
import org.activityinfo.client.widget.VSplitFrameSet;

import com.google.inject.Inject;

public class ConfigFrameSet extends VSplitFrameSet {

	public static final FrameId PAGE_ID = new FrameId("config");

	@Inject
	public ConfigFrameSet(EventBus eventBus, Dispatcher dispatcher) {
		super(ConfigFrameSet.PAGE_ID, new NavigationPanel(eventBus, 
				new ConfigNavigator(dispatcher, I18N.CONSTANTS, IconImageBundle.ICONS)));
	}

}
