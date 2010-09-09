package org.sigmah.server.endpoint;

import org.sigmah.server.endpoint.gwtrpc.handler.GetSitesHandlerHibernate;
import org.sigmah.shared.command.handler.GetSitesHandler;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class EndpointModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(GetSitesHandler.class).to(GetSitesHandlerHibernate.class).in(Singleton.class);
    }
}
