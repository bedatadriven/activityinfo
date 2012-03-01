package org.sigmah.server.authentication;

import java.io.IOException;
import java.nio.charset.Charset;
import javax.persistence.NoResultException;
import org.apache.commons.codec.binary.Base64;
import org.sigmah.server.database.hibernate.dao.UserDAO;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.auth.AuthenticatedUser;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class BasicAuthentication {

	private final ServerSideAuthProvider authProvider;
	private final Provider<UserDAO> userDAO;
	private final Provider<Authenticator> authenticator;
	
	@Inject
	public BasicAuthentication(
			 ServerSideAuthProvider authProvider,
			 Provider<UserDAO> userDAO,
			 Provider<Authenticator> authenticator) {
		this.authProvider = authProvider;
		this.userDAO = userDAO;
		this.authenticator = authenticator;
	}

	public User doAuthentication(String auth) throws IOException{
		
		User user = authenticate(auth);
		
		if(user == null){
			return null;
		}
		
		authProvider.set(new AuthenticatedUser("", user.getId(), user.getEmail()));
		
		return user;
	}
	
	// This method checks the user information sent in the Authorization
    // header against the database of users maintained in the users Hashtable.

    private User authenticate(String auth) throws IOException {
        if (auth == null) {
            return null;
        }// no auth

        if (!auth.toUpperCase().startsWith("BASIC ")) {
            return null;
        }// we only do BASIC

        // Get encoded user and password, comes after "BASIC "
        String emailpassEncoded = auth.substring(6);

        // Decode it, using any base 64 decoder

        byte[] emailpassDecodedBytes = Base64.decodeBase64(emailpassEncoded.getBytes());
        String emailpassDecoded = new String(emailpassDecodedBytes, Charset.defaultCharset());
        String[] emailPass = emailpassDecoded.split(":");

        if (emailPass.length != 2) {
            return null;
        }

        // look up the user in the database
        User user = null; 
        try {
        	user = userDAO.get().findUserByEmail(emailPass[0]);
        } catch (NoResultException e) {
        	return null;
        }
      
        if (!authenticator.get().check(user, emailPass[1])) {
            return null;
        }
        return user;

    }
}
