package org.activityinfo.server.report.generator;

import com.google.inject.Inject;
import org.activityinfo.server.dao.SchemaDAO;
import org.activityinfo.server.dao.hibernate.PivotDAO;
import org.activityinfo.server.dao.hibernate.SiteAdminOrder;
import org.activityinfo.server.dao.hibernate.SiteIndicatorOrder;
import org.activityinfo.server.dao.hibernate.SiteTableDAO;
import org.activityinfo.server.domain.Indicator;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.report.content.TableContent;
import org.activityinfo.shared.report.content.TableData;
import org.activityinfo.shared.report.model.*;
import org.hibernate.criterion.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TableGenerator extends ListGenerator<TableElement> {

	protected SchemaDAO schemaDAO;
    protected MapGenerator mapGenerator;

    @Inject
	public TableGenerator(PivotDAO pivotDAO, SiteTableDAO siteDAO, SchemaDAO schemaDAO,
                       MapGenerator mapGenerator) {
		super(pivotDAO, siteDAO);
		this.schemaDAO = schemaDAO;
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

        if(element.getMap() != null) {     
            mapGenerator.generate(user, element.getMap(), effectiveFilter, dateRange);

            Integer mapColIndex = data.getColumnIndex("map");
            if(mapColIndex != null) {

                Map<Integer, String> siteLabels = element.getMap().getContent().siteLabelMap();
                for(TableData.Row row : data.getRows()) {
                    row.values[mapColIndex] = siteLabels.get(row.getId());
                }
            }
        }
        element.setContent(content);
    }


    protected List<Order> resolveOrder(TableElement element) {
		List<Order> list = new ArrayList<Order>();

		for(TableColumn column : element.getSortBy()) {
            if("admin".equals(column.getProperty())) {
                list.add( new SiteAdminOrder(column.getPropertyQualifyingId(), column.isOrderAscending()));

            } else if("indicator".equals(column.getProperty())) {
				Indicator indicator = schemaDAO.findById(Indicator.class, column.getPropertyQualifyingId());
				list.add( new SiteIndicatorOrder(indicator, column.isOrderAscending()));

            } else {
            	if(column.isOrderAscending()) {
					list.add(Order.asc( column.getProperty() ));
				} else {
					list.add(Order.desc( column.getProperty() ));
				}
			}
		}
		return list;
	}

	public TableData generateData(User user, TableElement element, Filter filter) {

		TableData data = new TableData(element.getRootColumn());
        List<TableData.Row> rows = siteDAO.query(
                user,
                FilterCriterionBridge.resolveCriterion(filter),
                resolveOrder(element),
                new RowBinder(data),
                SiteTableDAO.RETRIEVE_ALL, 0, -1);

        data.setRows(rows);

		return data;
	}




}
