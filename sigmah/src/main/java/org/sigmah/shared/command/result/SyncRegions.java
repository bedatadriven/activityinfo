/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

import java.util.Iterator;
import java.util.List;

public class SyncRegions implements CommandResult, Iterable<SyncRegion> {

    private List<SyncRegion> list;

    public SyncRegions() {

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
