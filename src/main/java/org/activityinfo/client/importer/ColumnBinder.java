package org.activityinfo.client.importer;


public interface ColumnBinder<T> {

    void bind(String[] row, T model);
   
}
