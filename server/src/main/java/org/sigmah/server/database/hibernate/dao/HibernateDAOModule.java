package org.sigmah.server.database.hibernate.dao;

import com.google.inject.AbstractModule;

public class HibernateDAOModule extends AbstractModule {



	@Override
	protected void configure() {
		bind(AdminDAO.class).to(AdminHibernateDAO.class);
		bindDAOProxy(ActivityDAO.class);
		bindDAOProxy(AuthenticationDAO.class);
		bindDAOProxy(CountryDAO.class);
		bindDAOProxy(IndicatorDAO.class);
		bindDAOProxy(ReportDefinitionDAO.class);
		bindDAOProxy(PartnerDAO.class);
		bindDAOProxy(UserDatabaseDAO.class);
		bindDAOProxy(UserPermissionDAO.class);
		bind(UserDAO.class).to(UserDAOImpl.class);
		
	}
	private <T extends DAO> void bindDAOProxy(Class<T> daoClass) {
		HibernateDAOProvider<T> provider = new HibernateDAOProvider<T>(daoClass);
		requestInjection(provider);

		bind(daoClass).toProvider(provider);
	}

}
