/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator;

import org.sigmah.shared.report.content.PivotTableData;

public interface PivotTableAxisCompleter {

	
	void complete(PivotTableData.Axis axis);
	
}
