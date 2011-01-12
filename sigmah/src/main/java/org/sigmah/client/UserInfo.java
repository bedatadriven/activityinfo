package org.sigmah.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sigmah.shared.domain.profile.GlobalPermissionEnum;
import org.sigmah.shared.domain.profile.PrivacyGroupPermissionEnum;
import org.sigmah.shared.dto.OrgUnitDTOLight;
import org.sigmah.shared.dto.OrganizationDTO;
import org.sigmah.shared.dto.UserInfoDTO;
import org.sigmah.shared.dto.profile.PrivacyGroupDTO;
import org.sigmah.shared.dto.profile.ProfileDTO;

import com.allen_sauer.gwt.log.client.Log;
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

    /**
     * Profiles (org unit).
     */
    private List<ProfileDTO> profiles;
    private ProfileDTO aggregatedProfile;
    private boolean profilesHaveBeenSet;

    private final ArrayList<AsyncCallback<OrganizationDTO>> queueOrganization;
    private final ArrayList<AsyncCallback<OrgUnitDTOLight>> queueOrgUnit;
    private final ArrayList<AsyncCallback<ProfileDTO>> queueAggregatedProfile;
    private final ArrayList<AsyncCallback<List<ProfileDTO>>> queueProfiles;

    public UserInfo() {
        queueOrganization = new ArrayList<AsyncCallback<OrganizationDTO>>();
        queueOrgUnit = new ArrayList<AsyncCallback<OrgUnitDTOLight>>();
        queueAggregatedProfile = new ArrayList<AsyncCallback<ProfileDTO>>();
        queueProfiles = new ArrayList<AsyncCallback<List<ProfileDTO>>>();
        organizationHasBeenSet = false;
        orgUnitHasBeenSet = false;
        profilesHaveBeenSet = false;
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
     * Gets the org unit. If the org unit isn't available immediately, the
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
     * Gets the aggregated profile. If the aggregated profile isn't available
     * immediately, the callback will be called after the aggregated profile has
     * been set by the first server call.
     * 
     * @param callback
     *            The callback.
     */
    public void getAggregatedProfile(AsyncCallback<ProfileDTO> callback) {

        // If the profile is available, returns it immediately.
        if (profilesHaveBeenSet) {
            callback.onSuccess(aggregatedProfile);
        }
        // Else put the callback in queue to be called later.
        else {
            queueAggregatedProfile.add(callback);
        }
    }

    /**
     * Gets the profiles list. If the profiles list isn't available immediately,
     * the callback will be called after the profiles list has been set by the
     * first server call.
     * 
     * @param callback
     *            The callback.
     */
    public void getProfiles(AsyncCallback<List<ProfileDTO>> callback) {

        // If the profiles are available, returns them immediately.
        if (profilesHaveBeenSet) {
            callback.onSuccess(profiles);
        }
        // Else put the callback in queue to be called later.
        else {
            queueProfiles.add(callback);
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
     * Sets the profiles and call all waiting jobs.
     * 
     * @param profiles
     *            The list of profiles.
     * @param aggregatedProfile
     *            The aggregated profile.
     */
    private void setProfiles(List<ProfileDTO> profiles, ProfileDTO aggregatedProfile) {

        this.profiles = profiles;
        this.aggregatedProfile = aggregatedProfile;

        for (final AsyncCallback<List<ProfileDTO>> callback : queueProfiles) {
            callback.onSuccess(profiles);
        }

        for (final AsyncCallback<ProfileDTO> callback : queueAggregatedProfile) {
            callback.onSuccess(aggregatedProfile);
        }

        // Clears the queue.
        queueAggregatedProfile.clear();

        profilesHaveBeenSet = true;
    }

    /**
     * Sets all the user info and call all waiting jobs.
     * 
     * @param info
     *            The info.
     */
    public void setUserInfo(UserInfoDTO info) {

        // Debug info.
        if (Log.isDebugEnabled()) {

            final StringBuilder sb = new StringBuilder();

            sb.append("Sets user infos:");

            // Organization.
            sb.append("\n- Organization: ");
            sb.append(info.getOrganization().getName());

            // Org unit.
            sb.append("\n- Organization unit: ");
            sb.append(info.getOrgUnit().getName());

            // Profiles list.
            if (info.getProfiles() != null) {
                for (final ProfileDTO p : info.getProfiles()) {
                    sb.append("\n- Profile: ");
                    sb.append(p.getName());
                    sb.append("\n\tGlobal permissions: ");
                    for (final GlobalPermissionEnum perm : p.getGlobalPermissions()) {
                        sb.append(perm.name());
                        sb.append(" | ");
                    }
                    sb.append("\n\tPrivacy groups: ");
                    for (final Map.Entry<PrivacyGroupDTO, PrivacyGroupPermissionEnum> perm : p.getPrivacyGroups()
                            .entrySet()) {
                        sb.append(perm.getKey().getTitle());
                        sb.append(" - ");
                        sb.append(perm.getValue().name());
                        sb.append(" | ");
                    }
                }
            }

            // Aggregated profile.
            sb.append("\n- Aggregated profile: ");
            sb.append(info.getAggregatedProfile().getName());
            sb.append("\n\tGlobal permissions: ");
            for (final GlobalPermissionEnum perm : info.getAggregatedProfile().getGlobalPermissions()) {
                sb.append(perm.name());
                sb.append(" | ");
            }
            sb.append("\n\tPrivacy groups: ");
            for (final Map.Entry<PrivacyGroupDTO, PrivacyGroupPermissionEnum> perm : info.getAggregatedProfile()
                    .getPrivacyGroups().entrySet()) {
                sb.append(perm.getKey().getTitle());
                sb.append(" - ");
                sb.append(perm.getValue().name());
                sb.append(" | ");
            }

            Log.debug(sb.toString());
        }

        // Sets info.
        setOrganization(info.getOrganization());
        setOrgUnit(info.getOrgUnit().light());
        setProfiles(info.getProfiles(), info.getAggregatedProfile());
    }
}
