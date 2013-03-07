package org.activityinfo.shared.dto;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.common.collect.Sets;

/**
 * Aggregate DTO for all
 * {@link org.activityinfo.server.database.hibernate.entity.UserDatabase}s
 * visible to the client, along with the UserDatabase's
 * {@link org.activityinfo.server.database.hibernate.entity.Country Country},
 * and {@link org.activityinfo.server.database.hibernate.entity.Attribute} and
 * {@link org.activityinfo.server.database.hibernate.entity.Indicator}
 * 
 * @author Alex Bertram
 */
public final class SchemaDTO extends BaseModelData implements DTO {

    private long version;
    private List<UserDatabaseDTO> databases = new ArrayList<UserDatabaseDTO>(0);
    private List<CountryDTO> countries = new ArrayList<CountryDTO>(0);

    public SchemaDTO() {
    }

    /**
     * Gets the version number of this schema. This number can be used to check
     * for updates on the server.
     * 
     * @return the version number of this schema
     */
    public long getVersion() {
        return version;
    }

    /**
     * Sets the version number of the schema.
     * 
     * @param version
     *            a numeric version identifier
     */
    public void setVersion(long version) {
        this.version = version;
    }

    public List<UserDatabaseDTO> getDatabases()
    {
        return databases;
    }

    public Set<CountryDTO> getCountriesForIndicators(Set<Integer> indicatorIds) {
        Set<CountryDTO> countries = Sets.newHashSet();
        for (UserDatabaseDTO db : getDatabases()) {
            for (ActivityDTO activity : db.getActivities()) {
                if (activity.containsAny(indicatorIds)) {
                    countries.add(db.getCountry());
                }
            }
        }
        return countries;
    }

    public void setDatabases(List<UserDatabaseDTO> databases) {
        this.databases = databases;
    }

    public ActivityDTO getActivityById(int id) {
        for (UserDatabaseDTO database : databases) {
            ActivityDTO activity = getById(database.getActivities(), id);
            if (activity != null) {
                return activity;
            }
        }
        return null;
    }

    public PartnerDTO getPartnerById(int partnerId) {
        for (UserDatabaseDTO database : databases) {
            PartnerDTO partner = getById(database.getPartners(), partnerId);
            if (partner != null) {
                return partner;
            }
        }
        return null;
    }

    public ProjectDTO getProjectById(int projectId) {
        for (UserDatabaseDTO database : databases) {
            ProjectDTO project = getById(database.getProjects(), projectId);
            if (project != null) {
                return project;
            }
        }
        return null;
    }

    public CountryDTO getCountryById(int countryId) {
        return getById(countries, countryId);
    }

    /**
     * Finds a database in this schema by id.
     * 
     * @param id
     *            The database id
     * @return The database corresponding to this id, or null if none exists.
     */
    public UserDatabaseDTO getDatabaseById(int id) {
        for (UserDatabaseDTO database : databases) {
            if (database.getId() == id) {
                return database;
            }
        }
        return null;
    }

    /**
     * 
     * Helper function to search a list of <code>ModelData</code> for a model
     * with that has a value of <code>id</code> for the property "id"
     * 
     * @param list
     *            The list of <code>ModelData</code> to search
     * @param id
     *            The id for which to search
     * @param <T>
     *            The <code>ModelData</code> subclass
     * @return The corresponding <code>ModelData</code>, or null if none was
     *         found.
     */
    public static <T extends ModelData> T getById(List<T> list, int id) {
        for (T m : list) {
            Integer mId = m.get("id");
            if (mId != null && mId.equals(id)) {
                return m;
            }
        }
        return null;
    }

    public List<CountryDTO> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryDTO> countries) {
        this.countries = countries;
    }

    public AdminLevelDTO getAdminLevelById(int parentLevelId) {
        for (CountryDTO country : countries) {
            AdminLevelDTO level = country.getAdminLevelById(parentLevelId);
            if (level != null) {
                return level;
            }
        }
        return null;
    }

    public CountryDTO getCountryByAdminLevelId(Integer adminLevelId) {
        AdminLevelDTO adminLevel = getAdminLevelById(adminLevelId);

        if (adminLevel != null && countries != null) {
            return getCountryById(adminLevel.getCountryId());
        }

        return null;
    }

    public ActivityDTO getActivityByIndicatorId(int id) {
        for (UserDatabaseDTO db : databases) {
            for (ActivityDTO act : db.getActivities()) {
                for (IndicatorDTO ind : act.getIndicators()) {
                    if (ind.getId() == id) {
                        return act;
                    }
                }
            }
        }
        return null;
    }

    public IndicatorDTO getIndicatorById(int id) {
        for (UserDatabaseDTO db : databases) {

            for (ActivityDTO act : db.getActivities()) {
                for (IndicatorDTO ind : act.getIndicators()) {
                    if (ind.getId() == id) {
                        return ind;
                    }
                }
            }

        }
        return null;
    }

    public Set<PartnerDTO> getVisiblePartners() {

        Set<PartnerDTO> partners = new HashSet<PartnerDTO>();

        for (UserDatabaseDTO database : getDatabases()) {
            partners.addAll(database.getPartners());
        }

        return partners;
    }

    public List<PartnerDTO> getVisiblePartnersList() {
        List<PartnerDTO> list = new ArrayList<PartnerDTO>();
        list.addAll(getVisiblePartners());
        return list;
    }

    public ActivityDTO getFirstActivity() {
        for (UserDatabaseDTO database : getDatabases()) {

            for (ActivityDTO activity : database.getActivities()) {
                return activity;
            }
        }
        return null;
    }

    public AttributeGroupDTO getAttributeGroupById(int attributeGroupId) {
        for (UserDatabaseDTO db : getDatabases()) {
            for (ActivityDTO activity : db.getActivities()) {
                AttributeGroupDTO group = activity
                    .getAttributeGroupById(attributeGroupId);
                if (group != null) {
                    return group;
                }
            }
        }
        return null;
    }
    
    /**
     * Retrieves the name of the AttributeGroup from the Schema graph, 
     * or returns "" if the attribute group cannot be found in the 
     * loaded schema.
     *
     */
    public String getAttributeGroupNameSafe(int attributeGroupId) {
        AttributeGroupDTO group = getAttributeGroupById(attributeGroupId);
        if (group == null) {
            return "";
        } else {
            return group.getName();
        }
    }

    public AttributeDTO getAttributeById(int attributeId) {
        for (UserDatabaseDTO db : getDatabases()) {
            for (ActivityDTO activity : db.getActivities()) {
                AttributeDTO attr = activity.getAttributeById(attributeId);
                if (attr != null) {
                    return attr;
                }
            }
        }
        return null;
    }
}
