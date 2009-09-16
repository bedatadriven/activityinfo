package org.activityinfo.client.common.grid;

import com.extjs.gxt.ui.client.widget.treegrid.CellTreeGridSelectionModel;
import com.extjs.gxt.ui.client.data.ModelData;

import java.util.List;
import java.util.ArrayList;
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
