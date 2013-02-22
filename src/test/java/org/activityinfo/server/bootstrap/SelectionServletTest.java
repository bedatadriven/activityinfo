package org.activityinfo.server.bootstrap;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URL;

import javax.persistence.EntityManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.easymock.EasyMock;
import org.junit.Test;

import com.google.inject.Provider;

public class SelectionServletTest {

	@Test
	public void markManifestAsObsoleteIfAuthTokenIsBadOrExpired() throws ServletException, IOException {
		
		// this verifies correct behavior in a complicated scenario:
		
		// 1. User authenticates online
		// 2. User agent successfully caches the application
		// 3. User leaves
		// 4. User's auth token is invalidated/expires
		// 5. New version is posted to server
		// 6. User returns to site 
		// 7. RPC call fails due to IncompatibleRemoteServiceException
		//    (so client does not realize that the authToken is no longer valid)
		// 8. Cached client app prompts user for refresh
		// 9. User refreshes
		//10. User agent receives 500 status code while trying to download new manifest (because authToken is invalid)
		//        so user agent does not know there is a new version 
		//11. Goto step 7, repeat infinitely.
		
		
		// To get the CORRECT behavior, the server needs to respond with status code 404
		// to the request for the AppCache manifest, which will result in the cache being 
		// marked as obsolete and should force the user agent to delete the cache, allowing
		// the user to complete a refresh.
		
		EntityManager entityManager = createMock(EntityManager.class);
		expect(entityManager.find(eq(Authentication.class), eq("badtoken"))).andReturn(null);
		replay(entityManager);
		
		@SuppressWarnings("unchecked")
		Provider<EntityManager> provider = createMock(Provider.class);
		expect(provider.get()).andReturn(entityManager);
		replay(provider);
		
		Cookie[] cookies = new Cookie[] { 
				new Cookie(AuthenticatedUser.AUTH_TOKEN_COOKIE, "badtoken")		
		};

		URL permutationMap = getClass().getResource("permutations");
		assertThat("permutationMap is present for test", permutationMap, is(not(nullValue(URL.class))));
		
		ServletContext context = createMock(ServletContext.class);
		expect(context.getRealPath(eq("/ActivityInfo/permutations")))
			.andReturn(permutationMap.getFile());
		replay(context);
		
		ServletConfig config = createMock(ServletConfig.class);
		expect(config.getServletContext()).andReturn(context);
		replay(config);
		

		HttpServletRequest request = createMock(HttpServletRequest.class);
		expect(request.getHeader(eq("User-Agent"))).andReturn("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1 Paros/3.2.13");
		expect(request.getRequestURI()).andReturn("/ActivityInfo/ActivityInfo.appcache");
		expect(request.getCookies()).andReturn(cookies).anyTimes();
		replay(request);
		
		HttpServletResponse response = createMock(HttpServletResponse.class);
		response.sendError(eq(404), EasyMock.<String>anyObject());
		expectLastCall();
		replay(response);
				
		SelectionServlet servlet = new SelectionServlet(provider);
		servlet.testInit(config);
		servlet.testGet(request, response);
		
		verify(response);	
	}
}
