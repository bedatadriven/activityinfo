package org.activityinfo.server.endpoint.odk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.IndicatorDTO;

public class SiteFormData {
    private String xml;

    private String instanceID;
    private int activity;
    private int partner;
    private String locationname;
    private double latitude;
    private double longitude;
    private Date date1;
    private String date1String;
    private Date date2;
    private String date2String;
    private String comments;
    private List<FormIndicator> indicators = new ArrayList<FormIndicator>();
    private List<FormAttributeGroup> attributegroups = new ArrayList<FormAttributeGroup>();
    
    public SiteFormData(String xml) {
        this.xml = xml;
    }

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public void addIndicator(String name, String value) {
        getIndicators().add(new FormIndicator(name, value));
    }

    public void addAttributeGroup(String name, String value) {
        getAttributegroups().add(new FormAttributeGroup(name, value));
    }

    public class FormIndicator {
        private int id;
        private String name;
        private String value;
        private Double doubleValue;

        public FormIndicator(String name, String value) {
            this.name = name;
            this.value = value;

            deriveValues();
        }

        private void deriveValues() {
            this.id = IndicatorDTO.indicatorIdForPropertyName(name);

            try {
                this.doubleValue = Double.valueOf(value);
            } catch (NumberFormatException e) {
                this.doubleValue = null;
            }
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
        
        public boolean hasValue() {
            return value != null && !value.isEmpty();
        }
        
        // value as a double, or null if unparsable
        public Double getDoubleValue() {
            return doubleValue;
        }
    }

    public class FormAttributeGroup {
        private int id;
        private String name;
        private String value;
        private List<Integer> valueList;

        public FormAttributeGroup(String name, String value) {
            this.name = name;
            this.value = value;

            deriveValues();
        }

        private void deriveValues() {
            this.id = AttributeGroupDTO.idForPropertyName(name);

            this.valueList = new ArrayList<Integer>();
            String[] values = value.split("\\s+");
            for (String val : values) {
                try {
                    valueList.add(Integer.valueOf(val));
                } catch (NumberFormatException e) {
                    // just don't add to the list
                }
            }
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public boolean hasValue() {
            return value != null && !value.isEmpty();
        }
        
        // list of all parseable integers in the value (space separated)
        public List<Integer> getValueList() {
            return valueList;
        }

        public boolean isSelected(int attributeId) {
            return valueList.contains(attributeId);
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
            s.append(indicator.getName()).append(": ").append(indicator.getValue()).append(", ");
        }
        if (indicators.size() > 0) {
            s.setLength(s.length() - 2);
        }
        s.append("], attributeGroups: [");
        for (FormAttributeGroup attributegroup : attributegroups) {
            s.append(attributegroup.getName()).append(": ").append(attributegroup.getValue()).append(", ");
        }
        if (attributegroups.size() > 0) {
            s.setLength(s.length() - 2);
        }
        s.append("]}");
        return s.toString();
    }
}
