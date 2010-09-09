/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import org.sigmah.shared.dto.SchemaDTO;


/**
 * Returns a {@link org.sigmah.shared.dto.SchemaDTO} data transfer object that
 * includes the definitions of a databases visible to the authenticated user.
 *
 * @author Alex Bertram
 */
public class GetSchema implements Command<SchemaDTO>, OfflineSupport {


    @Override
    public String toString() {
        return "GetSchema";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        return obj instanceof GetSchema;
    }
}
