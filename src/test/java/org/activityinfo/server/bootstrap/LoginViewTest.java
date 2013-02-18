/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import org.activityinfo.server.bootstrap.model.LoginPageModel;
import org.junit.Test;

public class LoginViewTest extends ViewTestCase {

    @Test
    public void templateProcesses() {
        assertProcessable(new LoginPageModel());
    }

    @Test
    public void templateWithUrlSuffixProcesses() {
        assertProcessable(new LoginPageModel("#charts"));
    }


}
