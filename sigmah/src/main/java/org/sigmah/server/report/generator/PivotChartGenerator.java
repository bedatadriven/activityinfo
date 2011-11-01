/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator;

import org.sigmah.server.command.DispatcherSync;
import org.sigmah.server.util.LocaleHelper;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dao.IndicatorDAO;
import org.sigmah.shared.domain.Indicator;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.report.content.PivotChartContent;
import org.sigmah.shared.report.content.PivotTableData;
import org.sigmah.shared.report.model.DateRange;
import org.sigmah.shared.report.model.PivotChartReportElement;

import com.google.inject.Inject;

public class PivotChartGenerator extends PivotGenerator<PivotChartReportElement> {

    protected final IndicatorDAO indicatorDAO;

    @Inject
    public PivotChartGenerator(DispatcherSync dispatcher, IndicatorDAO indicatorDAO) {
        super(dispatcher);

        this.indicatorDAO = indicatorDAO;
    }

    @Override
    public void generate(User user, PivotChartReportElement element, Filter inheritedFilter,
                         DateRange dateRange) {

        Filter filter = GeneratorUtils.resolveElementFilter(element, dateRange);
        Filter effectiveFilter = inheritedFilter == null ? new Filter(filter, new Filter()) : new Filter(inheritedFilter, filter);

        PivotTableData data = generateData(
                user.getId(), 
                LocaleHelper.getLocaleObject(user),
                element,
                effectiveFilter,
                element.getCategoryDimensions(),
                element.getSeriesDimension());

        ScaleUtil.Scale scale = computeScale(element, data);

        PivotChartContent content = new PivotChartContent();
        content.setXAxisTitle(composeXAxisTitle(element));
        content.setYAxisTitle(composeYAxisTitle(element));
        content.setEffectiveFilter(filter);
        content.setFilterDescriptions(generateFilterDescriptions(filter, element.allDimensionTypes(), user));
        content.setYMin(scale.valmin);
        content.setYStep(scale.step);
        content.setData(data);

        element.setContent(content);
    }

    private ScaleUtil.Scale computeScale(PivotChartReportElement element, PivotTableData data) {

        if (element.getType() == PivotChartReportElement.Type.Pie) {
            return new ScaleUtil.Scale();
        }
       
        if (data.isEmpty()) {
        	return new ScaleUtil.Scale();
        }

        // find min, max values
        PivotTableData.RangeCalculator rangeCalculator = new PivotTableData.RangeCalculator();
        data.visitAllCells(rangeCalculator);

        // anchor the y axis to zero.
        // TODO: check for cases where we don't want the axis to start at zero
        // e.g. non-sum, non-count indicators
        return ScaleUtil.computeScale(0, rangeCalculator.getMaxValue(), 5);
    }

    /**
     * Composes a title for the X Axis.
     *
     * @param element The <code>PivotChartElement</code> for which to compose the title
     * @return The category axis title, if explicitly specified, otherwise the name
     *         of the dimension type of the last category dimension
     */
    protected String composeXAxisTitle(PivotChartReportElement element) {

        if (element.getCategoryAxisTitle() != null) {
            return element.getCategoryAxisTitle();
        }

        if (element.getCategoryDimensions().size() == 0) {
            return null;
        }


        // TODO : localize
        return element.getCategoryDimensions()
                .get(element.getCategoryDimensions().size() - 1).getType().toString();

    }

    /**
     * @param element
     * @return The value axis title, if specified explicitly, otherwise the units
     *         of the first indicator referenced
     */
    protected String composeYAxisTitle(PivotChartReportElement element) {

        if (element.getValueAxisTitle() != null) {
            return element.getValueAxisTitle();
        }

        int indicatorId = element.getIndicators().get(0);

        Indicator indicator = indicatorDAO.findById(indicatorId);

        return indicator != null ? indicator.getUnits() : "[Empty]";

    }


}
