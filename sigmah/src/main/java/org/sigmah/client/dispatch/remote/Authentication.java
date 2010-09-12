/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch.remote;

/**
 * Encapsulates user identity and their authorization to access the server.

 * This is normally injected by Gin, see the default {@link org.sigmah.client.inject.AuthProvider}
 *
 * @author Alex Bertram
 */
public class Authentication {
    private String authToken;
    private String email;
    private int userId;

    /**
     * 
     * @param userId user's id (from the server's database)
     * @param authToken authentication token, from {@link org.sigmah.server.domain.Authentication}
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

    /**
     * @return The authentication token required for calls to the command service
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * @return The email address of the currently authenticated user
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the name of the local Sqlite database for this user
     */
    public String getLocalDbName() {
        return "user" + userId;
    }
}
