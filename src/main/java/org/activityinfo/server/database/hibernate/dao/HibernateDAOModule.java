package org.activityinfo.server.database.hibernate.dao;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
