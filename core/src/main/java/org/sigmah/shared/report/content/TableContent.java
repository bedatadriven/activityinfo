/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.content;

import java.util.List;

/**
 * @author Alex Bertram 
 */
public class TableContent implements Content {

    private List<FilterDescription> filterDescriptions;
    private TableData data;

    public TableContent() {
    }

    public List<FilterDescription> getFilterDescriptions() {
        return filterDescriptions;
    }

    public void setFilterDescriptions(List<FilterDescription> filterDescriptions) {
        this.filterDescriptions = filterDescriptions;
    }

    public TableData getData() {
        return data;
    }

    public void setData(TableData data) {
        this.data = data;
    }

}
