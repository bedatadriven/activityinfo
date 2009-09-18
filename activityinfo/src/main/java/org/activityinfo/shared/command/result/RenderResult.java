package org.activityinfo.shared.command.result;
/*
 * @author Alex Bertram
 */

public class RenderResult implements CommandResult {

    private String url;

    public RenderResult() {
    }

    public RenderResult(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
