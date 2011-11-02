/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.filter;

import java.util.HashSet;
import java.util.Set;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.CountryDTO;

import com.extjs.gxt.ui.client.data.BaseTreeLoader;

class AdminTreeLoader extends BaseTreeLoader<AdminEntityDTO> {

    private CountryDTO country;
    private Set<Integer> levelsWithChildren;
    
    public AdminTreeLoader(Dispatcher service) {
        super(new AdminTreeProxy(service));

    }
    
    public void setCountry(CountryDTO country) {
    	this.country = country;
    	
    	((AdminTreeProxy)this.proxy).setCountry(country);
    	
    	levelsWithChildren = new HashSet<Integer>();
    	for(AdminLevelDTO level : country.getAdminLevels()) {
    		levelsWithChildren.add(level.getParentLevelId());
    	}
    }

    @Override
    public boolean hasChildren(AdminEntityDTO parent) {
    	return levelsWithChildren.contains(parent.getLevelId());
    }

	public void setFilter(Filter filter) {
		((AdminTreeProxy)this.proxy).setFilter(filter);
	}
    
}
