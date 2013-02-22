

package org.activityinfo.client.filter;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.result.AdminEntityResult;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.util.CollectionUtil;

import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

class AdminTreeProxy implements DataProxy<List<AdminEntityDTO>> {

    private final Dispatcher service;
    private Filter filter;

    private Set<Integer> levelsWithChildren = new HashSet<Integer>();
    
    public AdminTreeProxy(Dispatcher service) {
        this.service = service;
    }

	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	
	@Override
    public void load(DataReader<List<AdminEntityDTO>> dataReader, final Object parent, final AsyncCallback<List<AdminEntityDTO>> callback) {
    	if (filter == null) {
    		callback.onSuccess(new ArrayList<AdminEntityDTO>());
    		return;
    	}
    	
    	service.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failed to load admin entities", caught);
			}

			@Override
			public void onSuccess(SchemaDTO schema) {
				final Set<CountryDTO> countries = findCountries(schema);
				
		    	if (CollectionUtil.isEmpty(countries)) {
		    		callback.onSuccess(new ArrayList<AdminEntityDTO>());
		    		return;
		    	}
		    	
	    		initLevelsWithChildren(countries);
	    		
		    	GetAdminEntities request = new GetAdminEntities(toIdSet(countries), filter);
		    	
		        if (parent != null) {
		            assert parent instanceof AdminEntityDTO : "expecting AdminEntityDTO";
		            request.setParentId(((AdminEntityDTO) parent).getId());
		        }
		
		        service.execute(request, new AsyncCallback<AdminEntityResult>() {
		            public void onFailure(Throwable caught) {
		                callback.onFailure(caught);
		            }
		
		            public void onSuccess(AdminEntityResult result) {
		            	prepareData(countries, result.getData());
		                callback.onSuccess(result.getData());
		            }
		        });
			}
    	});
    }

    private Set<CountryDTO> findCountries(SchemaDTO schema) {
    	Set<CountryDTO> countries = new HashSet<CountryDTO>();
    	
		Set<Integer> activityIds = filter.getRestrictions(DimensionType.Activity);
		for (Integer activityId : activityIds) {
			countries.add(schema.getActivityById(activityId).getDatabase().getCountry());
		}
		
		Set<Integer> databaseIds = filter.getRestrictions(DimensionType.Database);
		for (Integer databaseId : databaseIds) {
			countries.add(schema.getDatabaseById(databaseId).getCountry());
		} 
		
		Set<Integer> indicatorIds = filter.getRestrictions(DimensionType.Indicator);
		for (Integer indicatorId : indicatorIds) {
			countries.add(schema.getActivityByIndicatorId(indicatorId).getDatabase().getCountry());
		}

		return countries;
    }
    
    private Set<Integer> toIdSet(Set<CountryDTO> countries) {
    	Set<Integer> ids = new HashSet<Integer>();
    	for (CountryDTO country : countries) {
    		ids.add(country.getId());
    	}
    	return ids;
    }
    
    private void initLevelsWithChildren(Set<CountryDTO> countries) {
		levelsWithChildren = new HashSet<Integer>();
		for (CountryDTO country : countries) {
			for (AdminLevelDTO level : country.getAdminLevels()) {
				levelsWithChildren.add(level.getParentLevelId());
			}
		}
    }

	private void prepareData(Set<CountryDTO> countries, List<AdminEntityDTO> list) {
		if (!sameLevel(list)) {
			for (AdminEntityDTO entity : list) {
				AdminLevelDTO level = findLevel(countries, entity);
				if (level != null) {
					entity.setName( entity.getName() + " [" + level.getName() + "]");
				}
			}
		}
	}
	
	private AdminLevelDTO findLevel(Set<CountryDTO> countries, AdminEntityDTO entity) {
		for (CountryDTO country : countries) {
			AdminLevelDTO level = country.getAdminLevelById(entity.getLevelId());
			if (level != null) {
				return level;
			}
		}
		return null;
	}
	
	private boolean sameLevel(List<AdminEntityDTO> entities) {
		Iterator<AdminEntityDTO> it = entities.iterator();
		if (it.hasNext()) {
			int levelId = it.next().getLevelId();
			while (it.hasNext()) {
				if (it.next().getLevelId() != levelId) {
					return false;
				}
			}
		}
		return true;
	}
	
    public boolean hasChildren(AdminEntityDTO parent) {
    	return levelsWithChildren.contains(parent.getLevelId());
    }
}

