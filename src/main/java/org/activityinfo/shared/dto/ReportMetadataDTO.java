/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.dto;

import java.util.List;

import org.activityinfo.shared.report.model.EmailDelivery;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * One-to-one DTO for the {@link org.activityinfo.server.database.hibernate.entity.ReportDefinition} domain class
 *
 * @author Alex Bertram
 */
public final class ReportMetadataDTO extends BaseModelData implements DTO {

	private List<String> subscribers;
    /**
     * Dummy reference to assure that GWT includes ReportFrequency is included
     * in the list of classes to serialize.
     */
    private EmailDelivery freq_;

    public ReportMetadataDTO() {
		setEmailDelivery(EmailDelivery.NONE);
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
     *
     * @return true if the current user permission to edit this report definition
     */
	public boolean isEditAllowed() {
		return (Boolean) get("editAllowed", false);
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
    public EmailDelivery getEmailDelivery() {
        return get("emailDelivery", EmailDelivery.NONE);
    }

    /**
     * Sets the ReportFrequency of this ReportDefinition
     */
    public void setEmailDelivery(EmailDelivery frequency) {
        set("emailDelivery", frequency);
    }

    /**
     * @return the day of the month [1, 31] on which this ReportDefinition is to be published
     */
    public Integer getDay() {
        return get("day", 0);
    }

    /**
     * Sets the day of the month on which this ReportDefinition is to be published.
     */
    public void setDay(Integer day) {
        set("day", day);
    }
    
	public List<String> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(List<String> subscribers) {
		this.subscribers = subscribers;
	}
	

	public void setDashboard(boolean dashboard) {
		set("dashboard", dashboard);
	}
	
	public boolean isDashboard() {
		return get("dashboard", false);
	}
    
}
