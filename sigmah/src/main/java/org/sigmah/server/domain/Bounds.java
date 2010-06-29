/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * @author Alex Bertram
 *
 */
@Embeddable
public class Bounds {

	private double x1;
	private double y1;
	private double x2;
	private double y2;
	
	public Bounds() {
		
	}
	
	@Column
	public double getX1() {
		return x1;
	}
	
	public void setX1(double x1) {
		this.x1 = x1;
	}
	
	@Column
	public double getY1() {
		return y1;
	}

	public void setY1(double y1) {
		this.y1 = y1;
	}
	
	@Column
	public double getX2() {
		return x2;
	}
	
	public void setX2(double x2) {
		this.x2 = x2;
	}
	
	@Column
	public double getY2() {
		return y2;
	}
	
	public void setY2(double y2) {
		this.y2 = y2;
	}
	
}
