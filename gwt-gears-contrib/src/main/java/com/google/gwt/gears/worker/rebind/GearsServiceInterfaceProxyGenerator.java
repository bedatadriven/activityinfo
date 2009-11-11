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

package com.google.gwt.gears.worker.rebind;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.gears.worker.client.rpc.GearsServiceProxy;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.rebind.rpc.ProxyCreator;
import com.google.gwt.user.rebind.rpc.ServiceInterfaceProxyGenerator;

/**
 * Changes the default behavior of the <code>ServiceInterfaceProxyGenerator</code> to create the actual
 * proxy class with <code>WorkerProxyCreator</code>.
 * <p/>
 * The point of all of this is to generate the proxy with a super class of <code>WorkerServiceProxy</code>,
 * which uses the Gears <code>HttpRequest</code> API rather than the GWT's HttpRequest API, which
 * isn't available from the worker thread.
 *
 * @author Alex Bertram
 */
public class GearsServiceInterfaceProxyGenerator extends ServiceInterfaceProxyGenerator {

  @Override
  protected ProxyCreator createProxyCreator(JClassType remoteService) {
    return new ProxyCreator(remoteService) {
      @Override
      protected Class<? extends RemoteServiceProxy> getProxySupertype() {
        return GearsServiceProxy.class;
      }
    };
  }
}
