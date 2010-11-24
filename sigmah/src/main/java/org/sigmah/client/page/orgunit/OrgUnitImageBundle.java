package org.sigmah.client.page.orgunit;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * Provides access to the application's icons through GWT's magic ImageBundle
 * generator.
 */
@SuppressWarnings("deprecation")
public interface OrgUnitImageBundle extends ImageBundle {

    final OrgUnitImageBundle ICONS = (OrgUnitImageBundle) GWT.create(OrgUnitImageBundle.class);

    @Resource(value = "orgunit.png")
    AbstractImagePrototype orgUnitLarge();

    @Resource(value = "orgunit2.png")
    AbstractImagePrototype orgUnitSmall();
    
    @Resource(value = "orgunit3.png")
    AbstractImagePrototype orgUnitSmallTransparent();
}
