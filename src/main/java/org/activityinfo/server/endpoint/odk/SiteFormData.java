package org.activityinfo.server.endpoint.odk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SiteFormData {
    private String instanceID;
    private int activity;
    private int partner;
    private String locationname;
    private String gps;
    private Date date1;
    private Date date2;
    private String comments;
    private List<FormIndicator> indicators = new ArrayList<FormIndicator>();
    private List<FormAttributeGroup> attributegroups = new ArrayList<FormAttributeGroup>();
    
    public String getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(String instanceID) {
        this.instanceID = instanceID;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public int getPartner() {
        return partner;
    }

    public void setPartner(int partner) {
        this.partner = partner;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public String getLocationname() {
        return locationname;
    }

    public void setLocationname(String locationname) {
        this.locationname = locationname;
    }

    public String getGps() {
        return gps;
    }

    public String[] getGpsValues() {
        return gps.split("\\s+");
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<FormIndicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<FormIndicator> indicators) {
        this.indicators = indicators;
    }

    public List<FormAttributeGroup> getAttributegroups() {
        return attributegroups;
    }

    public void setAttributegroups(List<FormAttributeGroup> attributegroups) {
        this.attributegroups = attributegroups;
    }

    public void addIndicator(int id, String value) {
        getIndicators().add(new FormIndicator(id, value));
    }

    public void addAttributeGroup(int id, String value) {
        getAttributegroups().add(new FormAttributeGroup(id, value));
    }

    public class FormIndicator {
        private int id;
        private String value;

        public FormIndicator(int id, String value) {
            this.id = id;
            this.value = value;
        }

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }
        
        public boolean hasValue() {
            return value != null && !value.isEmpty();
        }
        
        public Double getValueAsDouble() {
            return Double.valueOf(value);
        }
    }

    public class FormAttributeGroup {
        private int id;
        private String value;

        public FormAttributeGroup(int id, String value) {
            this.id = id;
            this.value = value;
        }

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public boolean hasValue() {
            return value != null && !value.isEmpty();
        }
        
        public List<Integer> getValues() {
            String [] values = value.split("\\s+");
            List<Integer> list = new ArrayList<Integer>();
            for (String val : values) {
                list.add(Integer.valueOf(val));
            }
            return list;
        }
    }
}
