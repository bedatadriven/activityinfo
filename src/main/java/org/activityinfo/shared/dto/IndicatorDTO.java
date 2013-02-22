package org.activityinfo.shared.dto;

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

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonView;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * One-to-one DTO for the
 * {@link org.activityinfo.server.database.hibernate.entity.Indicator} domain
 * object.
 * 
 * @author Alex Bertram
 */
@JsonAutoDetect(JsonMethod.NONE)
public final class IndicatorDTO extends BaseModelData implements EntityDTO,
    ProvidesKey {
    public final static int AGGREGATE_SUM = 0;
    public final static int AGGREGATE_AVG = 1;
    public final static int AGGREGATE_SITE_COUNT = 2;

    public static final String PROPERTY_PREFIX = "I";

    public static final int UNITS_MAX_LENGTH = 15;
    public static final int MAX_LIST_HEADER_LENGTH = 29;
    public static final int MAX_CATEGORY_LENGTH = 50;

    private IndicatorLinkDTO indicatorLinks;

    public IndicatorDTO() {
        super();
    }

    /**
     * 
     * @param name
     *            the name of the Indicator
     * @param units
     *            string describing this Indicator's units
     */
    public IndicatorDTO(String name, String units) {
        super();
        set("name", name);
        set("units", units);
    }

    /**
     * Constructs a copy of the given IndicatorDTO
     */
    public IndicatorDTO(IndicatorDTO dto) {
        super(dto.getProperties());
    }

    /**
     * 
     * @return the id of the Indicator
     */
    @Override
    @JsonProperty
    @JsonView(DTOViews.Schema.class)
    public int getId() {
        return (Integer) get("id");
    }

    /**
     * Sets the Indicator's id
     */
    public void setId(int id) {
        set("id", id);
    }

    /**
     * Sets the Indicator's name
     */
    public void setName(String name) {
        set("name", name);
    }

    /**
     * @return the Indicator's name
     */
    @Override
    @JsonProperty
    @JsonView(DTOViews.Schema.class)
    public String getName() {
        return get("name");
    }

    /**
     * Sets the Indicator's units, for example, "household" or "%"
     */
    public void setUnits(String units) {
        set("units", units);
    }

    /**
     * @return the Indicator's units
     */
    @JsonProperty
    @JsonView(DTOViews.Schema.class)
    public String getUnits() {
        return get("units");
    }

    /**
     * @return the short list header used when displaying this Indicator in a
     *         grid
     */
    @JsonProperty
    @JsonView(DTOViews.Schema.class)
    public String getListHeader() {
        return get("listHeader");
    }

    /**
     * Sets the short list header that is used when this Indicator's values are
     * displayed in a grid.
     */
    public void setListHeader(String value) {
        set("listHeader", value);
    }

    /**
     * Full description of this Indicator, used to aid users entering data.
     */

    public void setDescription(String description) {
        set("description", description);
    }

    /**
     * @return this Indicator's description, principally used to aid users
     *         entering data
     */
    @JsonProperty
    @JsonView(DTOViews.Schema.class)
    public String getDescription() {
        return get("description");
    }

    /**
     * Sets the aggregation method for this indicator
     */
    public void setAggregation(int aggregation) {
        set("aggregation", aggregation);
    }

    /**
     * @return the aggregation method for this indicator
     */
    @JsonProperty
    @JsonView(DTOViews.Schema.class)
    public int getAggregation() {
        return (Integer) get("aggregation");
    }

    /**
     * @return this Indicator's category
     */
    @JsonProperty
    @JsonView(DTOViews.Schema.class)
    public String getCategory() {
        return get("category");
    }

    /**
     * Sets this Indicator's category
     */
    public void setCategory(String category) {
        set("category", category);
    }

    /**
     * 
     * @return the name of the property in which values for this indicator are
     *         stored, for example in the
     *         {@link org.activityinfo.shared.dto.SiteDTO} object.
     * 
     */
    public String getPropertyName() {
        return getPropertyName(this.getId());
    }

    public IndicatorLinkDTO getIndicatorLinks() {
        return indicatorLinks;
    }

    public void setIndicatorLinks(IndicatorLinkDTO indicatorLinks) {
        this.indicatorLinks = indicatorLinks;
    }

    /**
     * Returns the name of the property in which values for Indicators of this
     * id are stored, for example in the
     * {@link org.activityinfo.shared.dto.SiteDTO} object.
     * 
     * For example, an indicator with the id of 3 will be stored as I3 =>
     * 1432.32 in a SiteDTO.
     * 
     * @param id
     * @return the property name for
     */
    public static String getPropertyName(int id) {
        return PROPERTY_PREFIX + id;
    }

    /**
     * Parses an Indicator property name, for example "I432" or "I565" for the
     * referenced indicator Id.
     * 
     * @return the id of referenced Indicator
     */
    public static int indicatorIdForPropertyName(String propertyName) {
        return Integer
            .parseInt(propertyName.substring(PROPERTY_PREFIX.length()));
    }

    @Override
    public String getEntityName() {
        return "Indicator";
    }

    @Override
    public String getKey() {
        return "i" + getId();
    }
}
