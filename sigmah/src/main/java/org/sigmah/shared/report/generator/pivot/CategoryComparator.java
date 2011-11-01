package org.sigmah.shared.report.generator.pivot;

import java.util.Comparator;

import org.sigmah.shared.report.content.PivotTableData;

public class CategoryComparator implements Comparator<PivotTableData.Axis> {

    @Override
    public int compare(PivotTableData.Axis a1, PivotTableData.Axis a2) {
        Comparable c1 = sortKey(a1);
        Comparable c2 = sortKey(a2);

        if (c1 == null && c2 == null) {
            return 0;
        }
        if (c1 == null) {
            return -1;
        }
        if (c2 == null) {
            return 1;
        }


        return c1.compareTo(c2);
    }

	private Comparable sortKey(PivotTableData.Axis a1) {
		if(a1.getCategory() == null) {
			return null;
		} else {
			return a1.getCategory().getSortKey();
		}
	}
}