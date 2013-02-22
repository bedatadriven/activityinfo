

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

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.dao.IndicatorDAO;
import org.activityinfo.server.database.hibernate.entity.Indicator;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.i18n.LocaleHelper;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.report.content.PivotChartContent;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.PivotChartReportElement;

import com.google.inject.Inject;

public class PivotChartGenerator extends PivotGenerator<PivotChartReportElement> {

    private final IndicatorDAO indicatorDAO;

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
        content.setYMin(scale.getValmin());
        content.setYStep(scale.getStep());
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

        if(element.getIndicators() == null || element.getIndicators().size() <= 0){
        	return "[Empty]";
        }
        
        int indicatorId = element.getIndicators().iterator().next();

        Indicator indicator = indicatorDAO.findById(indicatorId);

        return indicator != null ? indicator.getUnits() : "[Empty]";

    }


}
