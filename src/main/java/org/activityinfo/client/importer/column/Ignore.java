package org.activityinfo.client.importer.column;

public class Ignore extends ColumnBinding {

    public static final Ignore INSTANCE = new Ignore();
    
    private Ignore() {
    }
    
    @Override
    public String getLabel() {
        return "Ignore";
    }

    @Override
    public boolean equals(Object other) {
        return other == INSTANCE;
    }

    @Override
    public int hashCode() {
        return 0;
    }
    
    
}
