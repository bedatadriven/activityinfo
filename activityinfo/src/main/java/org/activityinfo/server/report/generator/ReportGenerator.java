package org.activityinfo.server.report.generator;

import org.activityinfo.server.dao.hibernate.PivotDAO;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.report.content.Content;
import org.activityinfo.shared.report.content.ReportContent;
import org.activityinfo.shared.report.model.*;

import com.google.inject.Inject;

import java.util.Map;
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
                                   Map<String,Object> parameterValues) {
        if(element instanceof PivotChartElement) {
            pivotChartGenerator.generate(user, (PivotChartElement) element, inheritedFilter, parameterValues);
            return ((PivotChartElement)element).getContent();

        } else if(element instanceof PivotTableElement) {
            pivotTableGenerator.generate(user, (PivotTableElement) element, inheritedFilter, parameterValues);
            return ((PivotTableElement) element).getContent();

        } else if(element instanceof MapElement) {
            mapGenerator.generate(user, (MapElement) element, inheritedFilter,parameterValues);
            return ((MapElement)element).getContent();

        } else if(element instanceof TableElement) {
            tableGenerator.generate(user, ((TableElement)element), inheritedFilter, parameterValues);
            return ((TableElement)element).getContent();

        } else {
            throw new RuntimeException("Unknown element type " + element.getClass().getName());
        }
    }

    @Override
    public void generate(User user, Report report, Filter inheritedFilter, Map<String, Object> parameterValues) {

        Filter filter = ParamFilterHelper.resolve(report.getFilter(), parameterValues);
        Filter effectiveFilter = inheritedFilter == null ? filter : new Filter(inheritedFilter, filter);

        for(ReportElement element : report.getElements()) {

            generateElement(user, element, effectiveFilter, parameterValues);

        }

        ReportContent content = new ReportContent();
        content.setFileName(generateFileName(report, parameterValues));
        content.setFilterDescriptions(generateFilterDescriptions(effectiveFilter,
                Collections.<DimensionType>emptySet()));

        report.setContent(content);

    }

    public String generateFileName(Report report, Map<String,Object> parameterValues) {

        StringBuilder name = new StringBuilder();

        if(report.getFileName() != null) {
            name.append(resolveTemplate(report.getFileName(),
                    report.getParameters(), parameterValues));
        } else if(report.getTitle() != null) {
            name.append(report.getTitle());
        } else {
            name.append("Report");
        }

        return name.toString();
    }


}
