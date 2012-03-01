/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;


/**
 *
 * Result of a <code>Command</code> in the form of Html
 *
 * @see org.sigmah.shared.command.RenderReportHtml
 *
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
