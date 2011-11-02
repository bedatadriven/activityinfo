package org.sigmah.server.authentication;


public class PasswordHelper {
	public static String hashPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}
}
