/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.mock;

import org.sigmah.shared.command.result.*;
import org.sigmah.shared.dto.*;
import org.sigmah.shared.map.BaseMap;
import org.sigmah.shared.map.LocalBaseMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyData {


    public static final AdminEntityDTO Beni = new AdminEntityDTO(2, 101, 100, "Beni", new BoundingBoxDTO(0, 0, 25, 25));
    public static final AdminEntityDTO Masisi = new AdminEntityDTO(2, 102, 100, "Masisi", new BoundingBoxDTO(0, 25, 25, 50));
    public static final AdminEntityDTO NordKivu = new AdminEntityDTO(1, 100, "Nord Kivu", new BoundingBoxDTO(0, 0, 100, 100));
    public static final AdminEntityDTO SudKivu = new AdminEntityDTO(1, 200, "Sud Kivu", new BoundingBoxDTO(0, 0, -100, -100));
    public static final AdminEntityDTO Watalina = new AdminEntityDTO(3, 1011, Beni.getParentId(), "Watalinga");

    /**
     *
     * @return A schema with one program "PEAR" with two activities (ids=91,92)
     */
	public static SchemaDTO PEAR() {

        CountryDTO country = new CountryDTO(1, "RDC");
        country.setBounds(new BoundingBoxDTO(0, 0, 300, 300));
        country.getAdminLevels().add(new AdminLevelDTO(1, "Province"));
        country.getAdminLevels().add(new AdminLevelDTO(2, 1, "Territoire"));
        country.getAdminLevels().add(new AdminLevelDTO(3, 2, "Secteur"));
        country.getLocationTypes().add(new LocationTypeDTO(1, "Localite"));
        country.getLocationTypes().add(new LocationTypeDTO(2, "Ecole"));

        UserDatabaseDTO pear = new UserDatabaseDTO(1, "PEAR");
        pear.setFullName("Program of Expanded Assistance to Refugees");
        pear.setEditAllAllowed(false);
        pear.setEditAllowed(true);
        pear.setManageUsersAllowed(true);
        pear.setManageAllUsersAllowed(false);
        pear.setDesignAllowed(true);
        pear.setMyPartnerId(88);
        pear.setCountry(country);

        pear.getPartners().add(new PartnerDTO(88, "NRC"));

        ActivityDTO nfiDistro = new ActivityDTO(91, "NFI Distributions");
        nfiDistro.setDatabase(pear);
        nfiDistro.setLocationTypeId(1);
        pear.getActivities().add(nfiDistro);

        ActivityDTO schoolRehab = new ActivityDTO(92, "School Rehab");
        schoolRehab.setDatabase(pear);
        schoolRehab.setLocationTypeId(2);
        pear.getActivities().add(schoolRehab);

        AttributeGroupDTO rehabType = new AttributeGroupDTO(71);
        rehabType.setName("Rehab type");
        rehabType.setMultipleAllowed(false);
        schoolRehab.getAttributeGroups().add(rehabType);
        
        AttributeDTO minor = new AttributeDTO(711, "Minor");
        rehabType.getAttributes().add(minor);

        AttributeDTO major = new AttributeDTO(712, "Major");
        rehabType.getAttributes().add(major);


        final SchemaDTO schema = new SchemaDTO();
        schema.getDatabases().add(pear);
        return schema;
	}

    public static UserResult RRM_Users() {
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
     * @return A schema with two programs, "PEAR" and "RRM" where "PEAR" has
     * no activities and "RRM" has one activity.
     */
    public static SchemaDTO EmptyPEARandRRM() {
        SchemaDTO schema = new SchemaDTO();
        schema.getDatabases().add(new UserDatabaseDTO(1, "PEAR"));

        UserDatabaseDTO rrm = new UserDatabaseDTO(2, "RRM");
        rrm.getActivities().add(new ActivityDTO(1, "NFI Distribution"));

        schema.getDatabases().add(rrm);

        return schema;
    }

    /**
     *
     * @return A map of two NFI Distribution sites (activityId=91) sites indexed by id
     * Site ids = 4, 5
     */
    public static Map<Integer, SiteDTO> PEAR_Sites() {

        final SiteDTO fargo = new SiteDTO(4);
        fargo.setActivityId(91);
        fargo.setPartner(new PartnerDTO(88, "NRC"));
        fargo.setLocationName("Fargo");
        fargo.setAdminEntity(3, Watalina);
        fargo.setAdminEntity(2, Beni);
        fargo.setAdminEntity(1, NordKivu);

      
        SiteDTO boise = new SiteDTO(5);
        boise.setActivityId(91);
        boise.setPartner(new PartnerDTO(89, "AVSI"));
        boise.setLocationName("Boise Idahao");


        Map<Integer, SiteDTO> sites = new HashMap<Integer, SiteDTO>();
        sites.put(4, fargo);
        sites.put(5, boise);

        return sites;
    }

    public static SiteResult PEAR_Sites_Many_Results(int count) {
        List<SiteDTO> sites = new ArrayList<SiteDTO>();
        for(int i=0;i!=count;++i) {
            SiteDTO fargo = new SiteDTO(i);
            fargo.setActivityId(91);
            fargo.setPartner(new PartnerDTO(88, "NRC"));
            fargo.setLocationName("Fargo");
            fargo.setAdminEntity(3, Watalina);
            fargo.setAdminEntity(2, Beni);
            fargo.setAdminEntity(1, NordKivu);
            sites.add(fargo);
        }
        return new SiteResult(sites);
    }

    public static SiteResult PEAR_Sites_Result() {
        return new SiteResult(new ArrayList<SiteDTO>(PEAR_Sites().values()));
    }
    
    public static ListResult<AdminEntityDTO> getProvinces() {
        List<AdminEntityDTO> list = new ArrayList<AdminEntityDTO>();
        list.add(NordKivu);
        list.add(SudKivu);

        return new AdminEntityResult(list);
    }

    public static ListResult<AdminEntityDTO> getTerritoires(int provinceId) {

        List<AdminEntityDTO> list = new ArrayList<AdminEntityDTO>();

        if(provinceId == 100) {
            list.add(Beni);
            list.add(Masisi);
        } else if(provinceId == 200) {
            list.add(new AdminEntityDTO(2, 202, 200, "Shabunda"));
            list.add(new AdminEntityDTO(2, 201, 200, "Walungu"));
        }

        return new AdminEntityResult(list);
    }

    public static SchemaDTO PEARPlus() {

        CountryDTO country = new CountryDTO(1, "RDC");
        country.getAdminLevels().add(new AdminLevelDTO(1, "Province") );
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

    public static AdminEntityResult PEARPlus_Provinces() {
        List<AdminEntityDTO> list = new ArrayList<AdminEntityDTO>();
        list.add(new AdminEntityDTO(1, 1, "Ituri"));
        return new AdminEntityResult(list);
    }

    public static AdminEntityResult PEARPlus_ZS() {
        List<AdminEntityDTO> list = new ArrayList<AdminEntityDTO>();
        list.add(new AdminEntityDTO(2, 11, 1, "Banana"));
        list.add(new AdminEntityDTO(2, 12, 1, "Drodo"));
        return new AdminEntityResult(list);
    }

    public static AdminEntityResult PEARPlus_AS() {
        List<AdminEntityDTO> list = new ArrayList<AdminEntityDTO>();
        list.add(new AdminEntityDTO(3, 111, 11, "Logo"));
        list.add(new AdminEntityDTO(3, 112, 11, "Ndikpa"));
        list.add(new AdminEntityDTO(3, 113, 11, "Zengo"));
        return new AdminEntityResult(list);
    }




    public static BaseMapResult BaseMaps() {

        BaseMap map = new LocalBaseMap();
        map.setName("Administrative Map");
        map.setCopyright("Foobar");
        map.setId("admin");
        map.setMinZoom(0);
        map.setMaxZoom(16);

        List<BaseMap> maps = new ArrayList<BaseMap>();
        maps.add(map);

        return new BaseMapResult(maps);
        
    }



}
