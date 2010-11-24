package org.sigmah.shared.dto.element;

import java.util.Date;

import org.sigmah.shared.domain.element.DefaultFlexibleElementType;
import org.sigmah.shared.dto.CountryDTO;

/**
 * Defines a DTO class that contains default flexible elements.
 * 
 * @author tmi
 * 
 */
public interface DefaultFlexibleElementContainer extends FlexibleElementContainer {

    /**
     * @return the property for the default type
     *         {@link DefaultFlexibleElementType#CODE}.
     */
    public String getName();

    /**
     * @return the property for the default type
     *         {@link DefaultFlexibleElementType#TITLE}.
     */
    public String getFullName();

    /**
     * @return the property for the default type
     *         {@link DefaultFlexibleElementType#START_DATE}.
     */
    public Date getStartDate();

    /**
     * @return the property for the default type
     *         {@link DefaultFlexibleElementType#END_DATE}.
     */
    public Date getEndDate();

    /**
     * @return the property for the default type
     *         {@link DefaultFlexibleElementType#BUDGET}.
     */
    public Double getPlannedBudget();

    /**
     * @return the property for the default type
     *         {@link DefaultFlexibleElementType#BUDGET}.
     */
    public Double getSpendBudget();

    /**
     * @return the property for the default type
     *         {@link DefaultFlexibleElementType#BUDGET}.
     */
    public Double getReceivedBudget();

    /**
     * @return the property for the default type
     *         {@link DefaultFlexibleElementType#COUNTRY}.
     */
    public CountryDTO getCountry();

    /**
     * @return the property for the default type
     *         {@link DefaultFlexibleElementType#OWNER}.
     */
    public String getOwnerFirstName();

    /**
     * @return the property for the default type
     *         {@link DefaultFlexibleElementType#OWNER}.
     */
    public String getOwnerName();
}
