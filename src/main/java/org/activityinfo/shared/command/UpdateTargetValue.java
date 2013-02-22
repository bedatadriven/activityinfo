package org.activityinfo.shared.command;

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

import java.util.Map;

import org.activityinfo.shared.command.result.VoidResult;

import com.extjs.gxt.ui.client.data.RpcMap;

public class UpdateTargetValue implements MutatingCommand<VoidResult> {

    private int targetId;
    private int indicatorId;
    private RpcMap changes;

    public UpdateTargetValue() {

    }

    public UpdateTargetValue(int targetId, int indicatorId,
        Map<String, Object> changes) {
        this.targetId = targetId;
        this.indicatorId = indicatorId;
        this.changes = new RpcMap();
        this.changes.putAll(changes);
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(int indicatorId) {
        this.indicatorId = indicatorId;
    }

    public RpcMap getChanges() {
        return changes;
    }

    public void setChanges(RpcMap changes) {
        this.changes = changes;
    }

}
