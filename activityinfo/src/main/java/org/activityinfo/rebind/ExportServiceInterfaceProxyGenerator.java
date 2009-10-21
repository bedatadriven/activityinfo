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

package org.activityinfo.rebind;

import com.google.gwt.user.rebind.rpc.ServiceInterfaceProxyGenerator;
import com.google.gwt.user.rebind.rpc.ProxyCreator;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.core.ext.typeinfo.JClassType;
import org.activityinfo.client.report.ExportServiceProxy;

/**
 * @author Alex Bertram
 */
public class ExportServiceInterfaceProxyGenerator extends ServiceInterfaceProxyGenerator {

    @Override
    protected ProxyCreator createProxyCreator(JClassType remoteService) {
        return new ProxyCreator(remoteService) {
            @Override
            protected Class<? extends RemoteServiceProxy> getProxySupertype() {
                return ExportServiceProxy.class;
            }
        };
    }
}
