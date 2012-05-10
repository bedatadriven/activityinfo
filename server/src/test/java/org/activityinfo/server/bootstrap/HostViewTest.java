/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import org.activityinfo.server.bootstrap.model.HostPageModel;
import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.server.database.hibernate.entity.User;
import org.junit.Test;

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
