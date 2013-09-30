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

import java.util.Date;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonView;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.extjs.gxt.ui.client.data.BaseModelData;

/** A period where 'normal users' cannot add, update or remove data */
@JsonAutoDetect(JsonMethod.NONE)
public class LockedPeriodDTO extends BaseModelData implements EntityDTO {

    /** An object supporting locks */
    public interface HasLockedPeriod extends EntityDTO {
        public Set<LockedPeriodDTO> getLockedPeriods();

        // This should be an instance method of LockedPeriodsList
        public Set<LockedPeriodDTO> getEnabledLockedPeriods();
    }

    private HasLockedPeriod parent;
    private Integer parentId;

    public LockedPeriodDTO() {
        super();
    }

    public void setName(String name) {
        set("name", name);
    }

    @Override
    @JsonProperty
    @JsonView(DTOViews.Schema.class)
    public String getName() {
        return (String) get("name");
    }

    public void setId(int id) {
        set("id", id);
    }

    @Override
    @JsonProperty
    @JsonView(DTOViews.Schema.class)
    public int getId() {
        return (Integer) get("id");
    }

    public void setToDate(Date toDate) {
        set("toDate", new LocalDate(toDate));
    }

    public void setToDate(LocalDate toDate) {
        if (toDate == null) {
            set("toDate", null);
        } else {
            set("toDate", toDate);
        }
    }

    @JsonProperty
    @JsonView(DTOViews.Schema.class)
    public LocalDate getToDate() {
        return get("toDate");
    }

    public void setFromDate(Date fromDate) {
        if (fromDate == null) {
            set("fromDate", null);
        } else {
            set("fromDate", new LocalDate(fromDate));
        }
    }

    public void setFromDate(LocalDate fromDate) {
        set("fromDate", fromDate);
    }

    @JsonProperty
    @JsonView(DTOViews.Schema.class)
    public LocalDate getFromDate() {
        return get("fromDate");
    }

    /**
     * Returns true when startDate is before end date, and both startDate and
     * EndDate are non-null.
     */
    public boolean isValid() {
        return getFromDate() != null &&
            getToDate() != null &&
            getFromDate().before(getToDate());
    }

    public void setEnabled(boolean enabled) {
        set("enabled", enabled);
    }

    @JsonProperty
    @JsonView(DTOViews.Schema.class)
    public boolean isEnabled() {
        return (Boolean) get("enabled");
    }

    @Override
    public String getEntityName() {
        return "LockedPeriod";
    }

    public HasLockedPeriod getParent() {
        return parent;
    }

    public void setParent(HasLockedPeriod hasLock) {
        this.parent = hasLock;
        this.parentId = hasLock.getId();
        set("parentName", hasLock.getName());
        set("parentType", hasLock.getEntityName());
    }

    public boolean hasParent() {
        return parent != null;
    }

    public boolean hasParentId() {
        return parentId != 0;
    }

    public void setParentId(int id) {
        this.parentId = id;
    }

    public String getParentName() {
        return (String) get("parentName");
    }

    public int getParentId() {
        return parentId;
    }

    public String getParentType() {
        return (String) get("parentType");
    }

    /** give meaning to the parentId by specifying the type of the parent. */
    public void setParentType(String type) {
        set("parentType", type);
    }

    public boolean fallsWithinPeriod(Date date) {
        return fallsWithinPeriod(new LocalDate(date));
    }

    public boolean fallsWithinPeriod(LocalDate date) {
        LocalDate from = getFromDate();
        LocalDate to = getToDate();

        boolean isSameAsFrom = from.compareTo(date) == 0;
        boolean isSameAsTo = to.compareTo(date) == 0;
        boolean isBetween = from.before(date) && to.after(date);

        return isBetween || isSameAsFrom || isSameAsTo;
    }
}
