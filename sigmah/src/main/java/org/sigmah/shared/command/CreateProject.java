package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;

/**
 * Creates a project instance for the given model.
 * 
 * @author tmi
 * 
 */
public class CreateProject implements Command<VoidResult> {

    private static final long serialVersionUID = -4693262505886999711L;

    /**
     * Project name.
     */
    private String name;

    /**
     * Project full name.
     */
    private String fullName;

    /**
     * Project model id.
     */
    private long modelId;

    /**
     * Project country id.
     */
    private int countryId;

    public CreateProject() {
        // required, or serialization exception
    }

    public CreateProject(String name, String fullName, long modelId, int countryId) {
        this.name = name;
        this.fullName = fullName;
        this.modelId = modelId;
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public long getModelId() {
        return modelId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setModelId(long modelId) {
        this.modelId = modelId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CreateProject other = (CreateProject) obj;
        if (countryId != other.countryId)
            return false;
        if (fullName == null) {
            if (other.fullName != null)
                return false;
        } else if (!fullName.equals(other.fullName))
            return false;
        if (modelId != other.modelId)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + countryId;
        result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
        result = prime * result + (int) (modelId ^ (modelId >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
}
