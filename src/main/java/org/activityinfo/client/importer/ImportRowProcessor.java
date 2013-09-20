package org.activityinfo.client.importer;

public interface ImportRowProcessor {
    
    public void process(int rowIndex, String[] columns);

}
