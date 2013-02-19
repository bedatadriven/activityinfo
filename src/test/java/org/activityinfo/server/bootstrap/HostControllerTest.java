/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.activityinfo.server.authentication.ServerSideAuthProvider;
import org.activityinfo.server.bootstrap.model.HostPageModel;
import org.activityinfo.server.bootstrap.model.LoginPageModel;
import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.view.Viewable;

public class HostControllerTest extends ControllerTestCase {

    private static final String CHROME_USER_AGENT = "Mozilla/6.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/532.0 (KHTML, like Gecko) Chrome/3.0.195.27 Safari/532.0";
    private static final String VALID_TOKEN = "XYZ123";
    
    private HostController resource;
    private ServerSideAuthProvider authProvider;
	
	@Before
	public void setup() {      
	    DeploymentConfiguration deploymentConfig = new DeploymentConfiguration(new Properties());
	
	    authProvider = new ServerSideAuthProvider();
	    resource = new HostController(deploymentConfig, authProvider);
	}

	@Test
	public void verifyThatRequestsWithoutAuthTokensAreShownLoginPage() throws Exception {

	    HttpServletRequest req = createMock(HttpServletRequest.class);
	    replay(req);
	    
	    Response response = resource.getHostPage(RestMockUtils.mockUriInfo("http://www.activityinfo.org"), req, false);

	    assertThat(response.getEntity(), instanceOf(Viewable.class));
	    assertThat(((Viewable) response.getEntity()).getModel(), instanceOf(LoginPageModel.class));
	    
	}

	@Test
	public void verifyThatRequestWithValidAuthTokensReceiveTheView() throws Exception {
	    
	    authProvider.set(new AuthenticatedUser(VALID_TOKEN, 3, "akbertram@gmail.com"));
	    

        HttpServletRequest req = createMock(HttpServletRequest.class);
        expect(req.getHeader("User-Agent")).andReturn(CHROME_USER_AGENT);
        replay(req);
	    
        Response response = resource.getHostPage(RestMockUtils.mockUriInfo("http://www.activityinfo.org"), req, false);

        assertThat(response.getEntity(), instanceOf(Viewable.class));
        assertThat(((Viewable) response.getEntity()).getModel(), instanceOf(HostPageModel.class));
	}

}
