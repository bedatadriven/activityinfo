package org.activityinfo.server.endpoint.odk;

import java.math.BigDecimal;
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
    private String date1String;
    private Date date2;
    private String date2String;
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

    public String getDate1String() {
        return date1String;
    }

    public void setDate1String(String date1String) {
        this.date1String = date1String;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public String getDate2String() {
        return date2String;
    }

    public void setDate2String(String date2String) {
        this.date2String = date2String;
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

    public void setGps(String gps) {
        this.gps = gps;
    }

    public BigDecimal getLatitude() {
        return parseGps(0);
    }

    public BigDecimal getLongitude() {
        return parseGps(1);
    }

    private BigDecimal parseGps(int ix) {
        BigDecimal result = null;
        try {
            String[] coords = gps.split("\\s+");
            if (coords.length >= 2) {
                result = new BigDecimal(coords[ix]);
            }
        } catch (Exception e) {
            // just return null;
        }
        return result;
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

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("{");
        s.append("instanceID: ").append(instanceID);
        s.append(", partner: ").append(partner);
        s.append(", locationname: ").append(locationname);
        s.append(", latitude: ").append(getLatitude());
        s.append(", longitude: ").append(getLongitude());
        s.append(", date1: ").append(date1String);
        s.append(", date2: ").append(date2String);
        s.append(", comments: ").append(comments);
        s.append(", indicators: [");
        for (FormIndicator indicator : indicators) {
            s.append(indicator.getId()).append(": ").append(indicator.getValue()).append(", ");
        }
        if (indicators.size() > 0) {
            s.setLength(s.length() - 2);
        }
        s.append("], attributeGroups: [");
        for (FormAttributeGroup attributegroup : attributegroups) {
            s.append(attributegroup.getId()).append(": ").append(attributegroup.getValue()).append(", ");
        }
        if (attributegroups.size() > 0) {
            s.setLength(s.length() - 2);
        }
        s.append("]}");
        return s.toString();
    }
}
