

package org.activityinfo.server.report.generator;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Collections;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.report.content.Content;
import org.activityinfo.shared.report.content.NullContent;
import org.activityinfo.shared.report.content.ReportContent;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.PivotChartReportElement;
import org.activityinfo.shared.report.model.PivotTableReportElement;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.report.model.ReportElement;
import org.activityinfo.shared.report.model.TableElement;
import org.activityinfo.shared.report.model.TextReportElement;

import com.google.inject.Inject;

public class ReportGenerator extends BaseGenerator<Report> {

    private final PivotTableGenerator pivotTableGenerator;
    private final PivotChartGenerator pivotChartGenerator;
    private final TableGenerator tableGenerator;
    private final MapGenerator mapGenerator;


    @Inject
    public ReportGenerator(DispatcherSync dispatcher,
                           PivotTableGenerator pivotTableGenerator,
                           PivotChartGenerator pivotChartGenerator,
                           TableGenerator tableGenerator, MapGenerator mapGenerator) {
        super(dispatcher);

        this.pivotTableGenerator = pivotTableGenerator;
        this.pivotChartGenerator = pivotChartGenerator;
        this.tableGenerator = tableGenerator;
        this.mapGenerator = mapGenerator;
    }

    public Content generateElement(User user, ReportElement element, Filter inheritedFilter,
                                   DateRange dateRange) {
        if (element instanceof PivotChartReportElement) {
            pivotChartGenerator.generate(user, (PivotChartReportElement) element, inheritedFilter, dateRange);
            return element.getContent();

        } else if (element instanceof PivotTableReportElement) {
            pivotTableGenerator.generate(user, (PivotTableReportElement) element, inheritedFilter, dateRange);
            return element.getContent();

        } else if (element instanceof MapReportElement) {
            mapGenerator.generate(user, (MapReportElement) element, inheritedFilter, dateRange);
            return element.getContent();

        } else if (element instanceof TableElement) {
            tableGenerator.generate(user, ((TableElement) element), inheritedFilter, dateRange);
            return element.getContent();
           
        } else if(element instanceof Report) {
        	generateReport(user, (Report) element, inheritedFilter, dateRange);
        	return element.getContent();
        
        } else if(element instanceof TextReportElement) {
        	return new NullContent();
        	
        } else {
            throw new RuntimeException("Unknown element type " + element.getClass().getName());
        }
    }

    @Override
    public void generate(User user, Report report, Filter inheritedFilter, DateRange dateRange) {
        generateReport(user, report, inheritedFilter, dateRange);
    }

	private void generateReport(User user, Report report,
			Filter inheritedFilter, DateRange dateRange) {
		Filter filter = GeneratorUtils.resolveElementFilter(report, dateRange);
        Filter effectiveFilter = GeneratorUtils.resolveEffectiveFilter(report, inheritedFilter, dateRange);

        for (ReportElement element : report.getElements()) {
            generateElement(user, element, effectiveFilter, dateRange);
        }

        ReportContent content = new ReportContent();
        content.setFileName(generateFileName(report, dateRange, user));
        content.setFilterDescriptions(generateFilterDescriptions(effectiveFilter,
                Collections.<DimensionType>emptySet(), user));

        report.setContent(content);
	}

    public String generateFileName(Report report, DateRange dateRange, User user) {

        StringBuilder name = new StringBuilder();

        if (report.getFileName() != null) {
            name.append(resolveTemplate(report.getFileName(),
                    dateRange, user));
        } else if (report.getTitle() != null) {
            name.append(report.getTitle());
        } else {
            name.append("Report");   // TODO: i18n
        }
        return name.toString();
    }


}
