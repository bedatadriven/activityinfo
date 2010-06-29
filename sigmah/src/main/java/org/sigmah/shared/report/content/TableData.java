package org.sigmah.shared.report.content;

import org.sigmah.shared.report.model.TableColumn;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableData implements Serializable {

    private TableColumn rootColumn;
    private int leafColumnCount;

	/**
	 * Maps leaf-columns to their slot in the <code>Row.values</code> array.
	 */
	private Map<TableColumn, Integer> columnMap;
	private List<Row> rows;



	public TableData(TableColumn rootColumn) {
		this.rootColumn = rootColumn;
        List<TableColumn> leaves = rootColumn.getLeaves();
        leafColumnCount = leaves.size();

		columnMap = new HashMap<TableColumn, Integer>();

		for(TableColumn leaf : leaves) {
			columnMap.put(leaf, columnMap.size());
		}
	}

    public Integer getColumnIndex(TableColumn column) {
		return columnMap.get(column);
	}

    public Integer getColumnIndex(String source) {
        for(Map.Entry<TableColumn, Integer> entry : columnMap.entrySet()) {
            if(entry.getKey().getProperty().equals(source)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public int getLeafColumnCount() {
        return leafColumnCount;
    }

    public TableColumn getRootColumn() {
        return rootColumn;
    }

    public void setRows(List<Row> rows) {
		this.rows = rows;
	}

    public Map<TableColumn, Integer> getColumnMap() {
        return columnMap;
    }

    public void setColumnMap(Map<TableColumn, Integer> columnMap) {
        this.columnMap = columnMap;
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

         public int getId() {
             return id;
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
