/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.report.view;

import org.activityinfo.shared.report.model.PivotChartReportElement;

/**
 * Interface to a view of a PivotChartElement
 *
 * @author Alex Bertram
 */
public interface ChartView extends ReportView<PivotChartReportElement> {
    void show(PivotChartReportElement element);
}
