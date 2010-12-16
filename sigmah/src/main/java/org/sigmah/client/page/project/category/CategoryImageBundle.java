package org.sigmah.client.page.project.category;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

@SuppressWarnings("deprecation")
public interface CategoryImageBundle extends ImageBundle {

    public static final CategoryImageBundle ICONS = (CategoryImageBundle) GWT.create(CategoryImageBundle.class);

    AbstractImagePrototype circle();

    AbstractImagePrototype cross();

    AbstractImagePrototype diamond();

    AbstractImagePrototype square();

    AbstractImagePrototype star();

    AbstractImagePrototype triangle();
}
