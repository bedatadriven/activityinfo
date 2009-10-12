package org.activityinfo.shared.command;

import org.activityinfo.shared.dto.Schema;


/**
 * Returns a {@link org.activityinfo.shared.dto.Schema} data transfer object that
 * includes the definitions of a databases visible to the authenticated user.
 *
 * @author Alex Bertram
 */
public class GetSchema implements Command<Schema> {


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
