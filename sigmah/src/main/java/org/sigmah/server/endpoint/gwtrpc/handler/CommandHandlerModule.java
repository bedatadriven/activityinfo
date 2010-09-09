package org.sigmah.server.endpoint.gwtrpc.handler;


import org.sigmah.shared.command.handler.GetSitesHandler;

import com.google.inject.AbstractModule;

/**
 * Guice module for binding hibernate specific command handlers.
 *
 * @author Alex Bertram
 */
public class CommandHandlerModule extends AbstractModule {

    @Override
    protected void configure() {
       
    	bind(GetSitesHandler.class).to(GetSitesHandlerHibernate.class);
    }
}
