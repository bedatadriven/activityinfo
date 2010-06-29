/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;

public class UpdateReportDef implements Command<VoidResult> {

	private int id;
	private String newXml;
	
	public UpdateReportDef() {
	
	}
	
	public UpdateReportDef(int id, String newXml) {
		this.id = id;
		this.newXml = newXml;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNewXml() {
		return newXml;
	}

	public void setNewXml(String newXml) {
		this.newXml = newXml;
	}
	

}
