package org.sigmah.client.page.project.dashboard.funding;

import org.sigmah.shared.domain.ProjectModelType;

import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Utility class to get icons for the different project type.
 * 
 * @author tmi
 * 
 */
public final class FundingIconProvider {

    /**
     * Provides only static methods.
     */
    private FundingIconProvider() {
    }

    /**
     * Defines the available size for these icons.
     * 
     * @author tmi
     * 
     */
    public static enum IconSize {
        SMALL, MEDIUM, LARGE;
    }

    /**
     * Gets the small icon for the given type.
     * 
     * @param type
     *            The type.
     * @return the small icon.
     */
    public static AbstractImagePrototype getProjectTypeIcon(ProjectModelType type) {
        return getProjectTypeIcon(type, IconSize.SMALL);
    }

    /**
     * Gets the icon for the given type at the given size.
     * 
     * @param type
     *            The type.
     * @param size
     *            The size.
     * @return The icon.
     */
    public static AbstractImagePrototype getProjectTypeIcon(ProjectModelType type, IconSize size) {

        if (type == null) {
            throw new IllegalArgumentException("The type must not be null.");
        }

        switch (type) {
        case FUNDING:
            switch (size) {
            case MEDIUM:
                return FundingIconImageBundle.ICONS.fundingMedium();
            case LARGE:
                return FundingIconImageBundle.ICONS.fundingLarge();
            default:
                return FundingIconImageBundle.ICONS.fundingSmall();
            }
        case LOCAL_PARTNER:
            switch (size) {
            case MEDIUM:
                return FundingIconImageBundle.ICONS.localPartnerMedium();
            case LARGE:
                return FundingIconImageBundle.ICONS.localPartnerLarge();
            default:
                return FundingIconImageBundle.ICONS.localPartnerSmall();
            }
        case NGO:
            switch (size) {
            case MEDIUM:
                return FundingIconImageBundle.ICONS.ngoMedium();
            case LARGE:
                return FundingIconImageBundle.ICONS.ngoLarge();
            default:
                return FundingIconImageBundle.ICONS.ngoSmall();
            }
        default:
            return FundingIconImageBundle.ICONS.localPartnerSmall();
        }
    }
}
