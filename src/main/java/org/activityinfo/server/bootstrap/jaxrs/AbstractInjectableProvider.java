package org.activityinfo.server.bootstrap.jaxrs;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.ws.rs.core.Context;

import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

public abstract class AbstractInjectableProvider<E> extends
		AbstractHttpContextInjectable<E> implements
		InjectableProvider<Context, Type> {

	private final Type t;

	public AbstractInjectableProvider() {
		this.t = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Override
	public Injectable<E> getInjectable(ComponentContext ic, Context a, Type c) {
		if (c.equals(t)) {
			return getInjectable(ic, a);
		}

		return null;
	}

	public Injectable<E> getInjectable(ComponentContext ic, Context a) {
		return this;
	}

	@Override
	public ComponentScope getScope() {
		return ComponentScope.PerRequest;
	}
}
