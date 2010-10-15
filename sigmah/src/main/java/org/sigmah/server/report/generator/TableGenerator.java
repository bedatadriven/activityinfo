/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator;

import com.google.inject.Inject;
import org.sigmah.server.dao.PivotDAO;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dao.IndicatorDAO;
import org.sigmah.shared.dao.SiteOrder;
import org.sigmah.shared.dao.SiteTableDAO;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.report.content.TableContent;
import org.sigmah.shared.report.content.TableData;
import org.sigmah.shared.report.model.DateRange;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.TableColumn;
import org.sigmah.shared.report.model.TableElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TableGenerator extends ListGenerator<TableElement> {

    protected IndicatorDAO indicatorDAO;
    protected MapGenerator mapGenerator;

    @Inject
    public TableGenerator(PivotDAO pivotDAO, SiteTableDAO siteDAO, IndicatorDAO indicatorDAO,
                          MapGenerator mapGenerator) {
        super(pivotDAO, siteDAO);
        this.indicatorDAO = indicatorDAO;
        this.mapGenerator = mapGenerator;
    }

    @Override
    public void generate(User user, TableElement element, Filter inheritedFilter, DateRange dateRange) {
        Filter filter = resolveElementFilter(element, dateRange);
        Filter effectiveFilter = inheritedFilter == null ? filter : new Filter(inheritedFilter, filter);

        TableContent content = new TableContent();
        content.setFilterDescriptions(generateFilterDescriptions(filter, Collections.<DimensionType>emptySet(), user));

        TableData data = generateData(user, element, effectiveFilter);
        content.setData(data);

        if (element.getMap() != null) {
            mapGenerator.generate(user, element.getMap(), effectiveFilter, dateRange);

            Integer mapColIndex = data.getColumnIndex("map");
            if (mapColIndex != null) {

                Map<Integer, String> siteLabels = element.getMap().getContent().siteLabelMap();
                for (TableData.Row row : data.getRows()) {
                    row.values[mapColIndex] = siteLabels.get(row.getId());
                }
            }
        }
        element.setContent(content);
    }


    protected List<SiteOrder> resolveOrder(TableElement element) {
        List<SiteOrder> list = new ArrayList<SiteOrder>();

        for (TableColumn column : element.getSortBy()) {
            if ("admin".equals(column.getProperty())) {
                list.add(new SiteOrder(AdminLevelDTO.getPropertyName(column.getPropertyQualifyingId()),
                        !column.isOrderAscending()));

            } else if ("indicator".equals(column.getProperty())) {
                list.add(new SiteOrder(IndicatorDTO.getPropertyName(column.getPropertyQualifyingId()),
                        !column.isOrderAscending()));

            } else {
                list.add(new SiteOrder(column.getProperty(), column.isOrderAscending()));
            }
        }
        return list;
    }

    public TableData generateData(User user, TableElement element, Filter filter) {

        TableData data = new TableData(element.getRootColumn());
        List<TableData.Row> rows = siteDAO.query(
                user,
                filter,
                resolveOrder(element),
                new RowBinder(data),
                SiteTableDAO.RETRIEVE_ALL, 0, -1);

        data.setRows(rows);

        return data;
    }


}
