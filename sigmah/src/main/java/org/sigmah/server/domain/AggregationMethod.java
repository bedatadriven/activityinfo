/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.domain;

public enum AggregationMethod {

	Sum(0),
	Average(1),
	SiteCount(2);
	
	private final int code;
	
	private AggregationMethod(int code) {
		this.code = code;
	}
	
	public final Integer code() {
		return code;
	}
	
}
