package org.sigmah.shared.command;

import org.sigmah.shared.command.result.XmlResult;

/**
 *
 * Returns the XML definition of the {@link org.sigmah.shared.report.model.Report ReportModel} for a given
 * {@link org.sigmah.server.domain.ReportDefinition} database entity.
 *
 * @author Alex Bertram
 */
public class GetReportDef implements Command<XmlResult> {


	private int id;
	
	protected GetReportDef() {
		
	}

    /**
     *
     * @param id The id of the {@link org.sigmah.server.domain.ReportDefinition} database entity for which to return
     *  the XML definition.
     */
	public GetReportDef(int id) {
		this.id = id;
	}

    /**
     *
     * @return The id of the {@link org.sigmah.server.domain.ReportDefinition} database entity for which to return
     *  the XML definition.
     */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
