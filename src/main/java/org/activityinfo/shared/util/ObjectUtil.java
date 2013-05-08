package org.activityinfo.shared.util;

public class ObjectUtil {
    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if ((object1 == null) || (object2 == null)) {
            return false;
        }
        return object1.equals(object2);
    }

    public static boolean notEquals(Object object1, Object object2) {
        return !equals(object1, object2);
    }
}
