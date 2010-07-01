/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Project extends UserDatabase {
	private static final long serialVersionUID = 3838595995254049090L;
	
	@OneToOne
	private LogFrame logFrame;

	public void setLogFrame(LogFrame logFrame) {
		this.logFrame = logFrame;
	}

	public LogFrame getLogFrame() {
		return logFrame;
	}

}
