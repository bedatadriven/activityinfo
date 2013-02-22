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

import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public final class AdminLevelPredicates {

    private AdminLevelPredicates() {
    }

    public static List<AdminLevelDTO> breadthFirstSort(
        List<AdminLevelDTO> allLevels) {
        List<AdminLevelDTO> sorted = Lists.newArrayList();
        Predicate<AdminLevelDTO> predicate = rootLevels();
        Collection<AdminLevelDTO> next;
        while (!(next = Collections2.filter(allLevels, predicate)).isEmpty()) {
            sorted.addAll(next);
            predicate = childrenOf(next);
        }
        return sorted;
    }

    public static Predicate<AdminLevelDTO> rootLevels() {
        return new Predicate<AdminLevelDTO>() {

            @Override
            public boolean apply(AdminLevelDTO level) {
                return level.isRoot();
            }
        };
    }

    public static Predicate<AdminLevelDTO> childrenOf(
        final Collection<AdminLevelDTO> parents) {
        return new Predicate<AdminLevelDTO>() {

            @Override
            public boolean apply(AdminLevelDTO level) {
                for (AdminLevelDTO parent : parents) {
                    if (!level.isRoot()
                        && parent.getId() == level.getParentLevelId()) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

}
