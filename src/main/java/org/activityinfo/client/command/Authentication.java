package org.activityinfo.client.command;
/*
 * @author Alex Bertram
 */

public class Authentication {

    private String authToken;
    private String email;

    public Authentication(){
        
    }

    public Authentication(String authToken, String email) {
        this.authToken = authToken;
        this.email = email;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
