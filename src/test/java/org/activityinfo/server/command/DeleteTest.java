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

import java.util.Collection;

import junit.framework.Assert;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.shared.command.Delete;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.result.PagingResult;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.test.InjectionSupport;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.extjs.gxt.ui.client.data.ModelData;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class DeleteTest extends CommandTestCase {

    public <T extends ModelData> T getById(Collection<T> list, Integer id) {
        for (T element : list) {
            if (id.equals(element.get("id"))) {
                return element;
            }
        }
        return null;
    }

    @Test
    public void testDeleteSite() throws CommandException {

        PagingResult<SiteDTO> sites = execute(GetSites.byId(3));
        execute(new Delete(sites.getData().get(0)));

        sites = execute(GetSites.byId(3));
        Assert.assertEquals(0, sites.getData().size());

        sites = execute(new GetSites());
        Assert.assertNull(getById(sites.getData(), 3));
    }

    @Test
    public void testDeleteIndicator() throws CommandException {

        SchemaDTO schema = execute(new GetSchema());
        execute(new Delete(schema.getIndicatorById(1)));

        schema = execute(new GetSchema());
        Assert.assertNull(schema.getIndicatorById(1));

        PagingResult<SiteDTO> sites = execute(GetSites.byId(1));
        Assert.assertNull(sites.getData().get(0).getIndicatorValue(1));
    }

    @Test
    public void testDeleteAttribute() throws CommandException {

        SchemaDTO schema = execute(new GetSchema());
        execute(new Delete(schema.getActivityById(1).getAttributeById(1)));

        schema = execute(new GetSchema());
        Assert.assertNull(schema.getActivityById(1).getAttributeById(1));
    }

    @Test
    public void testDeleteActivity() throws CommandException {

        SchemaDTO schema = execute(new GetSchema());
        execute(new Delete(schema.getActivityById(1)));
        execute(new Delete("Activity", 4));

        schema = execute(new GetSchema());
        Assert.assertNull("delete by entity reference",
            schema.getActivityById(1));
        Assert.assertNull("delete by id", schema.getActivityById(4));
    }
}
