/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.report.model.Report;


/**
 *
 * Creates a new Report Definition
 *
 * Returns {@link org.sigmah.shared.command.result.CreateResult}
 *
 * @author Alex Bertram
 */
public class CreateReport implements Command<CreateResult>{
	
	private Integer databaseId;
	private Report report;

	protected CreateReport() {
		
	}
	
	public CreateReport(Report report){
		super();
		this.databaseId = null;
		this.report = report;
	}


	public Integer getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(Integer databaseId) {
		this.databaseId = databaseId;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

}
