/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.endpoint.jaxws;

import com.sun.xml.ws.api.FeatureConstructor;

import javax.xml.ws.WebServiceFeature;

/**
 * The feature, just holds a unique ID and sets the enabled flag.
 *
 * @author Marcus Eriksson, krummas@gmail.com
 * @since Nov 4, 2008
 */
public class GuiceManagedFeature extends WebServiceFeature {
    public static final String ID="marcus.guice.managed.feature";

    @FeatureConstructor
    public GuiceManagedFeature()
    {
        this.enabled=true;
    }

    public String getID() {
        return ID;
    }
}

