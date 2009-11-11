package org.activityinfo.server.report.generator;

import com.google.inject.Inject;
import org.activityinfo.server.dao.hibernate.PivotDAO;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.report.content.Content;
import org.activityinfo.shared.report.content.ReportContent;
import org.activityinfo.shared.report.model.*;

import java.util.Collections;

public class ReportGenerator extends BaseGenerator<Report> {

    private final PivotTableGenerator pivotTableGenerator;
    private final PivotChartGenerator pivotChartGenerator;
    private final TableGenerator tableGenerator;
    private final MapGenerator mapGenerator;


    @Inject
    public ReportGenerator(PivotDAO pivotDAO,
                           PivotTableGenerator pivotTableGenerator,
                           PivotChartGenerator pivotChartGenerator,
                           TableGenerator tableGenerator, MapGenerator mapGenerator) {
        super(pivotDAO);

        this.pivotTableGenerator = pivotTableGenerator;
        this.pivotChartGenerator = pivotChartGenerator;
        this.tableGenerator = tableGenerator;
        this.mapGenerator = mapGenerator;
    }

    public Content generateElement(User user, ReportElement element, Filter inheritedFilter,
                                   DateRange dateRange) {
        if(element instanceof PivotChartElement) {
            pivotChartGenerator.generate(user, (PivotChartElement) element, inheritedFilter, dateRange);
            return ((PivotChartElement)element).getContent();

        } else if(element instanceof PivotTableElement) {
            pivotTableGenerator.generate(user, (PivotTableElement) element, inheritedFilter, dateRange);
            return ((PivotTableElement) element).getContent();

        } else if(element instanceof MapElement) {
            mapGenerator.generate(user, (MapElement) element, inheritedFilter,dateRange);
            return ((MapElement)element).getContent();

        } else if(element instanceof TableElement) {
            tableGenerator.generate(user, ((TableElement)element), inheritedFilter, dateRange);
            return ((TableElement)element).getContent();

        } else {
            throw new RuntimeException("Unknown element type " + element.getClass().getName());
        }
    }

    @Override
    public void generate(User user, Report report, Filter inheritedFilter, DateRange dateRange) {

        Filter filter = resolveElementFilter(report, dateRange);
        Filter effectiveFilter = resolveEffectiveFilter(report, inheritedFilter, dateRange);

        for(ReportElement element : report.getElements()) {

            generateElement(user, element, effectiveFilter, dateRange);

        }

        ReportContent content = new ReportContent();
        content.setFileName(generateFileName(report, dateRange, user));
        content.setFilterDescriptions(generateFilterDescriptions(effectiveFilter,
                Collections.<DimensionType>emptySet(),user));

        report.setContent(content);

    }

    public String generateFileName(Report report, DateRange dateRange, User user) {

        StringBuilder name = new StringBuilder();

        if(report.getFileName() != null) {
            name.append(resolveTemplate(report.getFileName(),
                     dateRange, user));
        } else if(report.getTitle() != null) {
            name.append(report.getTitle());
        } else {
            name.append("Report");   // TODO: i18n
        }
        return name.toString();
    }


}
