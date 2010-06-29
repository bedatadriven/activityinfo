/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.shared.command.GetReportTemplates;
import org.sigmah.shared.command.UpdateReportDef;
import org.sigmah.shared.command.result.ReportTemplateResult;
import org.sigmah.shared.dto.ReportDefinitionDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.report.model.ReportFrequency;
import org.sigmah.test.InjectionSupport;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/schema1.db.xml")
public class UpdateReportDefHandlerTest extends CommandTestCase {

    @Test
    public void testUpdate() throws CommandException {

        UpdateReportDef cmd = new UpdateReportDef();
        cmd.setId(1);
        cmd.setNewXml("<report frequency=\"Adhoc\"><title>My new title</title></report>");

        execute(cmd);

        ReportTemplateResult result = execute(new GetReportTemplates());

        ReportDefinitionDTO dto = result.getData().get(0);

        Assert.assertEquals("My new title", dto.getTitle());
        Assert.assertEquals(ReportFrequency.Adhoc, dto.getFrequency());
    }
}
