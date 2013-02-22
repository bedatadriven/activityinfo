

package org.activityinfo.server.report.generator;

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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.report.generator.PivotTableGenerator;
import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.report.content.DimensionCategory;
import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.model.AdminDimension;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.PivotTableReportElement;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PivotTableGeneratorTest {


    @Test
    public void test2x2() {

        // test input data: user

        User user = new User();
        user.setLocale("fr");


        // test input data: PivotTableElement

        PivotTableReportElement element = new PivotTableReportElement();
        AdminDimension provinceDim = new AdminDimension(1);
        element.addRowDimension(provinceDim);
        Dimension partnerDim = new Dimension(DimensionType.Partner);
        element.addColDimension(partnerDim);

        // test input data: aggregated results
        List<Bucket> buckets = new ArrayList<Bucket>();

        buckets.add(newBucket(433, category(provinceDim, 2, "Sud Kivu"), category(partnerDim, 1, "IRC")));
        buckets.add(newBucket(1032, category(provinceDim, 1, "Nord Kivu"), category(partnerDim, 2, "Solidarites")));
        buckets.add(newBucket(310, category(provinceDim, 1, "Nord Kivu"), category(partnerDim, 1, "IRC")));
        buckets.add(newBucket(926, category(provinceDim, 1, "Nord Kivu"), category(partnerDim, 3, "AVSI")));

        // collaborator : PivotDAO

        DispatcherSync dispatcher = createMock(DispatcherSync.class);
        expect(dispatcher.execute(isA(PivotSites.class)))
        	.andReturn(new PivotSites.PivotResult(buckets));
        replay(dispatcher);

        // CLASS UNDER TEST!!

        PivotTableGenerator generator = new PivotTableGenerator(dispatcher);

        generator.generate(user, element, null, null);

        Assert.assertNotNull("element content", element.getContent());

        PivotTableData data = element.getContent().getData();
        Assert.assertEquals("rows", 2, data.getRootRow().getChildCount());
        Assert.assertEquals("rows sorted", "Nord Kivu", data.getRootRow().getChildren().get(0).getLabel());
        Assert.assertEquals("cols", 3, data.getRootColumn().getChildCount());
    }

    @Test
    public void testIndicatorSortOrder() {

        // test input data: user
        User user = new User();
        user.setLocale("fr");

        // test input data: PivotTableElement
        PivotTableReportElement element = new PivotTableReportElement();
        Dimension indicatorDim = new Dimension(DimensionType.Indicator);
        element.addRowDimension(indicatorDim);

        // test input data: aggregated results
        List<Bucket> buckets = new ArrayList<Bucket>();

        buckets.add(newBucket(300, category(indicatorDim, 1, "Nb. menages", 3)));
        buckets.add(newBucket(400, category(indicatorDim, 2, "Nb. personnes", 1)));
        buckets.add(newBucket(600, category(indicatorDim, 3, "Nb. deplaces", 2)));

        // collaborator : PivotDAO
        DispatcherSync dispatcher = createMock(DispatcherSync.class);
        expect(dispatcher.execute(isA(PivotSites.class)))
        	.andReturn(new PivotSites.PivotResult(buckets));
        replay(dispatcher);
        
        // CLASS UNDER TEST!!

        PivotTableGenerator generator = new PivotTableGenerator(dispatcher);

        generator.generate(user, element, null, null);

        Assert.assertNotNull("element content", element.getContent());

        List<PivotTableData.Axis> rows = element.getContent().getData().getRootRow().getChildren();
        Assert.assertEquals(2, ((EntityCategory) rows.get(0).getCategory()).getId());
        Assert.assertEquals(3, ((EntityCategory) rows.get(1).getCategory()).getId());
        Assert.assertEquals(1, ((EntityCategory) rows.get(2).getCategory()).getId());
    }



    public class AndCategory {
        private Dimension dimension;
        private DimensionCategory category;

        public AndCategory(Dimension dimension, DimensionCategory category) {
            this.dimension = dimension;
            this.category = category;
        }

        public DimensionCategory getCategory() {
            return category;
        }

        public Dimension getDimension() {
            return dimension;
        }
    }



    public AndCategory category(Dimension dim, int id, String label) {
        return new AndCategory(dim, new EntityCategory(id, label));
    }

    public AndCategory category(Dimension dim, int id, String label, int sortOrder) {
        return new AndCategory(dim, new EntityCategory(id, label, sortOrder));
    }

    public Bucket newBucket(double value, AndCategory... pairs) {
        Bucket bucket = new Bucket(value);
        for (AndCategory pair : pairs) {
            bucket.setCategory(pair.getDimension(), pair.getCategory());
        }
        return bucket;
    }

}
