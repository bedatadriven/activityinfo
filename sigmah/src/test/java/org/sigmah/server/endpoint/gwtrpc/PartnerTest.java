package org.sigmah.server.endpoint.gwtrpc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.shared.command.AddPartner;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.exception.DuplicateException;
import org.sigmah.test.InjectionSupport;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class PartnerTest extends CommandTestCase {


    @Test
    public void testAddPartner() throws Exception {
        PartnerDTO newPartner = new PartnerDTO();
        newPartner.setName("NRC");
        newPartner.setFullName("Norweigen Refugee Committe");

        CreateResult cr = execute(new AddPartner(2, newPartner));

        Assert.assertEquals(1, cr.getNewId());

        SchemaDTO schema = execute(new GetSchema());
        PartnerDTO partner = schema.getDatabaseById(2).getPartnerById(1);

        Assert.assertNotNull(partner);
        Assert.assertEquals("NRC", partner.getName());
    }

    @Test
    public void testAddNewPartner() throws Exception {
        PartnerDTO newPartner = new PartnerDTO();
        newPartner.setName("VDE");
        newPartner.setFullName("Vision d'Espoir");


        CreateResult cr = execute(new AddPartner(1, newPartner));


        SchemaDTO schema = execute(new GetSchema());
        PartnerDTO partner = schema.getDatabaseById(1).getPartnerById(cr.getNewId());

        Assert.assertNotNull(partner);
        Assert.assertEquals("VDE", partner.getName());
        Assert.assertEquals("Vision d'Espoir", partner.getFullName());

    }


    @Test(expected = DuplicateException.class)
    public void testAddDuplicatePartner() throws Exception {
        PartnerDTO newPartner = new PartnerDTO();
        newPartner.setName("NRC");
        newPartner.setFullName("Norweigen Refugee Committe");

        CreateResult cr = execute(new AddPartner(1, newPartner));
    }
}