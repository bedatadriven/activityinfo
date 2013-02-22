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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.activityinfo.shared.command.result.AdminEntityResult;
import org.activityinfo.shared.command.result.BaseMapResult;
import org.activityinfo.shared.command.result.ListResult;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.command.result.UserResult;
import org.activityinfo.shared.map.TileBaseMap;
import org.activityinfo.shared.util.mapping.Extents;

import com.google.common.collect.Lists;

/**
 * Predefined DTOs for use in unit tests.
 * 
 */
public class DTOs {

    public static final AdminLevelDTO PROVINCE = new AdminLevelDTO(1,
        "Province");
    public static final AdminLevelDTO TERRITOIRE = new AdminLevelDTO(2, 1,
        "Territoire");
    public static final AdminLevelDTO SECTEUR = new AdminLevelDTO(3, 2,
        "Secteur");

    public static final LocationTypeDTO ECOLE = new LocationTypeDTO(2, "Ecole");
    public static final LocationTypeDTO LOCALITE = new LocationTypeDTO(1,
        "Localite");

    public static final AdminEntityDTO NORD_KIVU = rootEntity()
        .atLevel(PROVINCE).named("North Kivu")
        .withBounds(Extents.create(0, 0, 100, 100)).build();

    public static final AdminEntityDTO BENI = childOf(NORD_KIVU)
        .atLevel(TERRITOIRE).named("Beni")
        .withBounds(Extents.create(0, 0, 25, 25)).build();

    public static final AdminEntityDTO WATALINA = childOf(BENI)
        .atLevel(SECTEUR).named("Watalina")
        .build();

    public static final AdminEntityDTO RUIZI = childOf(BENI).atLevel(SECTEUR)
        .named("Ruizi")
        .build();

    public static final AdminEntityDTO MASISI = childOf(NORD_KIVU)
        .atLevel(TERRITOIRE).named("Masisi")
        .withBounds(Extents.create(0, 25, 25, 50)).build();

    public static final AdminEntityResult NORD_KIVU_TERRITOIRES = new AdminEntityResult(
        Lists.newArrayList(BENI, MASISI));

    public static final AdminEntityDTO SUD_KIVU = rootEntity().atLevel(PROVINCE)
        .named("Sud Kivu")
        .withBounds(Extents.create(0, 0, -100, -100)).build();

    public static final AdminEntityDTO SHABUNDA = childOf(SUD_KIVU)
        .atLevel(TERRITOIRE).named("Shabunda")
        .build();

    public static final AdminEntityResult SUD_KIVU_TERRITOIRES = new AdminEntityResult(
        Lists.newArrayList(SHABUNDA));

    public static final CountryDTO DRC;

    public static final PartnerDTO NRC = new PartnerDTO(88, "NRC");

    public static final AdminEntityResult PROVINCES = new AdminEntityResult(
        Lists.newArrayList(NORD_KIVU, SUD_KIVU));

    static {
        DRC = new CountryDTO(1, "RDC");
        DRC.setBounds(Extents.create(0, 0, 300, 300));
        DRC.setAdminLevels(Arrays.asList(PROVINCE, TERRITOIRE, SECTEUR));
        DRC.setLocationTypes(Arrays.asList(LOCALITE, ECOLE));
    }

    private static int nextId = 1;

    public static class PEAR {

        public static final SchemaDTO SCHEMA;
        public static final UserDatabaseDTO DATABASE;
        public static final ActivityDTO NFI_DISTRIBUTION;
        public static final ActivityDTO SCHOOL_REHAB;
        public static final AttributeDTO MINOR;
        public static final AttributeDTO MAJOR;
        public static final AttributeGroupDTO REHAB_TYPE;
        public static final SiteDTO WATALINA_CENTER_IN_BENI;
        public static final SiteDTO SITE_WITH_NO_ADMIN_LEVELS;

        public static final List<SiteDTO> SITES;

        static {

            DATABASE = new UserDatabaseDTO(1, "PEAR");
            DATABASE.setFullName("Program of Expanded Assistance to Refugees");
            DATABASE.setEditAllAllowed(false);
            DATABASE.setEditAllowed(true);
            DATABASE.setManageUsersAllowed(true);
            DATABASE.setManageAllUsersAllowed(false);
            DATABASE.setDesignAllowed(true);
            DATABASE.setMyPartnerId(88);
            DATABASE.setCountry(DRC);

            DATABASE.getPartners().add(NRC);

            NFI_DISTRIBUTION = new ActivityDTO(91, "NFI Distributions");
            NFI_DISTRIBUTION.setDatabase(DATABASE);
            NFI_DISTRIBUTION.setLocationTypeId(LOCALITE.getId());
            DATABASE.getActivities().add(NFI_DISTRIBUTION);

            SCHOOL_REHAB = new ActivityDTO(92, "School Rehab");
            SCHOOL_REHAB.setDatabase(DATABASE);
            SCHOOL_REHAB.setLocationTypeId(ECOLE.getId());
            DATABASE.getActivities().add(SCHOOL_REHAB);

            REHAB_TYPE = new AttributeGroupDTO(71);
            REHAB_TYPE.setName("Rehab type");
            REHAB_TYPE.setMultipleAllowed(false);
            SCHOOL_REHAB.getAttributeGroups().add(REHAB_TYPE);

            MINOR = new AttributeDTO(711, "Minor");
            REHAB_TYPE.getAttributes().add(MINOR);

            MAJOR = new AttributeDTO(712, "Major");
            REHAB_TYPE.getAttributes().add(MAJOR);

            SCHEMA = new SchemaDTO();
            SCHEMA.getDatabases().add(DATABASE);

            WATALINA_CENTER_IN_BENI = new SiteDTO(4);
            WATALINA_CENTER_IN_BENI.setActivityId(NFI_DISTRIBUTION.getId());
            WATALINA_CENTER_IN_BENI.setPartner(NRC);
            WATALINA_CENTER_IN_BENI
                .setLocationName("Watalina Center (in Beni)");
            WATALINA_CENTER_IN_BENI.setAdminEntity(1, NORD_KIVU);
            WATALINA_CENTER_IN_BENI.setAdminEntity(2, BENI);
            WATALINA_CENTER_IN_BENI.setAdminEntity(3, WATALINA);

            SITE_WITH_NO_ADMIN_LEVELS = new SiteDTO(5);
            SITE_WITH_NO_ADMIN_LEVELS.setActivityId(NFI_DISTRIBUTION.getId());
            SITE_WITH_NO_ADMIN_LEVELS.setPartner(new PartnerDTO(89, "AVSI"));
            SITE_WITH_NO_ADMIN_LEVELS.setLocationName("Boise Idahao");

            SITES = Lists.newArrayList();
            SITES.add(WATALINA_CENTER_IN_BENI);
        }
    }

    /**
     * 
     * @return A schema with one program "PEAR" with two activities (ids=91,92)
     */
    public static SchemaDTO pear() {
        return PEAR.SCHEMA;
    }

    public static UserResult rrmUsers() {
        List<UserPermissionDTO> users = new ArrayList<UserPermissionDTO>();

        UserPermissionDTO typhaine = new UserPermissionDTO();
        typhaine.setName("typhaine");
        typhaine.setEmail("typhaine@sol.net");
        typhaine.setAllowView(true);
        typhaine.setAllowManageUsers(true);
        typhaine.setAllowManageAllUsers(false);

        users.add(typhaine);

        return new UserResult(users);
    }

    /**
     * 
     * @return A schema with two programs, "PEAR" and "RRM" where "PEAR" has no
     *         activities and "RRM" has one activity.
     */
    public static SchemaDTO emptyPEARandRRM() {
        SchemaDTO schema = new SchemaDTO();
        schema.getDatabases().add(new UserDatabaseDTO(1, "PEAR"));

        UserDatabaseDTO rrm = new UserDatabaseDTO(2, "RRM");
        rrm.getActivities().add(new ActivityDTO(1, "NFI Distribution"));

        schema.getDatabases().add(rrm);

        return schema;
    }

    /**
     * 
     * @return A map of two NFI Distribution sites (activityId=91) sites indexed
     *         by id Site ids = 4, 5
     */
    public static Map<Integer, SiteDTO> pearSites() {
        throw new UnsupportedOperationException();

    }

    public static SiteResult pearSitesManyResults(int count) {
        List<SiteDTO> sites = new ArrayList<SiteDTO>();
        for (int i = 0; i != count; ++i) {
            SiteDTO fargo = new SiteDTO(i);
            fargo.setActivityId(91);
            fargo.setPartner(new PartnerDTO(88, "NRC"));
            fargo.setLocationName("Fargo");
            fargo.setAdminEntity(3, WATALINA);
            fargo.setAdminEntity(2, BENI);
            fargo.setAdminEntity(1, NORD_KIVU);
            sites.add(fargo);
        }
        return new SiteResult(sites);
    }

    public static SiteResult pearSitesResults() {
        return new SiteResult(new ArrayList<SiteDTO>(pearSites().values()));
    }

    public static ListResult<AdminEntityDTO> getProvinces() {
        List<AdminEntityDTO> list = new ArrayList<AdminEntityDTO>();
        list.add(NORD_KIVU);
        list.add(SUD_KIVU);

        return new AdminEntityResult(list);
    }

    public static ListResult<AdminEntityDTO> getTerritoires(int provinceId) {

        List<AdminEntityDTO> list = new ArrayList<AdminEntityDTO>();

        if (provinceId == 100) {
            list.add(BENI);
            list.add(MASISI);
        } else if (provinceId == 200) {
            list.add(new AdminEntityDTO(2, 202, 200, "Shabunda"));
            list.add(new AdminEntityDTO(2, 201, 200, "Walungu"));
        }

        return new AdminEntityResult(list);
    }

    public static SchemaDTO PEARPlus() {

        CountryDTO country = new CountryDTO(1, "RDC");
        country.getAdminLevels().add(PROVINCE);
        country.getAdminLevels().add(new AdminLevelDTO(2, 1, "Zone de Sante"));
        country.getAdminLevels().add(new AdminLevelDTO(3, 2, "Aire de Sante"));

        LocationTypeDTO aireSante = new LocationTypeDTO(1, "Aire de Sante");
        aireSante.setBoundAdminLevelId(3);
        country.getLocationTypes().add(aireSante);

        SchemaDTO schema = new SchemaDTO();
        UserDatabaseDTO pearPlus = new UserDatabaseDTO(1, "PEAR Plus");
        pearPlus.setCountry(country);
        pearPlus.setEditAllAllowed(true);
        pearPlus.setEditAllowed(true);

        schema.getDatabases().add(pearPlus);

        ActivityDTO activity = new ActivityDTO(11, "Reunificiation des Enfants");
        activity.setDatabase(pearPlus);
        activity.setLocationTypeId(1);
        activity.setReportingFrequency(ActivityDTO.REPORT_MONTHLY);
        pearPlus.getActivities().add(activity);

        return schema;
    }

    public static AdminEntityResult pearPlusProvinces() {
        List<AdminEntityDTO> list = new ArrayList<AdminEntityDTO>();
        list.add(new AdminEntityDTO(1, 1, "Ituri"));
        return new AdminEntityResult(list);
    }

    public static AdminEntityResult pearPlusZonesSantes() {
        List<AdminEntityDTO> list = new ArrayList<AdminEntityDTO>();
        list.add(new AdminEntityDTO(2, 11, 1, "Banana"));
        list.add(new AdminEntityDTO(2, 12, 1, "Drodo"));
        return new AdminEntityResult(list);
    }

    public static AdminEntityResult pearPlusAS() {
        List<AdminEntityDTO> list = new ArrayList<AdminEntityDTO>();
        list.add(new AdminEntityDTO(3, 111, 11, "Logo"));
        list.add(new AdminEntityDTO(3, 112, 11, "Ndikpa"));
        list.add(new AdminEntityDTO(3, 113, 11, "Zengo"));
        return new AdminEntityResult(list);
    }

    public static BaseMapResult baseMaps() {

        TileBaseMap map = new TileBaseMap();
        map.setName("Administrative Map");
        map.setCopyright("Foobar");
        map.setId("admin");
        map.setMinZoom(0);
        map.setMaxZoom(16);

        return new BaseMapResult(Collections.singletonList(map));
    }

    public static EntityBuilder childOf(AdminEntityDTO parent) {
        return new EntityBuilder(parent);
    }

    public static EntityBuilder rootEntity() {
        return new EntityBuilder();
    }

    private static class EntityBuilder {
        private AdminEntityDTO entity;

        public EntityBuilder() {
            entity = new AdminEntityDTO();
            entity.setId(nextId++);
        }

        public EntityBuilder(AdminEntityDTO parent) {
            this();
            entity.setParentId(parent.getId());
        }

        public EntityBuilder named(String name) {
            entity.setName(name);
            return this;
        }

        public EntityBuilder withBounds(Extents bounds) {
            entity.setBounds(bounds);
            return this;
        }

        public EntityBuilder atLevel(AdminLevelDTO level) {
            entity.setLevelId(level.getId());
            return this;
        }

        public AdminEntityDTO build() {
            return entity;
        }
    }
}
