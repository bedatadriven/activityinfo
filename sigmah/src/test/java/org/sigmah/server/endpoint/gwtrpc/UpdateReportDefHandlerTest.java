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
