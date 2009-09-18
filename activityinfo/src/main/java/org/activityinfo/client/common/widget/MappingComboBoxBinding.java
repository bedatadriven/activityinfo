package org.activityinfo.client.common.widget;

import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.Converter;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.data.ModelData;
/*
 * @author Alex Bertram
 */

public class MappingComboBoxBinding extends FieldBinding {

    public MappingComboBoxBinding(final MappingComboBox field, String property) {
        super(field, property);
        setConvertor(new Converter() {
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
