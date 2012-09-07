/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.report.model.Report;


/**
 *
 * Creates a new Report Definition
 *
 * Returns {@link org.activityinfo.shared.command.result.CreateResult}
 *
 * @author Alex Bertram
 */
public class CreateReport implements MutatingCommand<CreateResult>{
	
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
