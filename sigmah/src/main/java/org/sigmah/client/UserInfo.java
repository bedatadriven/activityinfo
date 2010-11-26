package org.sigmah.client;

import java.util.ArrayList;

import org.sigmah.shared.dto.OrgUnitDTOLight;
import org.sigmah.shared.dto.OrganizationDTO;
import org.sigmah.shared.dto.UserInfoDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Singleton;

/**
 * Stores the user infos on client-side.
 * 
 * @author tmi
 * 
 */
@Singleton
public class UserInfo {

    /**
     * User organization.
     */
    private OrganizationDTO organization;
    private boolean organizationHasBeenSet;

    /**
     * User org unit.
     */
    private OrgUnitDTOLight orgUnit;
    private boolean orgUnitHasBeenSet;

    private final ArrayList<AsyncCallback<OrganizationDTO>> queueOrganization;
    private final ArrayList<AsyncCallback<OrgUnitDTOLight>> queueOrgUnit;

    public UserInfo() {
        queueOrganization = new ArrayList<AsyncCallback<OrganizationDTO>>();
        queueOrgUnit = new ArrayList<AsyncCallback<OrgUnitDTOLight>>();
        organizationHasBeenSet = false;
        orgUnitHasBeenSet = false;
    }

    /**
     * Gets the organization. If the organization isn't available immediately,
     * the callback will be called after the organization has been set by the
     * first server call.
     * 
     * @param callback
     *            The callback.
     */
    public void getOrganization(AsyncCallback<OrganizationDTO> callback) {

        // If the organization is available, returns it immediately.
        if (organizationHasBeenSet) {
            callback.onSuccess(organization);
        }
        // Else put the callback in queue to be called later.
        else {
            queueOrganization.add(callback);
        }
    }

    /**
     * Gets the org unit. If the organization isn't available immediately, the
     * callback will be called after the org unit has been set by the first
     * server call.
     * 
     * @param callback
     *            The callback.
     */
    public void getOrgUnit(AsyncCallback<OrgUnitDTOLight> callback) {

        // If the org unit is available, returns it immediately.
        if (orgUnitHasBeenSet) {
            callback.onSuccess(orgUnit);
        }
        // Else put the callback in queue to be called later.
        else {
            queueOrgUnit.add(callback);
        }
    }

    /**
     * Sets the organization and call all waiting jobs.
     * 
     * @param organization
     *            The new organization.
     */
    private void setOrganization(OrganizationDTO organization) {

        this.organization = organization;

        for (final AsyncCallback<OrganizationDTO> callback : queueOrganization) {
            callback.onSuccess(organization);
        }

        // Clears the queue.
        queueOrganization.clear();

        organizationHasBeenSet = true;
    }

    /**
     * Sets the org unit and call all waiting jobs.
     * 
     * @param orgUnit
     *            The new org unit.
     */
    private void setOrgUnit(OrgUnitDTOLight orgUnit) {

        this.orgUnit = orgUnit;

        for (final AsyncCallback<OrgUnitDTOLight> callback : queueOrgUnit) {
            callback.onSuccess(orgUnit);
        }

        // Clears the queue.
        queueOrgUnit.clear();

        orgUnitHasBeenSet = true;
    }

    /**
     * Sets all the user info and call all waiting jobs.
     * 
     * @param info
     *            The info.
     */
    public void setUserInfo(UserInfoDTO info) {
        setOrganization(info.getOrganization());
        setOrgUnit(info.getOrgUnit().light());
    }
}
