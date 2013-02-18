/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.report.content;

import java.io.Serializable;
import java.util.List;

import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.model.TableColumn;

public class TableData implements Serializable {

    private final TableColumn rootColumn;
	private final List<TableColumn> leaves;
	private final List<SiteDTO> rows;

	public TableData(TableColumn rootColumn, List<SiteDTO> rows) {
		this.rootColumn = rootColumn;
        this.leaves = rootColumn.getLeaves();
		this.rows = rows;
	}

    public String getColumnIndex(String source) {
        for(TableColumn column : leaves) {
            if(column.getProperty().equals(source)) {
                return column.getSitePropertyName();
            }
        }
        return null;
    }

    public int getLeafColumnCount() {
        return leaves.size();
    }

    public TableColumn getRootColumn() {
        return rootColumn;
    }

    public List<SiteDTO> getRows() {
		return rows;
	}

	public boolean isEmpty() {
		return rows == null || rows.size() == 0;
	}
}
