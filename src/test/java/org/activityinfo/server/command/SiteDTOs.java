package org.activityinfo.server.command;

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

import java.util.GregorianCalendar;

import org.activityinfo.client.local.command.handler.KeyGenerator;
import org.activityinfo.shared.dto.PartnerDTO;
import org.activityinfo.shared.dto.ProjectDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.junit.Assert;

public class SiteDTOs {

    public static SiteDTO newSite() {
        SiteDTO newSite = new SiteDTO();

        newSite.setId(new KeyGenerator().generateInt());
        newSite.setActivityId(1);
        newSite.setLocationId(1);
        newSite.setPartner(new PartnerDTO(1, "Foobar"));
        newSite.setReportingPeriodId(new KeyGenerator().generateInt());
        newSite.setDate1((new GregorianCalendar(2008, 12, 1)).getTime());
        newSite.setDate2((new GregorianCalendar(2009, 1, 3)).getTime());
        newSite.setIndicatorValue(1, 996.0);
        newSite.setIndicatorValue(2, null);
        newSite.setAttributeValue(1, true);
        newSite.setAttributeValue(2, false);
        newSite.setComments("huba huba");
        newSite.setProject(new ProjectDTO(1, "WoeiProject"));
        return newSite;
    }

    public static void validateNewSite(SiteDTO secondRead) {
        SiteDTO newSite = newSite();
        Assert.assertEquals("site.location.name", LocationDTOs.newLocation()
            .getName(), secondRead.getLocationName());
        Assert.assertEquals("site.location.axe", LocationDTOs.newLocation()
            .getAxe(), secondRead.getLocationAxe());

        Assert.assertEquals("site.attribute[1]", true,
            secondRead.getAttributeValue(1));
        Assert.assertEquals("site.reportingPeriod[0].indicatorValue[0]", 996.0,
            secondRead.getIndicatorValue(1), 0.1);
        Assert.assertEquals("site.comments", newSite.getComments(),
            secondRead.getComments());
        Assert.assertEquals("site.partner", newSite.getPartner().getId(),
            secondRead.getPartner().getId());
    }

}
