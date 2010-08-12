/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.widget;

import com.extjs.gxt.ui.client.binding.Converter;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.data.ModelData;

/**
 * Extension of FieldBinding to bind 
 *
 * @author Alex Bertram
 */
public class MappingComboBoxBinding extends FieldBinding {

    public MappingComboBoxBinding(final MappingComboBox field, String property) {
        super(field, property);
        setConverter(new Converter() {
            @Override
            public Object convertModelValue(Object value) {
                return field.wrap(value);
            }

            @Override
            public Object convertFieldValue(Object value) {
                return ((ModelData)value).get("value");
            }
        });
    }
}
