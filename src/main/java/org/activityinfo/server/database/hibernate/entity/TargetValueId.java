package org.activityinfo.server.database.hibernate.entity;

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

import javax.persistence.Column;

import com.google.common.base.Objects;

public class TargetValueId implements java.io.Serializable {

    private int targetId;
    private int indicatorId;

    public TargetValueId() {

    }

    public TargetValueId(int targetId, int indicatorId) {
        this.targetId = targetId;
        this.indicatorId = indicatorId;
    }

    @Column(name = "targetId", nullable = false)
    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    @Column(name = "indicatorId", nullable = false)
    public int getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(int indicatorId) {
        this.indicatorId = indicatorId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTargetId(), getIndicatorId());
    }

    @Override
    public boolean equals(Object obj) {
        TargetValueId i = (TargetValueId) obj;
        return Objects.equal(i.getTargetId(), this.getTargetId())
            && Objects.equal(i.getIndicatorId(), this.getIndicatorId());
    }
}
