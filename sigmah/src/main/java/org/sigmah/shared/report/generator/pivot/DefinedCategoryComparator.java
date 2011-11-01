package org.sigmah.shared.report.generator.pivot;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.shared.report.content.DimensionCategory;
import org.sigmah.shared.report.content.PivotTableData;

public class DefinedCategoryComparator implements Comparator<PivotTableData.Axis> {
    private final Map<DimensionCategory, Integer> orderMap;

    public DefinedCategoryComparator(List<DimensionCategory> order) {
        orderMap = new HashMap<DimensionCategory, Integer>();

        for (int i = 0; i != order.size(); ++i) {
            orderMap.put(order.get(i), i);
        }

    }

    @Override
    public int compare(PivotTableData.Axis a1, PivotTableData.Axis a2) {
        Integer o1 = orderMap.get(a1.getCategory());
        Integer o2 = orderMap.get(a2.getCategory());

        if (o1 == null) {
            o1 = Integer.MAX_VALUE;
        }
        if (o2 == null) {
            o2 = Integer.MAX_VALUE;
        }

        return o1.compareTo(o2);
    }
}