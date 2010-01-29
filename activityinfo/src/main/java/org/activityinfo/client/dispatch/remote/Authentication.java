package org.activityinfo.client.dispatch.remote;


/**
 * Encapsulates user identity and their authorization to access the server.
 * <p/>
 * This is normally injected by Gin, see the default {@link org.activityinfo.client.inject.AuthProvider}
 *
 * @author Alex Bertram
 */
public class Authentication {


    private String authToken;
    private String email;

    public Authentication() {

    }

    public Authentication(String authToken, String email) {
        this.authToken = authToken;
        this.email = email;
    }

    /**
     * @return The authorization token required for calls to the command service
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
}
