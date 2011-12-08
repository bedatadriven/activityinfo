/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.charts;

import org.sigmah.shared.report.model.PivotChartReportElement;

/**
 * Interface to a view of a PivotChartElement
 *
 * @author Alex Bertram
 */
public interface ChartView {
    void setContent(PivotChartReportElement element);
}
