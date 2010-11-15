/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

/**
 * Application icons
 */
package org.sigmah.client.page.project.dashboard.funding;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * Provides access to the application's icons through GWT's magic ImageBundle
 * generator.
 */
@SuppressWarnings("deprecation")
public interface FundingIconImageBundle extends ImageBundle {
    final FundingIconImageBundle ICONS = (FundingIconImageBundle) GWT.create(FundingIconImageBundle.class);

    @Resource(value = "funding.png")
    AbstractImagePrototype fundingLarge();

    @Resource(value = "funding2.png")
    AbstractImagePrototype fundingMedium();

    @Resource(value = "funding3.png")
    AbstractImagePrototype fundingMediumTransparent();

    @Resource(value = "funding4.png")
    AbstractImagePrototype fundingSmall();

    @Resource(value = "ngo.png")
    AbstractImagePrototype ngoLarge();

    @Resource(value = "ngo2.png")
    AbstractImagePrototype ngoMedium();

    @Resource(value = "ngo3.png")
    AbstractImagePrototype ngoMediumTransparent();

    @Resource(value = "ngo4.png")
    AbstractImagePrototype ngoSmall();

    @Resource(value = "partner.png")
    AbstractImagePrototype localPartnerLarge();

    @Resource(value = "partner2.png")
    AbstractImagePrototype localPartnerMedium();

    @Resource(value = "partner3.png")
    AbstractImagePrototype localPartnerMediumTransparent();

    @Resource(value = "partner4.png")
    AbstractImagePrototype localPartnerSmall();
}
