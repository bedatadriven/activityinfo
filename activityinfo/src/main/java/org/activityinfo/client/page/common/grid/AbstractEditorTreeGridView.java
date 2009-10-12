package org.activityinfo.client.page.common.grid;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.treegrid.EditorTreeGrid;
/*
 * @author Alex Bertram
 */

public abstract class AbstractEditorTreeGridView<ModelT extends ModelData, PresenterT extends GridPresenter<ModelT>>
        extends AbstractEditorGridView<ModelT, PresenterT>
        implements TreeGridView<PresenterT, ModelT> {

    private EditorTreeGrid<ModelT> tree;

    @Override
    protected void initGridListeners(Grid<ModelT> grid) {
        super.initGridListeners(grid);

        this.tree = (EditorTreeGrid<ModelT>) grid;

    }

    


}
