/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.common.grid;

import com.extjs.gxt.ui.client.data.ModelData;

public abstract class AbstractEditorTreeGridView<ModelT extends ModelData, PresenterT extends GridPresenter<ModelT>>
        extends AbstractEditorGridView<ModelT, PresenterT>
        implements TreeGridView<PresenterT, ModelT> {



}
