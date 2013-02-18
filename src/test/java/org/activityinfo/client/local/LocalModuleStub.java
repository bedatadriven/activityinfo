package org.activityinfo.client.local;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.MockEventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.remote.Remote;
import org.activityinfo.client.i18n.UIConstants;
import org.activityinfo.client.local.command.HandlerRegistry;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.query.SqlDialect;
import com.bedatadriven.rebar.sql.client.query.SqliteDialect;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class LocalModuleStub extends AbstractModule {
	
	private final SqlDatabase database;
	private final AuthenticatedUser authentication;
	private Dispatcher remoteDispatcher;
	
	public LocalModuleStub(AuthenticatedUser auth, SqlDatabase database, 
			Dispatcher remoteDispatcher) {
		this.database = database;
		this.authentication = auth;
		this.remoteDispatcher = remoteDispatcher;
	}
	
	@Override
	protected void configure() {
		bind(SqlDatabase.class).toInstance(database);
		bind(AuthenticatedUser.class).toInstance(authentication);
		bind(HandlerRegistry.class).toProvider(HandlerRegistryProvider.class);
		bind(SqlDialect.class).to(SqliteDialect.class);
		bind(Dispatcher.class).annotatedWith(Remote.class).toInstance(remoteDispatcher);
		bind(EventBus.class).to(MockEventBus.class);
	}
	
	@Provides
	public UIConstants provideConstants() {
		return (UIConstants) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { UIConstants.class },
				new InvocationHandler() {
			
			@Override
			public Object invoke(Object instance, Method method, Object[] arguments)
					throws Throwable {
				return method.getName();
			}
		});
	}
}
