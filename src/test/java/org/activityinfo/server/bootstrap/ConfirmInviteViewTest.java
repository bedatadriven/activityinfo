/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import org.activityinfo.server.bootstrap.model.ConfirmInvitePageModel;
import org.activityinfo.server.bootstrap.model.InvalidInvitePageModel;
import org.activityinfo.server.database.hibernate.entity.User;
import org.junit.Test;

public class ConfirmInviteViewTest extends ViewTestCase {

    @Test
    public void templateProcesses() {
        User user = new User();
        user.setName("Alex");
        user.setEmail("alex@bertram");
        user.setChangePasswordKey("ABC12345");
        user.setLocale("en");

        assertProcessable(new ConfirmInvitePageModel(user));
    }

    @Test
    public void invalidInvitePage() {

        assertProcessable(new InvalidInvitePageModel());

    }

}
