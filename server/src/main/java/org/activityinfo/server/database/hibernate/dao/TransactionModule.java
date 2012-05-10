package org.activityinfo.server.database.hibernate.dao;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class TransactionModule extends AbstractModule {

	@Override
	protected void configure() {
        TransactionalInterceptor interceptor = new TransactionalInterceptor();
        requestInjection(interceptor);

        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class),
                interceptor);
	}

}
