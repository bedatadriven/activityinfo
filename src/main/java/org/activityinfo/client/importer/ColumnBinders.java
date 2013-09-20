package org.activityinfo.client.importer;

import java.util.List;

public class ColumnBinders {
    
    public static <T> void bind(List<ColumnBinder<T>> binders, String[] row, T model) {
        for(ColumnBinder<T> binder : binders) {
            binder.bind(row, model);
        }
    }
}
