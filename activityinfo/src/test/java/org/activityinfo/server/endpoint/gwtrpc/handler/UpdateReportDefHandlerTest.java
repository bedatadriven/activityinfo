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

package org.activityinfo.server.endpoint.gwtrpc.handler;

import junit.framework.Assert;
import org.activityinfo.server.endpoint.gwtrpc.CommandTestCase;
import org.activityinfo.shared.command.GetReportTemplates;
import org.activityinfo.shared.command.UpdateReportDef;
import org.activityinfo.shared.command.result.ReportTemplateResult;
import org.activityinfo.shared.dto.ReportTemplateDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.model.ReportFrequency;
import org.junit.Test;

/**
 * @author Alex Bertram
 */
public class UpdateReportDefHandlerTest extends CommandTestCase {

    @Test
    public void testUpdate() throws CommandException {

        populate("schema1");

        setUser(1);

        UpdateReportDef cmd = new UpdateReportDef();
        cmd.setId(1);
        cmd.setNewXml("<report frequency=\"Adhoc\"><title>My new title</title></report>");

        execute(cmd);

        ReportTemplateResult result = execute(new GetReportTemplates());

        ReportTemplateDTO dto = result.getData().get(0);

        Assert.assertEquals("My new title", dto.getTitle());
        Assert.assertEquals(ReportFrequency.Adhoc, dto.getFrequency());
    }
}
