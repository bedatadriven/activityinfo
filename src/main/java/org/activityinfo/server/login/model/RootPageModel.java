package org.activityinfo.server.login.model;


public class RootPageModel extends PageModel {
    private boolean showLogin;

    public boolean isShowLogin() {
        return showLogin;
    }

    public void setShowLogin(boolean showLogin) {
        this.showLogin = showLogin;
    }
}
