package org.activityinfo.shared.report.content;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;


import org.activityinfo.shared.report.model.TableElement;
import org.activityinfo.shared.report.model.TableElement.Column;

public class TableData implements Serializable {

    private Column rootColumn;
    private int leafColumnCount;

	/**
	 * Maps leaf-columns to their slot in the <code>Row.values</code> array.
	 */
	private Map<Column, Integer> columnMap;
	private List<Row> rows;


	public TableData(Column rootColumn) {
		this.rootColumn = rootColumn;

        List<Column> leaves = rootColumn.getLeaves();
        leafColumnCount = leaves.size();


		columnMap = new HashMap<Column, Integer>();

		for(Column leaf : leaves) {
			columnMap.put(leaf, columnMap.size());
		}
	}

    public Integer getColumnIndex(Column column) {
		return columnMap.get(column);
	}

    public int getLeafColumnCount() {
        return leafColumnCount;
    }

    public Column getRootColumn() {
        return rootColumn;
    }

    public void setRows(List<Row> rows) {
		this.rows = rows;
	}


	public List<Row> getRows() {
		return rows;
	}

	public boolean isEmpty() {
		return rows == null || rows.size() == 0;
	}


     /**
     * Data structure that stores the data for each
     * row in the table.
     *
     */
    public static class Row implements SiteGeoData, Serializable {

        private Row() {

        }

        public Row(int leafColumnCount) {
            values = new Object[leafColumnCount];
        }

        public int id;

        public Object[] values;
        public boolean hasXY;
        public double x;
        public double y;

        private Marker<TableData.Row> marker;

        public void setMarker(Marker marker) {
            this.marker = marker;
        }

        public Marker getMarker() {
            return this.marker;
        }

        @Override
        public double getLatitude() {
            return y;
        }
        @Override
        public double getLongitude() {
            return x;
        }
        @Override
        public boolean hasLatLong() {
            return hasXY;
        }
    }
}
