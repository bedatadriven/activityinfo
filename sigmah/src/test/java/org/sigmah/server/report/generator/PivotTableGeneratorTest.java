/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.sigmah.server.dao.PivotDAO;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.report.content.DimensionCategory;
import org.sigmah.shared.report.content.EntityCategory;
import org.sigmah.shared.report.content.PivotTableData;
import org.sigmah.shared.report.model.AdminDimension;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.PivotTableElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.easymock.EasyMock.*;

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

        PivotTableElement element = new PivotTableElement();
        AdminDimension provinceDim = new AdminDimension(1);
        element.addRowDimension(provinceDim);
        Dimension partnerDim = new Dimension(DimensionType.Partner);
        element.addColDimension(partnerDim);

        // test input data: aggregated results
        List<PivotDAO.Bucket> buckets = new ArrayList<PivotDAO.Bucket>();

        buckets.add(newBucket(433, category(provinceDim, 2, "Sud Kivu"), category(partnerDim, 1, "IRC")));
        buckets.add(newBucket(1032, category(provinceDim, 1, "Nord Kivu"), category(partnerDim, 2, "Solidarites")));
        buckets.add(newBucket(310, category(provinceDim, 1, "Nord Kivu"), category(partnerDim, 1, "IRC")));
        buckets.add(newBucket(926, category(provinceDim, 1, "Nord Kivu"), category(partnerDim, 3, "AVSI")));

        // collaborator : PivotDAO

        PivotDAO dao = createMock(PivotDAO.class);
        expect(dao.aggregate(anyInt(), isA(Filter.class), EasyMock.<Set<Dimension>>anyObject())).andReturn(buckets);
        replay(dao);

        // CLASS UNDER TEST!!

        PivotTableGenerator generator = new PivotTableGenerator(dao);

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
        PivotTableElement element = new PivotTableElement();
        Dimension indicatorDim = new Dimension(DimensionType.Indicator);
        element.addRowDimension(indicatorDim);

        // test input data: aggregated results
        List<PivotDAO.Bucket> buckets = new ArrayList<PivotDAO.Bucket>();

        buckets.add(newBucket(300, category(indicatorDim, 1, "Nb. menages", 3)));
        buckets.add(newBucket(400, category(indicatorDim, 2, "Nb. personnes", 1)));
        buckets.add(newBucket(600, category(indicatorDim, 3, "Nb. deplaces", 2)));

        // collaborator : PivotDAO
        PivotDAO dao = createMock(PivotDAO.class);
        expect(dao.aggregate(anyInt(), isA(Filter.class), EasyMock.<Set<Dimension>>anyObject())).andReturn(buckets);
        replay(dao);

        // CLASS UNDER TEST!!

        PivotTableGenerator generator = new PivotTableGenerator(dao);

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

    public PivotDAO.Bucket newBucket(double value, AndCategory... pairs) {
        PivotDAO.Bucket bucket = new PivotDAO.Bucket();
        bucket.setDoubleValue(value);
        for (AndCategory pair : pairs) {
            bucket.setCategory(pair.getDimension(), pair.getCategory());
        }
        return bucket;
    }

}
