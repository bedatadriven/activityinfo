package org.sigmah.server.endpoint.wfs;

import java.util.List;

public class Capabilities {

    private String postUrl;
    private String getUrl;

    private List activities;


    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getGetUrl() {
        return getUrl;
    }

    public void setGetUrl(String getUrl) {
        this.getUrl = getUrl;
    }

    public List getActivities() {
        return activities;
    }

    public void setActivities(List activities) {
        this.activities = activities;
    }
}
