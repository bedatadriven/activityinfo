package org.activityinfo.shared.report.model;

import org.activityinfo.shared.report.content.PivotChartContent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PivotChartElement extends PivotElement<PivotChartContent> {

	public enum Type {
		Bar,
		StackedBar,
		ClusteredBar,
        Pie
	}

	private Type type;
	private List<Dimension> legendDimensions = new ArrayList<Dimension>();
	private List<Dimension> categoryDimensions = new ArrayList<Dimension>();
	private List<ParameterizedValue<Integer>> indicators = new ArrayList<ParameterizedValue<Integer>>();
	
	private String categoryAxisTitle;
	private String valueAxisTitle;

	
	public PivotChartElement() {
		
	}
	
	public PivotChartElement(Type type) {
		this.type = type;
	}

	@Override
	public Set<Dimension> allDimensions() {
		Set<Dimension> set = new HashSet<Dimension>();
		set.addAll(categoryDimensions);
		set.addAll(legendDimensions);
		return set;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<ParameterizedValue<Integer>> getIndicators() {
		return indicators;
	}

	public void setIndicators(List<ParameterizedValue<Integer>> indicators) {
		this.indicators = indicators;
	}

	public void setIndicator(int indicatorId) {
		this.indicators.clear();
		this.indicators.add(ParameterizedValue.literal(indicatorId));
	}
	
	public void addIndicator(int indicatorId) {
		this.indicators.add(ParameterizedValue.literal(indicatorId));
	}

	public void addIndicator(ParameterizedValue<Integer> value) {
		this.indicators.add(value);
	}
	
	public List<Dimension> getLegendDimensions() {
		return legendDimensions;
	}

	public void setLegendDimensions(List<Dimension> legendDimensions) {
		this.legendDimensions = legendDimensions;
	}

	public void addLegendDimension(Dimension dimension) {
		this.legendDimensions.add(dimension);
	}
	
	public List<Dimension> getCategoryDimensions() {
		return categoryDimensions;
	}

	public void setCategoryDimensions(List<Dimension> categoryDimensions) {
		this.categoryDimensions = categoryDimensions;
	}
	
	public void addCategoryDimension(Dimension dimension) {
		this.categoryDimensions.add(dimension);
	}

	public String getCategoryAxisTitle() {
		return categoryAxisTitle;
	}

	public void setCategoryAxisTitle(String categoryAxisTitle) {
		this.categoryAxisTitle = categoryAxisTitle;
	}

	public String getValueAxisTitle() {
		return valueAxisTitle;
	}

	public void setValueAxisTitle(String valueAxisTitle) {
		this.valueAxisTitle = valueAxisTitle;
	}

}
