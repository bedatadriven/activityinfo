/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.generator;

import java.util.Collections;
import java.util.Map;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.content.TableContent;
import org.activityinfo.shared.report.content.TableData;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.TableColumn;
import org.activityinfo.shared.report.model.TableElement;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.inject.Inject;

public class TableGenerator extends ListGenerator<TableElement> {

    private MapGenerator mapGenerator;
    
    @Inject
    public TableGenerator(DispatcherSync dispatcher, MapGenerator mapGenerator) {
        super(dispatcher);
        this.mapGenerator = mapGenerator;
    }

    @Override
    public void generate(User user, TableElement element, Filter inheritedFilter, DateRange dateRange) {
        Filter filter = GeneratorUtils.resolveElementFilter(element, dateRange);
        Filter effectiveFilter = inheritedFilter == null ? filter : new Filter(inheritedFilter, filter);

        TableContent content = new TableContent();
        content.setFilterDescriptions(generateFilterDescriptions(filter, Collections.<DimensionType>emptySet(), user));

        TableData data = generateData(element, effectiveFilter);
        content.setData(data);

        if (element.getMap() != null) {
            mapGenerator.generate(user, element.getMap(), effectiveFilter, dateRange);

            Map<Integer, String> siteLabels = element.getMap().getContent().siteLabelMap();
            for (SiteDTO row : data.getRows()) {
                row.set("map", siteLabels.get(row.getId()));
            }
        }
        element.setContent(content);
    }


    public TableData generateData(TableElement element, Filter filter) {
    	GetSites query = new GetSites(filter);
    	
    	if(!element.getSortBy().isEmpty()) {
    		TableColumn sortBy = element.getSortBy().get(0);
    		query.setSortInfo(new SortInfo(sortBy.getSitePropertyName(), sortBy.isOrderAscending() ? SortDir.ASC : SortDir.DESC));
    	}
    	
		SiteResult sites = getDispatcher().execute(query);    	
        return new TableData(element.getRootColumn(), sites.getData());
    }
}
