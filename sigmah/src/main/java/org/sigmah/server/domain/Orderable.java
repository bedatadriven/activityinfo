/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.domain;

public interface Orderable {

	public int getSortOrder();
	
	public void setSortOrder(int sortOrder);
}
