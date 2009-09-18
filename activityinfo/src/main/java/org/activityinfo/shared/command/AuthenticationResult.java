package org.activityinfo.shared.command;

import java.io.Serializable;
import java.util.List;

public class AuthenticationResult implements Serializable {

	private String authToken;
	private String userName;

    private AuthenticationResult() {

    }

	public AuthenticationResult(String authToken, String userName) {
	    this.authToken = authToken;
        this.userName = userName;
	}


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuthToken() {
        return authToken;
    }


}
