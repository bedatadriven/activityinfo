/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;

import org.activityinfo.server.bootstrap.model.ConfirmInvitePageModel;
import org.activityinfo.server.bootstrap.model.InvalidInvitePageModel;
import org.activityinfo.server.database.hibernate.dao.AuthenticationDAO;
import org.activityinfo.server.database.hibernate.dao.UserDAO;
import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.server.database.hibernate.entity.User;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.util.Providers;
import com.sun.jersey.api.view.Viewable;


public class ConfirmInviteControllerTest  {

    private static final String VALID_KEY = "xyz123";
    private UserDAO userDAO;
    private ConfirmInviteController resource;
    private User user;

    @Before
    public final void setup() {
        
        user = new User();

        userDAO = createMock(UserDAO.class);
        expect(userDAO.findUserByChangePasswordKey(eq(VALID_KEY))).andReturn(user);
        expect(userDAO.findUserByChangePasswordKey(EasyMock.not(eq(VALID_KEY)))).andThrow(new NoResultException());
        replay(userDAO);
        
        AuthenticationDAO authDAO = createMock(AuthenticationDAO.class);
        authDAO.persist(isA(Authentication.class));
        expectLastCall().anyTimes();
        
        AuthTokenProvider authTokenProvider = new AuthTokenProvider(Providers.of(authDAO));

        resource = new ConfirmInviteController(
            Providers.of(userDAO), authTokenProvider);
    }
    
    @Test
    public void requestWithValidKeyShouldGetView() throws Exception {
                
        Viewable response = resource.getPage( RestMockUtils.mockUriInfo("http://www.activityinfo.org/confirm?xyz123"));
        
        assertThat(response.getModel(), instanceOf(ConfirmInvitePageModel.class));
        
    }

    @Test
    public void badKeyShouldGetProblemPage() throws Exception {

        Viewable response = resource.getPage( RestMockUtils.mockUriInfo("http://www.activityinfo.org/confirm?badkey") );

        assertThat(response.getModel(), instanceOf(InvalidInvitePageModel.class));
    }

    @Test
    public void passwordShouldBeSetAfterNewUserCompletion() throws Exception {

        resource.confirm(RestMockUtils.mockUriInfo("http://www.activityinfo.org/confirm"), 
            VALID_KEY, "fr", "foobar", "Alex Bertram");
        
        assertThat(user.getHashedPassword(), is(not(nullValue())));
        assertThat(user.getLocale(), equalTo("fr"));
        assertThat(user.getChangePasswordKey(), is(nullValue()));
        assertThat(user.getName(), equalTo("Alex Bertram"));
    }

    @Test
    public void emptyPasswordShouldNotBeAccepted() throws Exception {
        Response response = resource.confirm(RestMockUtils.mockUriInfo("http://www.activityinfo.org/confirm"), 
            VALID_KEY, "fr", null, "Alex Bertram");
        
        Viewable viewable = (Viewable) response.getEntity();
        assertThat(viewable.getModel(), instanceOf(ConfirmInvitePageModel.class));

        ConfirmInvitePageModel model = (ConfirmInvitePageModel) viewable.getModel();
        assertTrue("error message set", model.isFormIncomplete());
    }

}
