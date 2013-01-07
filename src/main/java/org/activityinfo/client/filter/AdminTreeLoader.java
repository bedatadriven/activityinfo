/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.filter;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.dto.AdminEntityDTO;

import com.extjs.gxt.ui.client.data.BaseTreeLoader;

class AdminTreeLoader extends BaseTreeLoader<AdminEntityDTO> {

    public AdminTreeLoader(Dispatcher service) {
        super(new AdminTreeProxy(service));
    }

    @Override
    public boolean hasChildren(AdminEntityDTO parent) {
    	return ((AdminTreeProxy)this.proxy).hasChildren(parent);
    }

	public void setFilter(Filter filter) {
		((AdminTreeProxy)this.proxy).setFilter(filter);
	}
}
