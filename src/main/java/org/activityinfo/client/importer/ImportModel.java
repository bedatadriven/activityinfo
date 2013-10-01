package org.activityinfo.client.importer;

import java.util.List;

import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.LocationDTO;

import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.EventType;

public class ImportModel extends BaseObservable {
    
    public static final EventType COLUMNS_CHANGED = new EventType();
    
    private ActivityDTO activity;
    private String text = "";
    private ImportData data;
    private List<LocationDTO> locations;
    
    public ActivityDTO getActivity() {
        return activity;
    }

    public void setActivity(ActivityDTO activity) {
        this.activity = activity;
    }

    public void setText(String text) {
        this.text = text;
        if(this.text == null) {
            this.text = "";
        }
    }
    
    public String getText() {
        return text;
    }
    

    public void setData(ImportData data) {
        this.data = data;
    }
    
    public ImportData getData() {
        return data;
    }

    public List<LocationDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationDTO> location) {
        this.locations = location;
    }
}
