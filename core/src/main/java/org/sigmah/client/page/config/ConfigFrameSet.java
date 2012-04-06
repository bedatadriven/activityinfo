package org.sigmah.client.page.config;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.FrameId;
import org.sigmah.client.page.common.nav.NavigationPanel;
import org.sigmah.client.widget.VSplitFrameSet;

import com.google.inject.Inject;

public class ConfigFrameSet extends VSplitFrameSet {

	public static final FrameId PAGE_ID = new FrameId("config");

	@Inject
	public ConfigFrameSet(EventBus eventBus, Dispatcher dispatcher) {
		super(ConfigFrameSet.PAGE_ID, new NavigationPanel(eventBus, 
				new ConfigNavigator(dispatcher, I18N.CONSTANTS, IconImageBundle.ICONS)));
	}

}
