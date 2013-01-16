package org.activityinfo.shared.util;

import java.util.Collection;

public class CollectionUtil {
	public static boolean isEmpty(Collection<?> col) {
		return col == null || col.size() == 0;
	}

	public static boolean isNotEmpty(Collection<?> col) {
		return col != null && col.size() > 0;
	}
}
