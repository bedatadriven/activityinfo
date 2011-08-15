package org.sigmah.client.page.common;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;

public class CheckBoxList<M extends ModelData> extends TreePanel<M> {

	public CheckBoxList(TreeStore<M> store) {
		super(store);
        
        setCheckable(true);
        setCheckNodes(TreePanel.CheckNodes.LEAF);
        setCheckStyle(TreePanel.CheckCascade.NONE);
        getStyle().setNodeCloseIcon(null);
        getStyle().setNodeOpenIcon(null);
        setBorders(true);
        setDisplayProperty("name");
	}

}
