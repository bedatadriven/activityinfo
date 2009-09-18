package org.activityinfo.server.report.generator;

import java.util.*;

import org.activityinfo.server.dao.SchemaDAO;
import org.activityinfo.server.dao.hibernate.PivotDAO;
import org.activityinfo.server.domain.Indicator;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.report.content.PivotChartContent;
import org.activityinfo.shared.report.model.*;

import com.google.inject.Inject;

public class PivotChartGenerator extends PivotGenerator<PivotChartElement> {

	protected final SchemaDAO schemaDAO;

	@Inject
	public PivotChartGenerator(PivotDAO pivotDAO, SchemaDAO schemaDAO) {
		super(pivotDAO);
		
		this.schemaDAO = schemaDAO;
	}

    @Override
    public void generate(User user, PivotChartElement element, Filter inheritedFilter,
                         Map<String, Object> parameterValues) {

		if(element.getIndicators().size() == 0) {
			throw new ModelException("No indicators specified for chart", element);
		} else if(element.getIndicators().size() > 1 && 
				!element.allDimensionTypes().contains(DimensionType.Indicator)) {
			throw new ModelException("If multiple indicators are provided, either the category or legend dimension must be indicator.", element);
		}
		
		if(element.getLegendDimensions().size() > 0 && 
				element.getType() == PivotChartElement.Type.Bar) {
			
			throw new ModelException("Bar charts that are not stacked/clustered cannot have legend dimensions.", element);
		}


        Filter filter = ParamFilterHelper.resolve(element.getFilter(), parameterValues);
        Filter effectiveFilter = inheritedFilter == null ? new Filter(filter, new Filter()) : new Filter(inheritedFilter, filter);

        /*
         * Replace any inherited filters on indicators with those explicitly defined
         * here
         */
        effectiveFilter.clearRestrictions(DimensionType.Indicator);
        for(ParameterizedValue<Integer> indicator : element.getIndicators()) {
            effectiveFilter.addRestriction(DimensionType.Indicator, indicator.resolve(parameterValues));
        }

        PivotChartContent content = new PivotChartContent();
        content.setXAxisTitle(composeXAxisTitle(element));

        
        content.setYAxisTitle(composeYAxisTitle(element, parameterValues));
        content.setEffectiveFilter(filter);
        content.setFilterDescriptions(generateFilterDescriptions(filter, element.allDimensionTypes()));
        content.setData(generateData(user.getLocaleObject(), element, effectiveFilter, element.getCategoryDimensions(), element.getLegendDimensions()));

        element.setContent(content);
	}
	
	/**
	 * 
	 * @param element
	 * @return The category axis title, if explicitly specified, otherwise the name
	 * of the dimension type of the last category dimension
	 */
	protected String composeXAxisTitle(PivotChartElement element) {
		
		if(element.getCategoryAxisTitle()!=null) {
			return element.getCategoryAxisTitle();
		}

        if(element.getCategoryDimensions().size()==0) {
            return null;
        }

		
		// TODO : localize
		return element.getCategoryDimensions()
			.get(element.getCategoryDimensions().size()-1).getType().toString();
	
	}

	/**
	 * 
	 * @param element
	 * @return The value axis title, if specified explicitly, otherwise the units
	 * 			of the first indicator referenced
	 */
	protected String composeYAxisTitle(PivotChartElement element, Map<String, Object> parameterValues) {
		
		if(element.getValueAxisTitle() != null) {
			return element.getValueAxisTitle();
		}
		
		int indicatorId = element.getIndicators().get(0).resolve(parameterValues);
		
		Indicator indicator = schemaDAO.findById(Indicator.class, indicatorId);
		
		return indicator.getUnits();
	
	}



}
