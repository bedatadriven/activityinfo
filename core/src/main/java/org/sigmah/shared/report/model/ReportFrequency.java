/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model;

/**
 * @author Alex Bertram
 */
public enum ReportFrequency {

    /**
     * The report is not bound to a time frame
     *
     */
    NotDateBound,

    /**
     *
     * The time frame of the report will be monthly.
     */
    Monthly,

    /**
     * The time frame of the report is weekly
     */
    Weekly,

    /**
     * The time frame of the report is daily
     */
    Daily,

    /**
     * The time frame of the report is to be defined by an arbitrary
     * date range.
     *
     * (These types of reports cannot be "subscribed" to)
     */
    Adhoc;

}
