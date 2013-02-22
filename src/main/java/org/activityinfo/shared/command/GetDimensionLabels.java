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
import java.util.Set;

import org.activityinfo.shared.command.GetDimensionLabels.DimensionLabels;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.report.model.DimensionType;

public class GetDimensionLabels implements Command<DimensionLabels> {

    private DimensionType type;
    private Set<Integer> ids;

    public GetDimensionLabels(DimensionType type, Set<Integer> ids) {
        this.type = type;
        this.ids = ids;
    }

    public DimensionType getType() {
        return type;
    }

    public void setType(DimensionType type) {
        this.type = type;
    }

    public Set<Integer> getIds() {
        return ids;
    }

    public void setIds(Set<Integer> ids) {
        this.ids = ids;
    }

    public static class DimensionLabels implements CommandResult {
        private Map<Integer, String> labels;

        public DimensionLabels(Map<Integer, String> labels) {
            this.labels = labels;
        }

        public DimensionLabels() {

        }

        public Map<Integer, String> getLabels() {
            return labels;
        }

        public void setLabels(Map<Integer, String> labels) {
            this.labels = labels;
        }

    }
}
