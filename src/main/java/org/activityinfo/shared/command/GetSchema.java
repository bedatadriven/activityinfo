package org.activityinfo.shared.command;

import org.activityinfo.shared.dto.Schema;



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
