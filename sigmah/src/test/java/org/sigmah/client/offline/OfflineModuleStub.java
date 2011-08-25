package org.sigmah.client.offline;

import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.offline.command.HandlerRegistry;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.google.inject.AbstractModule;

public class OfflineModuleStub extends AbstractModule {
	
	private final SqlDatabase database;
	private final Authentication authentication;
	
	public OfflineModuleStub(Authentication auth, SqlDatabase database) {
		this.database = database;
		this.authentication = auth;
	}
	
	@Override
	protected void configure() {
		bind(SqlDatabase.class).toInstance(database);
		bind(Authentication.class).toInstance(authentication);
		bind(HandlerRegistry.class).toProvider(HandlerRegistryProvider.class);
	}
	
	

}
