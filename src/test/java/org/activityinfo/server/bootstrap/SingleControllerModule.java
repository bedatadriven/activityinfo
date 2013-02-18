package org.activityinfo.server.bootstrap;

import com.google.inject.AbstractModule;

public class SingleControllerModule extends AbstractModule {
	private Class<?> clazz;

	public SingleControllerModule(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	protected void configure() {
		bind(clazz);
	}
}
