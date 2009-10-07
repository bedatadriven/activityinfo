/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.command.handler;

import com.google.inject.Inject;
import org.activityinfo.server.domain.*;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.IllegalAccessCommandException;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.Map;

/**
 * @see org.activityinfo.shared.command.UpdateEntity
 *
 * @author Alex Bertram
 */
public class UpdateEntityHandler extends BaseEntityHandler implements CommandHandler<UpdateEntity> {

    @Inject
    public UpdateEntityHandler(EntityManager em) {
        super(em);
    }

    @Override
	public CommandResult execute(UpdateEntity cmd, User user)
			throws CommandException {
	
		Map<String, Object> changes = cmd.getChanges().getTransientMap();
		         
		if("Activity".equals(cmd.getEntityName())) {
            updateActivity(user, cmd, changes);
			
		} else if("AttributeGroup".equals(cmd.getEntityName())) {
            updateAttributeGroup(cmd, changes);
			
		} else if("Attribute".equals(cmd.getEntityName())) {
	        updateAttribute(user, cmd, changes);
			
		} else if("Indicator".equals(cmd.getEntityName())) {
            updateIndicator(user, cmd, changes);

        } else if("Site".equals(cmd.getEntityName())) {
            updateSite(user, cmd, changes);

		} else {
			throw new RuntimeException("unknown entity type");
		}
		
		return null;
	}

    private void updateIndicator(User user, UpdateEntity cmd, Map<String, Object> changes) throws IllegalAccessCommandException {
        Indicator indicator = em.find(Indicator.class, cmd.getId());

        assertDesignPriviledges(user, indicator.getActivity().getDatabase());

        updateIndicatorProperties(indicator, changes);
    }

    private void updateAttribute(User user, UpdateEntity cmd, Map<String, Object> changes) {
        Attribute attribute = em.find(Attribute.class, cmd.getId());

        // TODO: decide where attributes belong and how to manage them
   //     assertDesignPriviledges(user, attribute.get);

        updateAttributeProperties(changes, attribute);
    }

    private void updateAttributeGroup(UpdateEntity cmd, Map<String, Object> changes) {
        AttributeGroup group = em.find(AttributeGroup.class, cmd.getId() );


        updateAttributeGroupProperties(group, changes);
    }

    private void updateActivity(User user, UpdateEntity cmd, Map<String, Object> changes) throws IllegalAccessCommandException {
        Activity activity = em.find(Activity.class, cmd.getId());

        assertDesignPriviledges(user, activity.getDatabase());

        updateActivityProperties(activity, changes);
    }

    protected void updateSite(User user, UpdateEntity cmd, Map<String, Object> changes) throws IllegalAccessCommandException {

        Site site = em.find(Site.class, cmd.getId());

        assertSiteEditPriveleges(user, site.getActivity(), site.getPartner());

        site.setDateEdited(new Date());

        updateSiteProperties(site, changes, false);
        updateAttributeValueProperties(site, changes, false);
        updateLocationProperties(site.getLocation(), changes);
        updateAdminProperties(site.getLocation(), changes, false);

        ReportingPeriod period = site.primaryReportingPeriod();
        if(period != null) {
            updatePeriodProperties(period, changes, false);
            updateIndicatorValueProperties(period, changes, false);
        }
    }


}
