/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

import org.sigmah.shared.exception.ReportModelException;
import org.sigmah.shared.report.content.PivotChartContent;

import com.google.common.collect.Lists;

public class PivotChartReportElement extends PivotReportElement<PivotChartContent> {

    public enum Type {
		Bar,
		StackedBar,
        Line,
		ClusteredBar,
        Pie
	}

	private Type type = Type.Bar;
	private List<Dimension> categoryDimensions = new ArrayList<Dimension>();
	private List<Dimension> seriesDimension = new ArrayList<Dimension>();
	private String categoryAxisTitle;
    private String valueAxisTitle;

	public PivotChartReportElement() {
		
	}
	
	public PivotChartReportElement(Type type) {
		this.type = type;
	}

	@Override
	public Set<Dimension> allDimensions() {
		Set<Dimension> set = new HashSet<Dimension>();
		set.addAll(categoryDimensions);
		set.addAll(seriesDimension);
		return set;
	}

    @XmlElement(required = true)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

    public void setIndicator(int indicatorId) {
		getFilter().clearRestrictions(DimensionType.Indicator);
		addIndicator(indicatorId);
	}

	public void addIndicator(int indicatorId) {
		getFilter().addRestriction(DimensionType.Indicator, indicatorId);
	}

	public void addIndicator(Integer indicatorId) {
		getFilter().addRestriction(DimensionType.Indicator, indicatorId);
	}

    @XmlElement(name="dimension")
    @XmlElementWrapper(name = "series")
	public List<Dimension> getSeriesDimension() {
		return seriesDimension;
	}

	public void setSeriesDimension(List<Dimension> seriesDimension) {
		this.seriesDimension = seriesDimension;
	}

	public void addSeriesDimension(Dimension dimension) {
		this.seriesDimension.add(dimension);
	}
	
	public void setSeriesDimension(Dimension dimension) {
		this.seriesDimension = Lists.newArrayList(dimension);
	}

    @XmlElement(name="dimension")
    @XmlElementWrapper(name="categories")
	public List<Dimension> getCategoryDimensions() {
		return categoryDimensions;
	}

	public void setCategoryDimensions(List<Dimension> categoryDimensions) {
		this.categoryDimensions = categoryDimensions;
	}
	

	public void setCategoryDimension(Dimension dimension) {
		this.categoryDimensions = Lists.newArrayList(dimension);
	}

	
	public void addCategoryDimension(Dimension dimension) {
		this.categoryDimensions.add(dimension);
	}

    @XmlElement
	public String getCategoryAxisTitle() {
		return categoryAxisTitle;
	}

	public void setCategoryAxisTitle(String categoryAxisTitle) {
		this.categoryAxisTitle = categoryAxisTitle;
	}

    @XmlElement
	public String getValueAxisTitle() {
		return valueAxisTitle;
	}

	public void setValueAxisTitle(String valueAxisTitle) {
		this.valueAxisTitle = valueAxisTitle;
	}
	
	public void validateModel() {
        if (getIndicators().isEmpty()) {
            throw new ReportModelException("No indicators specified for chart", this);
        } else if (getIndicators().size() > 1 &&
                !allDimensionTypes().contains(DimensionType.Indicator)) {
            throw new ReportModelException("If multiple indicators are provided, either the category or legend dimension must be indicator.", this);
        }

        if (getSeriesDimension().size() > 0 &&
                getType() == PivotChartReportElement.Type.Bar) {

            throw new ReportModelException("Bar charts that are not stacked/clustered cannot have legend dimensions.", this);
        }
	}

}
