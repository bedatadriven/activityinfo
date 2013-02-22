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

import java.util.List;

import org.activityinfo.server.database.hibernate.entity.Country;

/**
 * Data Access Object for
 * {@link org.activityinfo.server.database.hibernate.entity.Country} objects.
 * Implemented by
 * {@link org.activityinfo.server.database.hibernate.dao.DAOInvocationHandler
 * proxy}, see the Country class for query definitions.
 * 
 * @author Alex Bertram
 */
public interface CountryDAO extends DAO<Country, Integer> {

    /**
     * Returns a list of Countries in alphabetical order. See
     * {@link org.activityinfo.server.database.hibernate.entity.Country} for
     * query definition
     * 
     * @return a list of Countries in alphabetical order
     */
    List<Country> queryAllCountriesAlphabetically();

}
