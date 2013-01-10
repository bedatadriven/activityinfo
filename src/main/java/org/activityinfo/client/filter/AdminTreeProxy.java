/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.filter;

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
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.report.model.DimensionType;

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
				final CountryDTO country = findCountry(schema);
				
		    	if (country == null) {
		    		callback.onSuccess(new ArrayList<AdminEntityDTO>());
		    		return;
		    	}

	    		initLevelsWithChildren(country);
	    		
		    	GetAdminEntities request = new GetAdminEntities(country.getId(), filter);
		    	
		        if (parent != null) {
		            assert parent instanceof AdminEntityDTO : "expecting AdminEntityDTO";
		            request.setParentId(((AdminEntityDTO) parent).getId());
		        }
		
		        service.execute(request, new AsyncCallback<AdminEntityResult>() {
		            public void onFailure(Throwable caught) {
		                callback.onFailure(caught);
		            }
		
		            public void onSuccess(AdminEntityResult result) {
		            	prepareData(country, result.getData());
		                callback.onSuccess(result.getData());
		            }
		        });
			}
    	});
    }

    private CountryDTO findCountry(SchemaDTO schema) {
    	CountryDTO country = null;
    	
		Set<Integer> activityIds = filter.getRestrictions(DimensionType.Activity);
		if (!activityIds.isEmpty()) {
			ActivityDTO activity = schema.getActivityById(activityIds.iterator().next());
			country = activity.getDatabase().getCountry();
		} else {
			Set<Integer> databaseIds = filter.getRestrictions(DimensionType.Database);
			if (!databaseIds.isEmpty()) {
				UserDatabaseDTO database = schema.getDatabaseById(databaseIds.iterator().next());
				country = database.getCountry();
			}
		}
		
		return country;
    }
    
    private void initLevelsWithChildren(CountryDTO country) {
		levelsWithChildren = new HashSet<Integer>();
		for (AdminLevelDTO level : country.getAdminLevels()) {
			levelsWithChildren.add(level.getParentLevelId());
		}
    }

	private void prepareData(CountryDTO country, List<AdminEntityDTO> list) {
		if (!sameLevel(list)) {
			for (AdminEntityDTO entity : list) {
				AdminLevelDTO level = country.getAdminLevelById(entity.getLevelId());
				entity.setName( entity.getName() + " [" + level.getName() + "]");
			}
		}
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

