package org.activityinfo.server.bootstrap.model;

/**
 * Represents a Page Redirect - Used for JAX-RS Redirects
 * 
 * @author aldrin
 *
 */
public class Redirect {
	/**
	 * Absolute Page Path
	 */
	final String location;

	public Redirect(String location) {
		super();
		this.location = location;
	}
	
	public String getLocation() {
		return location;
	}
}
