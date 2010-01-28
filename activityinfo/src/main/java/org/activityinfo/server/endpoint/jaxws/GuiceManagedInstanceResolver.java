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

import com.google.inject.Injector;
import com.sun.istack.NotNull;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.server.ResourceInjector;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.api.server.WSWebServiceContext;
import com.sun.xml.ws.server.AbstractMultiInstanceResolver;
import org.activityinfo.server.StartupListener;

import javax.servlet.ServletContext;
import javax.xml.ws.handler.MessageContext;

/**
 * This class is based on that of the same name from Jax-Ws commons but ours
 * is modified slightly so we use the injector created by the GuiceContextListener
 * rather than creating our own. This allows us to share singletons between the jaxws
 * web services and the rest of our endpoints, including the gwt-rpc stuff.
 * <p/>
 * See https://jax-ws-commons.dev.java.net/guice/
 *
 * @author Alex Bertram
 * @see org.activityinfo.server.StartupListener
 */
public class GuiceManagedInstanceResolver<T> extends AbstractMultiInstanceResolver<T> {

    private Injector injector;

    private ResourceInjector resourceInjector;

    private WSWebServiceContext webServiceContext;

    public GuiceManagedInstanceResolver(final Class<T> clazz)
            throws IllegalAccessException, InstantiationException {
        super(clazz);
    }


    /**
     * save the web service context instance
     *
     * @param wsc
     * @param endpoint
     */
    @Override
    public void start(final WSWebServiceContext wsc, final WSEndpoint endpoint) {
        super.start(wsc, endpoint);

        this.resourceInjector = GuiceManagedInstanceResolver.getResourceInjector(endpoint);
        this.webServiceContext = wsc;
    }


    /**
     * Let guice create the instance
     * <p/>
     * the {@code create()} method in {@code AbstractMultiInstanceResolver}
     * simply returns {@code clazz.newInstance()} so no magic happens there.
     * <p/>
     * If the endpoint is declared as singleton, the same instance will be
     * returned every time.
     *
     * @param packet
     * @return
     */
    @Override
    public T resolve(@NotNull final Packet packet) {
        // Retrieve the servlet context where we stored our app-wide injector
        // (See StartupListener)
        ServletContext servletContext =
                (ServletContext) webServiceContext.getMessageContext().get(MessageContext.SERVLET_CONTEXT);

        servletContext.log("Creating " + this.clazz.getName() + " with our Guice injector.");

        // Retrieve the injector from the servlet context
        this.injector = (Injector) servletContext.getAttribute(StartupListener.INJECTOR_NAME);

        final T instance = injector.getInstance(this.clazz);
        this.resourceInjector.inject(this.webServiceContext, instance);
        return instance;
    }
}

