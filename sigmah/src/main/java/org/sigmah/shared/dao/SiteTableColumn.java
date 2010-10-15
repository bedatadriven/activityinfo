/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dao;



/**  
 * 
 * Names of columns that can be used to create a Criterion for 
 * {@link SiteTableDAO}
 * 
* @author Alex Bertram (akbertram@gmail.com)
*/
public enum SiteTableColumn {

    id(1, "site.SiteId"),
    activity_id(2, "activity.ActivityId"),
    activity_name(3, "activity.name"),
    database_id(4, "UserDatabase.DatabaseId"),
    database_name(5, "UserDatabase.name"),
    date1(6, "site.date1"),
    date2(7, "site.date2"),
    status(8, "site.status"),
    partner_id(9, "partner.PartnerId"),
    partner_name(10, "partner.name"),
    location_name(11, "location.name"),
    location_axe(12, "location.axe"),
    location_type(13, "locationType.name"),
    comments(14, "site.comments"),
    x(15, "location.x"),
    y(16, "location.y");

    private final String property;
    private final int index;

    SiteTableColumn(int index, String property) {
        this.index = index;
        this.property = property;
    }

    public int index() {
        return index;
    }

    public String alias() {
        return toString();
    }

    public String property() {
        return property;
    }
}
