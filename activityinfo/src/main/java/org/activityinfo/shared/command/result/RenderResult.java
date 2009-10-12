package org.activityinfo.shared.command.result;

/**
 *
 * The result of a {@link org.activityinfo.shared.command.RenderElement} command.
 *
 * @author Alex Bertram
 */
public class RenderResult implements CommandResult {

    private String url;

    public RenderResult() {
    }

    public RenderResult(String url) {
        this.url = url;
    }

    /**
     * @return The URL from which the rendered file can be accessed.
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
