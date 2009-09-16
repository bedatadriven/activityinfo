package org.activityinfo.server.report.generator;

import java.util.*;

import org.activityinfo.server.dao.SchemaDAO;
import org.activityinfo.server.dao.hibernate.*;
import org.activityinfo.server.domain.Indicator;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.report.content.TableContent;
import org.activityinfo.shared.report.content.TableData;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.Filter;
import org.activityinfo.shared.report.model.TableElement;
import org.activityinfo.shared.report.model.TableElement.Column;
import org.hibernate.criterion.Order;

import com.google.inject.Inject;

public class TableGenerator extends ListGenerator<TableElement> {

	protected SchemaDAO schemaDAO;

    @Inject
	public TableGenerator(PivotDAO pivotDAO, SiteTableDAO siteDAO, SchemaDAO schemaDAO ) {
		super(pivotDAO, siteDAO);

		this.schemaDAO = schemaDAO;
	}

    @Override
    public void generate(User user, TableElement element, Filter inheritedFilter, Map<String, Object> parameterValues) {

        Filter filter = ParamFilterHelper.resolve(element.getFilter(), parameterValues);
        Filter effectiveFilter = inheritedFilter == null ? filter : new Filter(inheritedFilter, filter);

        TableContent content = new TableContent();
        content.setFilterDescriptions(generateFilterDescriptions(filter, Collections.<DimensionType>emptySet()));
        content.setData(generateData(user, element, effectiveFilter));


    }

    protected List<Order> resolveOrder(TableElement element) {
		List<Order> list = new ArrayList<Order>();

		for(Column column : element.getSortBy()) {
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

		for(Column column : element.getRootColumn().getLeaves()) {

			postProcessColumn(data, column);

		}


		return data;
	}

	protected void postProcessColumn(TableData table, Column column) {

		if("status".equals(column.getProperty())) {

			Map<Integer, String> labels = new HashMap<Integer, String>(4);
			labels.put(-2, "Planifie");
			labels.put(-1, "En cours");
			labels.put(0, "Annule");
			labels.put(1, "Complet");

			int valueIndex = table.getColumnIndex(column);

			for(TableData.Row row : table.getRows()) {
				row.values[valueIndex] = 
					labels.get(row.values[valueIndex]);
			}
		}
	}



}
