/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.report.model;

/**
 * @author Alex Bertram
 */
public enum EmailDelivery {

    /**
     * The report is not bound to a time frame
     *
     */
    NONE,

    /**
     *
     * The time frame of the report will be monthly.
     */
    MONTHLY,

    /**
     * The time frame of the report is weekly
     */
    WEEKLY;


}
