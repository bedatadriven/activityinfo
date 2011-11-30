package org.sigmah.server.authentication;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import org.sigmah.shared.auth.AuthenticatedUser;
import org.sigmah.shared.dto.AnonymousUser;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ServerSideAuthProvider implements Provider<AuthenticatedUser> {

	private final Provider<HttpServletRequest> request;
	private final Provider<EntityManager> entityManager;
	public static ThreadLocal<AuthenticatedUser> authenticatedUserThreadLocal = new ThreadLocal<AuthenticatedUser>();
	
	@Inject
	public ServerSideAuthProvider(Provider<HttpServletRequest> request,
								  Provider<EntityManager> entityManager) {
		super();
		this.request = request;
		this.entityManager = entityManager;

	}

	@Override
	public AuthenticatedUser get() {
		// first attempt to get authToken from the cookie

		AuthenticatedUser user = null;
		AuthenticatedUser authenticatedUser = authenticatedUserThreadLocal.get();
	
		if(authenticatedUser!=null){
			user = AnonymousUser(authenticatedUser.getAuthToken());
			if(user == null){
				user = authFromToken(authenticatedUser.getAuthToken());
			}
		}
		if(user !=null){
			return user;
		}
		
		throw new IllegalStateException("Request is not authenticated");
	}
	
	private AuthenticatedUser authFromToken(String authToken) {
		org.sigmah.server.database.hibernate.entity.Authentication entity =
				entityManager.get().find(org.sigmah.server.database.hibernate.entity.Authentication.class, authToken);
		
		return new AuthenticatedUser(entity.getUser().getId(), authToken, entity.getUser().getEmail());
	}
	
	private AuthenticatedUser AnonymousUser(String authToken){
		if (authToken.equals(AnonymousUser.AUTHTOKEN)) {
			return new AuthenticatedUser(
					AnonymousUser.USER_ID, AnonymousUser.AUTHTOKEN,
					AnonymousUser.USER_EMAIL);
		}
		return null;
	}

	
}
