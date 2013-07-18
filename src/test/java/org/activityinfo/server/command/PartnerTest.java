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

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.shared.command.AddPartner;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.PartnerDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.test.InjectionSupport;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class PartnerTest extends CommandTestCase {
    public static final int PEAR_PLUS_DB_ID = 2;
    public static final int SOL_ID = 2;

    @Test
    public void testAddPartner() throws Exception {
        PartnerDTO newPartner = new PartnerDTO();
        newPartner.setName("Solidarites");

        CreateResult cr = execute(new AddPartner(PEAR_PLUS_DB_ID, newPartner));

        Assert.assertEquals(SOL_ID, cr.getNewId());

        SchemaDTO schema = execute(new GetSchema());
        PartnerDTO partner = schema.getDatabaseById(PEAR_PLUS_DB_ID)
            .getPartnerById(SOL_ID);

        Assert.assertNotNull(partner);
        Assert.assertEquals(newPartner.getName(), partner.getName());
    }

    @Test
    public void testAddNewPartner() throws Exception {
        PartnerDTO newPartner = new PartnerDTO();
        newPartner.setName("VDE");
        newPartner.setFullName("Vision d'Espoir");

        CreateResult cr = execute(new AddPartner(1, newPartner));

        SchemaDTO schema = execute(new GetSchema());
        PartnerDTO partner = schema.getDatabaseById(1).getPartnerById(
            cr.getNewId());

        Assert.assertNotNull(partner);
        Assert.assertEquals("VDE", partner.getName());
        Assert.assertEquals("Vision d'Espoir", partner.getFullName());

    }

    // wrapped DuplicatePartnerException
    @Test(expected = CommandException.class)
    public void testAddDuplicatePartner() throws Exception {
        PartnerDTO newPartner = new PartnerDTO();
        newPartner.setName("NRC");
        newPartner.setFullName("Norweigen Refugee Committe");

        execute(new AddPartner(1, newPartner));
    }
}