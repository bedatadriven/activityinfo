package org.activityinfo.server.report.generator;

import org.activityinfo.server.dao.SiteProjectionBinder;
import org.activityinfo.server.dao.hibernate.SiteTableDAOHibernate;
import org.activityinfo.server.domain.AdminEntity;
import org.activityinfo.shared.domain.SiteColumn;
import org.activityinfo.shared.report.content.TableData;
import org.activityinfo.shared.report.model.TableColumn;

import java.util.HashMap;
import java.util.Map;

/**
 * The RowBinder class bridges the SiteDAO object (which
 * serves to retrieve data at many points throughout ActivityInfo)
 * with the particular data model of report TableElement.
 *
 * @author Alex Bertram
 *
 */

public class RowBinder implements SiteProjectionBinder<TableData.Row> {

    /**
     * Map between the array provided in the SiteProjectionBinder.newInstance
     * call and the row data array
     *
     * The key is the symbol in the <code>SiteProjectionBinder.newInstance</code<
     * <code>values</code> array
     * (corresponding to the position within the SiteDAO.Column enum)
     *
     * The value is the corresponding symbol in the <code>Row.values</code> array
     */
    private Map<Integer, Integer> baseColumns = new HashMap<Integer, Integer>();

    /**
     * Map between admin level id's  and position in the <code>Row.values</code> array
     */
    private Map<Integer, Integer> adminColumns = new HashMap<Integer, Integer>();

    /**
     * Map between indicator id's and the position in the <code>Row.values</code> array
     */
    private Map<Integer, Integer> indicatorColumns = new HashMap<Integer, Integer>();

    /**
     * Map between attribute id's and the position in the <code>Row.values</code> array
     */
    private Map<Integer, Integer> attributeColumns = new HashMap<Integer, Integer>();


    private int xIndex;
    private int yIndex;
    private TableData tableData;

    public RowBinder(TableData tableData) {
        this.tableData = tableData;

        xIndex = SiteTableDAOHibernate.getColumnIndex(SiteColumn.x);
        yIndex = SiteTableDAOHibernate.getColumnIndex(SiteColumn.y);

        for(TableColumn column : tableData.getRootColumn().getLeaves()) {
            int columnIndex = tableData.getColumnIndex(column);
            if(column.getProperty().equals("admin")) {
                adminColumns.put(column.getPropertyQualifyingId(), columnIndex);

            } else if(column.getProperty().equals("indicator")) {
                 indicatorColumns.put(column.getPropertyQualifyingId(), columnIndex);

            } else if(column.getProperty().equals("attribute")) {
                attributeColumns.put(column.getPropertyQualifyingId(), columnIndex);

            } else if(column.getProperty().equals("map")) {

            } else {
                baseColumns.put(findSiteColumn(column.getProperty()).index(), columnIndex);
            }
        }
    }

    private SiteColumn findSiteColumn(String property) {
        for(SiteColumn column : SiteColumn.values()) {
            if(column.property().equals(property)) {
                return column;
            }
        }
        throw new IllegalArgumentException("Cannot match property " + property);
    }

    @Override
    public TableData.Row newInstance(String[] properties, Object[] values) {

        TableData.Row row = new TableData.Row(tableData.getLeafColumnCount());
        row.id = (Integer)values[0];

        if(values[xIndex]!=null && values[yIndex]!=null) {
            row.hasXY = true;
            row.x = (Double)values[xIndex];
            row.y = (Double)values[yIndex];
        }

        for(Map.Entry<Integer, Integer> entry : baseColumns.entrySet()) {
            row.values[entry.getValue()] = values[entry.getKey()];
        }

        return row;
    }

    @Override
    public void addIndicatorValue(TableData.Row row, int indicatorId,
                                  int aggregationMethod, double value) {

        Integer index = indicatorColumns.get(indicatorId);

        if(index != null) {
            row.values[index] = value;
        }

    }

    @Override
    public void setAdminEntity(TableData.Row row, AdminEntity entity) {

        Integer index = adminColumns.get(entity.getLevel().getId());

        if(index != null) {
            row.values[index] = entity.getName();
        }

    }

    @Override
    public void setAttributeValue(TableData.Row row, int attributeId, boolean value) {

        Integer index = attributeColumns.get(attributeId);

        if(index != null) {
            row.values[index] = value;
        }
    }
}
