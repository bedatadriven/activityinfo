package org.activityinfo.client.common.widget;

import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.data.ModelData;
/*
 * @author Alex Bertram
 */

public class RemoteComboBox<T extends ModelData> extends ComboBox<T> {

    @Override
    public void doQuery(String q, boolean forceAll) {
       store.getLoader().load(getParams(q));
        expand();
    }

    @Override
    public void setValue(T value) {
        super.setValue(value);
    }
}
