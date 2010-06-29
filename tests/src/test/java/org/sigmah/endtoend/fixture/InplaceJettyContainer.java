/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend.fixture;

public class InplaceJettyContainer extends AbstractContainer {

    @Override
    public String getUrl(String page) {
        StringBuilder url = new StringBuilder();
        url.append("http://localhost:9090/activityinfo/");
        url.append(page);
        return url.toString();
    }
}
