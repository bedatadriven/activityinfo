package org.sigmah.server.auth.impl;


public class PasswordHelper {
	public static String hashPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}
}
