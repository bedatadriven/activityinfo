/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.sigmah.shared.command.result;

import java.util.Iterator;
import java.util.List;

public class SyncRegions implements CommandResult, Iterable<SyncRegion> {

    private List<SyncRegion> list;

    protected SyncRegions() {

    }

    public SyncRegions(List<SyncRegion> list) {
        this.list = list;
    }

    public List<SyncRegion> getList() {
        return list;
    }

    protected void setList(List<SyncRegion> list) {
        this.list = list;
    }

    @Override
    public Iterator<SyncRegion> iterator() {
        return list.iterator();
    }
}
