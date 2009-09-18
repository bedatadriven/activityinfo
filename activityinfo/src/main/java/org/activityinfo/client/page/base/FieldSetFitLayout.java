package org.activityinfo.client.page.base;

import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.util.Size;

/**
* @author Alex Bertram (akbertram@gmail.com)
*/
public class FieldSetFitLayout extends FitLayout {

    public FieldSetFitLayout() {
        super();
    }

    @Override
    protected void setItemSize(Component item, Size size) {

        size.height = size.height - 30;

        super.setItemSize(item, size);
    }
}
