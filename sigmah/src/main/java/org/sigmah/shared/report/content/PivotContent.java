/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.content;

import org.sigmah.shared.dao.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PivotContent implements Content {

    public PivotContent() {

    }
    private Filter effectiveFilter;
    private List<FilterDescription> filterDescriptions;
    private PivotTableData data;

    public PivotContent(PivotTableData data, ArrayList<FilterDescription> filterDescriptions) {
        this.data = data;
        this.filterDescriptions = filterDescriptions;
    }

    public List<FilterDescription> getFilterDescriptions() {
        return filterDescriptions;
    }

    public void setFilterDescriptions(List<FilterDescription> filterDescriptions) {
        this.filterDescriptions = filterDescriptions;
    }

    public PivotTableData getData() {
        return data;
    }

    public void setData(PivotTableData data) {
        this.data = data;
    }

    public Filter getEffectiveFilter() {
        return effectiveFilter;
    }

    public void setEffectiveFilter(Filter effectiveFilter) {
        this.effectiveFilter = effectiveFilter;
    }
}
