package org.activityinfo.client.page.config;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
