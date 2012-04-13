/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator;

import java.util.Collections;

import org.sigmah.server.command.DispatcherSync;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.report.content.Content;
import org.sigmah.shared.report.content.ReportContent;
import org.sigmah.shared.report.model.DateRange;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportElement;
import org.sigmah.shared.report.model.TableElement;

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
