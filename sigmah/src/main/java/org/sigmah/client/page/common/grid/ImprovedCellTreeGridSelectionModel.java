/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.grid;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.widget.treegrid.CellTreeGridSelectionModel;

import java.util.ArrayList;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class ImprovedCellTreeGridSelectionModel<M extends ModelData> extends CellTreeGridSelectionModel<M> {

    @Override
    public M getSelectedItem() {
        return getSelectCell().model;
    }


    @Override
    public List<M> getSelectedItems() {
        List<M> list = new ArrayList<M>(1);
        list.add(getSelectCell().model);
        return list;
    }
}
