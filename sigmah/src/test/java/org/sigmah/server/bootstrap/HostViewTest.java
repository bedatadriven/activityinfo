/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap;

import org.junit.Test;
import org.sigmah.server.bootstrap.model.HostPageModel;
import org.sigmah.server.domain.Authentication;
import org.sigmah.shared.domain.User;

public class HostViewTest extends ViewTestCase {


    @Test
    public void templateProcesses() {

        User user = new User();
        user.setName("Alex");
        user.setEmail("akbertram@gmail.com");
        user.setLocale("fr");

        Authentication auth = new Authentication(user);
        auth.setId("XYZ12345");
        auth.setUser(user);

        assertProcessable(new HostPageModel(auth,  "http://www.activityinfo.org"));
    }
}
