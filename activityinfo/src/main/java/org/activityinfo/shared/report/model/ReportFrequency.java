/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.shared.report.model;

/**
 * @author Alex Bertram
 */
public class ReportFrequency {

    /**
     * The report is not bound to a time frame
     *
     */
    public static final int NOT_DATE_BOUND = 0;                             

    /**
     *
     * The time frame of the report will be monthly.
     */
    public static final int MONTHLY = 1;

    /**
     * The time frame of the report is weekly
     */
    public static final int WEEKLY = 2;

    /**
     * The time frame of the report is daily
     */
    public static int DAILY = 4;

    /**
     * The time frame of the report is to be defined by an arbitrary
     * date range.
     *
     * (These types of reports cannot be "subscribed" to)
     */
    public static final int ADHOC = 3;

    public static final int LAST_DAY_OF_MONTH = 28;
}
