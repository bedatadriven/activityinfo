package org.activityinfo.client.report.editor.pivotTable;

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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.activityinfo.client.MockEventBus;
import org.activityinfo.client.dispatch.DispatcherStub;
import org.activityinfo.client.page.report.ReportChangeEvent;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.report.model.AttributeGroupDimension;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.PivotTableReportElement;
import org.junit.Before;
import org.junit.Test;

public class DimensionPrunerTest {

	private static final int FUNDING_GROUP_ID = 102;
	private static final int NB_MENAGES_INDICATOR_ID = 101;
	private SchemaDTO schema;
	private DispatcherStub dispatcher = new DispatcherStub();
	private MockEventBus eventBus = new MockEventBus();

	@Before
	public void setupData() {
		
		ActivityDTO dist = new ActivityDTO(1, "Distribution");
		IndicatorDTO nbMenages = new IndicatorDTO();
		nbMenages.setId(NB_MENAGES_INDICATOR_ID);
		nbMenages.setName("Nb Menages");
		dist.getIndicators().add(nbMenages);
		
		AttributeGroupDTO funding = new AttributeGroupDTO(FUNDING_GROUP_ID);
		funding.setName("Funding Source");
		dist.getAttributeGroups().add(funding);
		
		UserDatabaseDTO nfi = new UserDatabaseDTO(1, "NFI");
		nfi.getActivities().add(dist);
		
		this.schema = new SchemaDTO();
		schema.getDatabases().add(nfi);
		
		dispatcher.setResult(GetSchema.class, schema);
	}
	
	@Test
	public void test() {
		
		PivotTableReportElement table = new PivotTableReportElement();
		
		DimensionPruner pruner = new DimensionPruner(eventBus, dispatcher);
		pruner.bind(table);
		
		table.getFilter().addRestriction(DimensionType.Indicator, NB_MENAGES_INDICATOR_ID);		
		table.addColDimension(new Dimension(DimensionType.Indicator));
		eventBus.fireEvent(new ReportChangeEvent(this, table));
		
		AttributeGroupDimension groupDim = new AttributeGroupDimension(FUNDING_GROUP_ID);
		table.addColDimension(groupDim);
		eventBus.fireEvent(new ReportChangeEvent(this, table));
		
		assertTrue(table.getColumnDimensions().contains(groupDim));
		
		// now remove the indicator and verify that the attribute group has been removed
		table.getFilter().clearRestrictions(DimensionType.Indicator);
		eventBus.fireEvent(new ReportChangeEvent(this, table));
		
		assertFalse(table.getColumnDimensions().contains(groupDim));	
	}
}

