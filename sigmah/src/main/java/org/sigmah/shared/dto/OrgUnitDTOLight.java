package org.sigmah.shared.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;

public class OrgUnitDTOLight extends BaseTreeModel implements EntityDTO {

    private static final long serialVersionUID = -1821740105271321253L;

    @Override
    public String getEntityName() {
        return "OrgUnit";
    }

    // Id
    @Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    // Name
    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }
    
    // Title
    public String getTitle() {
        return get("title");
    }

    public void setTitle(String title) {
        set("title", title);
    }

    // Full name
    public String getFullName() {
        return get("fullName");
    }

    public void setFullName(String fullName) {
        set("fullName", fullName);
    }

    public void generateCompleteName() {
        set("completeName", getName() + " - " + getFullName());
    }

    // Parent
    public OrgUnitDTOLight getParentDTO() {
        return get("parent");
    }

    public void setParentDTO(OrgUnitDTOLight parent) {
        set("parent", parent);
        setParent(parent);
    }

    // Children
    public List<OrgUnitDTOLight> getChildrenDTO() {
        return get("children");
    }

    // Country
    public CountryDTO getOfficeLocationCountry() {
        return get("country");
    }

    public void setOfficeLocationCountry(CountryDTO officeLocationCountry) {
        set("country", officeLocationCountry);
    }

    // Can contain projects
    public Boolean getCanContainProjects() {
        final Boolean b = (Boolean) get("canContainProjects");
        if (b == null) {
            return false;
        } else {
            return b;
        }
    }

    public void setCanContainProjects(Boolean canContainProjects) {
        set("canContainProjects", canContainProjects);
    }

    public void setChildrenDTO(Set<OrgUnitDTOLight> children) {

        final Comparator<ModelData> comp = new Comparator<ModelData>() {
            @Override
            public int compare(ModelData o1, ModelData o2) {
                return ((String) o1.get("name")).compareToIgnoreCase((String) o2.get("name"));
            }
        };

        final ArrayList<ModelData> l = new ArrayList<ModelData>(children);
        Collections.sort(l, comp);
        setChildren(l);

        set("children", l);
    }
}
