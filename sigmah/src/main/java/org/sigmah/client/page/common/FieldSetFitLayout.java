/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common;

import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * Subclass of {@link com.extjs.gxt.ui.client.widget.layout.FitLayout} that leaves room
 * for a {@link com.extjs.gxt.ui.client.widget.form.FieldSet}'s header.
 * 
* @author Alex Bertram
*/
public class FieldSetFitLayout extends FitLayout {
    @Override
    protected void setItemSize(Component item, Size size) {
        size.height = size.height - 30;
        super.setItemSize(item, size);
    }
}
