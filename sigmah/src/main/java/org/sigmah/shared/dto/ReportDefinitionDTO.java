/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;
import org.sigmah.shared.report.model.ReportFrequency;

/**
 * One-to-one DTO for the {@link org.sigmah.server.domain.ReportDefinition} domain class
 *
 * @author Alex Bertram
 */
public final class ReportDefinitionDTO extends BaseModelData implements DTO {

    /**
     * Dummy reference to assure that GWT includes ReportFrequency is included
     * in the list of classes to serialize.
     */
    private ReportFrequency freq_;

    public ReportDefinitionDTO() {
		setFrequency(ReportFrequency.NotDateBound);
	}

    /**
     * @return this ReportDefinition's id
     */
    public int getId() {
		return (Integer)get("id");
	}

    /**
     * Sets the id of this ReportDefinition
     */
	public void setId(int id) {
		set("id", id);
	}

    /**
     * Sets the title of this ReportDefinition
     *
     */
	public void setTitle(String title) {
		set("title", title);
	}

    /**
     * @return the title of this ReportDefinition
     */
	public String getTitle() {
		return get("title");
	}

    /**
     * Sets the description of this ReportDefinition
     */
	public void setDescription(String description) {
		set("description", description);
	}

    /**
     * @return  the description of this ReportDefinition
     */
	public String getDescription() {
		return get("description");
	}

    /**
     * Sets the name of the UserDatabase which this ReportDefinition references.
     * If this ReportDefinition references multiple UserDatabases, name should be null.
     */
    public void setDatabaseName(String name) {
        set("databaseName", name);
    }
    /**
     *
     * @return true if the current user permission to edit this report definition
     */
	public boolean isEditAllowed() {
		return (Boolean)get("editAllowed");
	}

    /**
     * Sets the permission of the current user to edit this report definition
     */
	public void setEditAllowed(boolean allowed) {
		set("editAllowed", allowed);
	}

    /**
     * @return  the name of the User who owns this ReportDefinition
     */
	public String getOwnerName() {
		return get("ownerName");
	}

    /**
     * Sets the name of the User who own this ReportDefinition
     */
	public void setOwnerName(String name) {
		set("ownerName", name);
	}

    /**
     * Sets whether the current user is the owner of this ReportDefintion
     */
    public void setAmOwner(boolean amOwner) {
		set("amOwner", amOwner);
	}

    /**
     * @return true if the current user is the owner of this ReportDefinition
     */
	public boolean getAmOwner() {
		return (Boolean)get("amOwner");
	}

    /**
     * @return the ReportFrequency of this ReportDefinition
     */
    public ReportFrequency getFrequency() {
        return get("frequency");
    }

    /**
     * Sets the ReportFrequency of this ReportDefinition
     */
    public void setFrequency(ReportFrequency frequency) {
        set("frequency", frequency);
    }

    /**
     * @return the day of the month [1, 31] on which this ReportDefinition is to be published
     */
    public Integer getDay() {
        return get("day");
    }

    /**
     * Sets the day of the month on which this ReportDefinition is to be published.
     */
    public void setDay(Integer day) {
        set("day", day);
    }

    /**
     * See {@link org.sigmah.server.domain.ReportSubscription#isSubscribed()}
     *
     * @return true if the current user is subscribed to this ReportDefinition
     */
    public boolean isSubscribed() {
        return (Boolean)get("subscribed");
    }

    /**
     * Sets whether the current user is subscribed to this ReportDefinition.
     * See {@link org.sigmah.server.domain.ReportSubscription#setSubscribed(boolean)}
     */
    public void setSubscribed(boolean subscribed) {
        set("subscribed", subscribed);
    }
}
