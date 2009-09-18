package org.activityinfo.shared.command.result;
/*
 * @author Alex Bertram
 */

public class HtmlResult implements CommandResult {

    private String html;

    public HtmlResult() {
    }

    public HtmlResult(String html) {
        this.html = html;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
