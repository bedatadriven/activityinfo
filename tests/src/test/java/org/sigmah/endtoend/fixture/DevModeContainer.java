/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend.fixture;

/**
 * Models the GWT Development Mode container.
 */
public class DevModeContainer extends AbstractContainer {

    @Override
    public String getUrl(String page) {
        StringBuilder url = new StringBuilder();
        url.append("http://localhost:9090");
        url.append(page);
        url.append(page.contains("?") ? "&" : "?");
        url.append("gwt.codesvr=localhost:9997");
        return url.toString();
    }
}