/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page;

/**
 * Uniquely identifies a page within the application.
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PageId {
    private final String id;

    public PageId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
