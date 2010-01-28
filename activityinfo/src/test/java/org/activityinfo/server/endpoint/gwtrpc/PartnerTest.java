package org.activityinfo.server.endpoint.gwtrpc;

import org.activityinfo.server.dao.OnDataSet;
import org.activityinfo.shared.command.AddPartner;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.PartnerModel;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.exception.DuplicateException;
import org.activityinfo.test.InjectionSupport;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class PartnerTest extends CommandTestCase {


    @Test
    public void testAddPartner() throws Exception {
        PartnerModel newPartner = new PartnerModel();
        newPartner.setName("NRC");
        newPartner.setFullName("Norweigen Refugee Committe");

        CreateResult cr = execute(new AddPartner(2, newPartner));

        Assert.assertEquals(1, cr.getNewId());

        Schema schema = execute(new GetSchema());
        PartnerModel partner = schema.getDatabaseById(2).getPartnerById(1);

        Assert.assertNotNull(partner);
        Assert.assertEquals("NRC", partner.getName());
    }

    @Test
    public void testAddNewPartner() throws Exception {
        PartnerModel newPartner = new PartnerModel();
        newPartner.setName("VDE");
        newPartner.setFullName("Vision d'Espoir");


        CreateResult cr = execute(new AddPartner(1, newPartner));


        Schema schema = execute(new GetSchema());
        PartnerModel partner = schema.getDatabaseById(1).getPartnerById(cr.getNewId());

        Assert.assertNotNull(partner);
        Assert.assertEquals("VDE", partner.getName());
        Assert.assertEquals("Vision d'Espoir", partner.getFullName());

    }


    @Test(expected = DuplicateException.class)
    public void testAddDuplicatePartner() throws Exception {
        PartnerModel newPartner = new PartnerModel();
        newPartner.setName("NRC");
        newPartner.setFullName("Norweigen Refugee Committe");

        CreateResult cr = execute(new AddPartner(1, newPartner));
    }
}