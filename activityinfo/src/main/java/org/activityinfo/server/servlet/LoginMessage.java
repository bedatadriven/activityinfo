package org.activityinfo.server.servlet;
/*
 * @author Alex Bertram
 */

public class LoginMessage {

    private String message;

    public LoginMessage() {
        message = "";
    }

    public LoginMessage(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
