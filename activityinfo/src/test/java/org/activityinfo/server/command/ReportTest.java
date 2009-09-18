package org.activityinfo.server.command;

import junit.framework.Assert;

import org.activityinfo.server.command.CommandTestCase;
import org.activityinfo.shared.command.GetReportTemplates;
import org.activityinfo.shared.command.UpdateSubscription;
import org.activityinfo.shared.command.result.ReportTemplateResult;
import org.activityinfo.shared.dto.ReportParameterDTO;
import org.activityinfo.shared.dto.ReportTemplateDTO;
import org.activityinfo.shared.exception.CommandException;
import org.junit.BeforeClass;
import org.junit.Test;

import com.extjs.gxt.ui.client.data.PagingLoadResult;

public class ReportTest extends CommandTestCase {


	@Test
	public void testReport() throws CommandException {


		populate("schema1");

		setUser(1); // Alex
		

		ReportTemplateResult reports = execute(new GetReportTemplates());
		
		Assert.assertEquals("records returned", 1, reports.getData().size());
		
		ReportTemplateDTO report =  reports.getData().get(0);
		
		Assert.assertEquals("report title", "Report 1", report.getTitle());
		Assert.assertEquals("owner", "Alex", report.getOwnerName());
		Assert.assertEquals("parameter count", 1, report.getParameters().size());
        Assert.assertEquals("subscription", 1, report.getSubscriptionFrequency());
        Assert.assertEquals("subscription day", 5, report.getSubscriptionDay());
		
		ReportParameterDTO param = report.getParameters().get(0);
		Assert.assertEquals("name", "MONTH", param.getName());
		Assert.assertEquals("label", "Mois", param.getLabel());
		Assert.assertEquals("type", ReportParameterDTO.TYPE_DATE, param.getType());
		Assert.assertEquals("dateUnit", ReportParameterDTO.UNIT_MONTH, param.getDateUnit());
	}


    @Test
    public void testReportNoSubscription() throws CommandException {

        populate("schema1");


        setUser(2); // Bavon

        ReportTemplateResult reports = execute(new GetReportTemplates());

        Assert.assertEquals("records returned", 1, reports.getData().size());

        ReportTemplateDTO report =  reports.getData().get(0);

        Assert.assertEquals("subscription", 0, report.getSubscriptionFrequency());
        Assert.assertEquals("subscription day", 0, report.getSubscriptionDay());
	
    }

    @Test
    public void testNewSubscription() throws CommandException {

        populate("schema1");

        setUser(2);

        // update
        execute(new UpdateSubscription(1, 2, 15));

        // check for change

        ReportTemplateResult reports = execute(new GetReportTemplates());

        Assert.assertEquals("records returned", 1, reports.getData().size());
        Assert.assertEquals("subscription frequency", 2, reports.getData().get(0).getSubscriptionFrequency());
        Assert.assertEquals("day", 15, reports.getData().get(0).getSubscriptionDay());

    }


    @Test
    public void testUpdateSubscription() throws CommandException {

        populate("schema1");

        setUser(1);

        // update
        execute(new UpdateSubscription(1, 3, 6));

        // check for change

        ReportTemplateResult reports = execute(new GetReportTemplates());

        Assert.assertEquals("records returned", 1, reports.getData().size());
        Assert.assertEquals("frequency", 3, reports.getData().get(0).getSubscriptionFrequency());
        Assert.assertEquals("day", 6, reports.getData().get(0).getSubscriptionDay());

    }

}
