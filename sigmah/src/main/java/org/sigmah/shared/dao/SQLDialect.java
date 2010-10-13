/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dao;

/**
 * Utility class that detects and caches required properties of the
 * SQL dialect in use.
 *
 * Note: Obviously hibernate manages SQLDialects as well, but there are some
 * additional database-specific parameters that we need, and this will ultimately
 * be used on the client as well.
 *
 * @author Alex Bertram
 */
public interface SQLDialect {

    /**
     * @param expression a valid SQL expression
     * @return Returns the SQL expression that evaluates to the year of the given  date {@code expression}
     */
    public String yearFunction(String expression);

    /**
     * @param expression a valid SQL expression
     * @return Returns the SQL expression that evaluates to the month of the given date {@code expression}
     */
    public String monthFunction(String month);

    /**
     * @param expression a valid SQL expression
     * @return Returns the SQL expression that evaluates to the quarter of the given date {@code expression}
     */
    public String quarterFunction(String column);

    /**
     *
     * @return true if it possible to disable referential integrity for this
     * database
     */
    boolean isPossibleToDisableReferentialIntegrity();

    /**
     *
     * @param disabled true if the referential integrity checking should be disabled
     * @return  the statement which will disable or renable referential integrity checking
     */
    String disableReferentialIntegrityStatement(boolean disabled);

}
