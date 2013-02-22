package org.activityinfo.client.local.capability;

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

/**
 * Internet Explorer 6-8 offline capability profile.
 * 
 * If gears is not installed, the user is prompted to install.
 * 
 * IE itself does not support offline features. There is an "experimental"
 * indexeddb plugin that can be downloaded from MS lab's web site, but there is
 * absolutely no appcache support, even in IE9.
 */
public class IECapabilityProfile extends GearsCapabilityProfile {

    @Override
    public String getInstallInstructions() {
        return ProfileResources.INSTANCE.startupMessageIE().getText();
    }

}
