/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface PageStateParser {

    public PageState parse(String token);
}
