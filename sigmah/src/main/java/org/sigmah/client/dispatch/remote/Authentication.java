/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch.remote;

import org.sigmah.shared.domain.User;

/**
 * Encapsulates user identity and their authorization to access the server.
 * 
 * This is normally injected by Gin, see the default
 * {@link org.sigmah.client.inject.AuthProvider}
 * 
 * @author Alex Bertram
 */
public class Authentication {
    private String authToken;
    private String email;
    private int userId;
    private int organizationId;
    private int orgUnitId;
    private boolean showMenus;
    private String userName;
    private String userFirstName;
    private String completeName;
    private String shortName;

    /**
     * 
     * @param userId
     *            user's id (from the server's database)
     * @param authToken
     *            authentication token, from
     *            {@link org.sigmah.server.domain.Authentication}
     * @param email
     */
    public Authentication(int userId, String authToken, String email) {
        this.userId = userId;
        this.authToken = authToken;
        this.email = email;
    }

    /**
     * Default constuctor for dummy tokens.
     * 
     */
    public Authentication() {

    }

    /**
     * @return Unique ID for the user, from the server's database
     */
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return The authentication token required for calls to the command
     *         service
     */
    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     * @return The email address of the currently authenticated user
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 
     * @return The organization id.
     */
    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * 
     * @return The organizational unit id.
     */
    public int getOrgUnitId() {
        return orgUnitId;
    }

    public void setOrgUnitId(int orgUnitId) {
        this.orgUnitId = orgUnitId;
    }

    /**
     * 
     * @return If the activityInfo menus has to been shown.
     */
    public boolean isShowMenus() {
        return showMenus;
    }

    public void setShowMenus(boolean showMenus) {
        this.showMenus = showMenus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    /**
     * Gets the formatted complete name of the connected user.
     * <ul>
     * <li>If the user has a first name and a last name, returns '<i>John
     * Doe</i>'.</li>
     * <li>If the user hasn't a first name and has a last name, returns
     * '<i>Doe</i>'.</li>
     * <li>If the user has neither a first name or a last name, returns an empty
     * string.</li>
     * </ul>
     * 
     * @return The complete name.
     */
    public String getUserCompleteName() {

        if (completeName == null) {
            completeName = User.getUserCompleteName(userFirstName, userName);
        }

        return completeName;
    }

    /**
     * Gets the formatted short name of the connected user.
     * <ul>
     * <li>If the user has a first name and a last name, returns '<i>J.
     * Doe</i>'.</li>
     * <li>If the user hasn't a first name and has a last name, returns
     * '<i>Doe</i>'.</li>
     * <li>If the user has neither a first name or a last name, returns an empty
     * string.</li>
     * </ul>
     * 
     * @return The short name.
     */
    public String getUserShortName() {

        if (shortName == null) {
            shortName = User.getUserShortName(userFirstName, userName);
        }

        return shortName;
    }

    /**
     * @return the name of the local Sqlite database for this user
     */
    public String getLocalDbName() {
        return "user" + userId;
    }
}
