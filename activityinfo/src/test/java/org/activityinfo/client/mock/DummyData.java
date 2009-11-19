package org.activityinfo.client.mock;

import org.activityinfo.shared.command.result.*;
import org.activityinfo.shared.dto.*;
import org.activityinfo.shared.map.BaseMap;
import org.activityinfo.shared.map.LocalBaseMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyData {


    public static final AdminEntityModel Beni = new AdminEntityModel(2, 101, 100, "Beni", new Bounds(0, 0, 25, 25));
    public static final AdminEntityModel Masisi = new AdminEntityModel(2, 102, 100, "Masisi", new Bounds(0, 25, 25, 50));
    public static final AdminEntityModel NordKivu = new AdminEntityModel(1, 100, "Nord Kivu", new Bounds(0, 0, 100, 100));
    public static final AdminEntityModel SudKivu = new AdminEntityModel(1, 200, "Sud Kivu", new Bounds(0, 0, -100, -100));
    public static final AdminEntityModel Watalina = new AdminEntityModel(3, 1011, Beni.getParentId(), "Watalinga");

    /**
     *
     * @return A schema with one program "PEAR" with two activities (ids=91,92)
     */
	public static Schema PEAR() {

        CountryModel country = new CountryModel(1, "RDC");
        country.setBounds(new Bounds(0, 0, 300, 300));
        country.getAdminLevels().add(new AdminLevelModel(1, "Province"));
        country.getAdminLevels().add(new AdminLevelModel(2, 1, "Territoire"));
        country.getAdminLevels().add(new AdminLevelModel(3, 2, "Secteur"));
        country.getLocationTypes().add(new LocationTypeModel(1, "Localite"));
        country.getLocationTypes().add(new LocationTypeModel(2, "Ecole"));

        UserDatabaseDTO pear = new UserDatabaseDTO(1, "PEAR");
        pear.setFullName("Program of Expanded Assistance to Refugees");
        pear.setEditAllAllowed(false);
        pear.setEditAllowed(true);
        pear.setDesignAllowed(true);
        pear.setMyPartnerId(88);
        pear.setCountry(country);

        pear.getPartners().add(new PartnerModel(88, "NRC"));

        ActivityModel nfiDistro = new ActivityModel(91, "NFI Distributions");
        nfiDistro.setDatabase(pear);
        nfiDistro.setLocationTypeId(1);
        pear.getActivities().add(nfiDistro);

        ActivityModel schoolRehab = new ActivityModel(92, "School Rehab");
        schoolRehab.setDatabase(pear);
        schoolRehab.setLocationTypeId(2);
        pear.getActivities().add(schoolRehab);

        AttributeGroupModel rehabType = new AttributeGroupModel(71);
        rehabType.setName("Rehab type");
        rehabType.setMultipleAllowed(false);
        schoolRehab.getAttributeGroups().add(rehabType);
        
        AttributeModel minor = new AttributeModel(711, "Minor");
        rehabType.getAttributes().add(minor);

        AttributeModel major = new AttributeModel(712, "Major");
        rehabType.getAttributes().add(major);


        final Schema schema = new Schema();
        schema.getDatabases().add(pear);
        return schema;
	}

    public static UserResult RRM_Users() {
        List<UserModel> users = new ArrayList<UserModel>();

        UserModel typhaine = new UserModel();
        typhaine.setName("typhaine");
        typhaine.setEmail("typhaine@sol.net");
        typhaine.setAllowView(true);
        users.add(typhaine);

        return new UserResult(users);
    }

    /**
     *
     * @return A schema with two programs, "PEAR" and "RRM" where "PEAR" has
     * no activities and "RRM" has one activity.
     */
    public static Schema EmptyPEARandRRM() {
        Schema schema = new Schema();
        schema.getDatabases().add(new UserDatabaseDTO(1, "PEAR"));

        UserDatabaseDTO rrm = new UserDatabaseDTO(2, "RRM");
        rrm.getActivities().add(new ActivityModel(1, "NFI Distribution"));

        schema.getDatabases().add(rrm);

        return schema;
    }

    /**
     *
     * @return A map of two NFI Distribution sites (activityId=91) sites indexed by id
     * Site ids = 4, 5
     */
    public static Map<Integer, SiteModel> PEAR_Sites() {

        final SiteModel fargo = new SiteModel(4);
        fargo.setActivityId(91);
        fargo.setPartner(new PartnerModel(88, "NRC"));
        fargo.setLocationName("Fargo");
        fargo.setAdminEntity(3, Watalina);
        fargo.setAdminEntity(2, Beni);
        fargo.setAdminEntity(1, NordKivu);

      
        SiteModel boise = new SiteModel(5);
        boise.setActivityId(91);
        boise.setPartner(new PartnerModel(89, "AVSI"));
        boise.setLocationName("Boise Idahao");


        Map<Integer, SiteModel> sites = new HashMap<Integer, SiteModel>();
        sites.put(4, fargo);
        sites.put(5, boise);

        return sites;
    }

    public static SiteResult PEAR_Sites_Many_Results(int count) {
        List<SiteModel> sites = new ArrayList<SiteModel>();
        for(int i=0;i!=count;++i) {
            SiteModel fargo = new SiteModel(i);
            fargo.setActivityId(91);
            fargo.setPartner(new PartnerModel(88, "NRC"));
            fargo.setLocationName("Fargo");
            fargo.setAdminEntity(3, Watalina);
            fargo.setAdminEntity(2, Beni);
            fargo.setAdminEntity(1, NordKivu);
            sites.add(fargo);
        }
        return new SiteResult(sites);
    }

    public static SiteResult PEAR_Sites_Result() {
        return new SiteResult(new ArrayList<SiteModel>(PEAR_Sites().values()));
    }
    
    public static ListResult<AdminEntityModel> getProvinces() {
        List<AdminEntityModel> list = new ArrayList<AdminEntityModel>();
        list.add(NordKivu);
        list.add(SudKivu);

        return new AdminEntityResult(list);
    }

    public static ListResult<AdminEntityModel> getTerritoires(int provinceId) {

        List<AdminEntityModel> list = new ArrayList<AdminEntityModel>();

        if(provinceId == 100) {
            list.add(Beni);
            list.add(Masisi);
        } else if(provinceId == 200) {
            list.add(new AdminEntityModel(2, 202, 200, "Shabunda"));
            list.add(new AdminEntityModel(2, 201, 200, "Walungu"));
        }

        return new AdminEntityResult(list);
    }

    public static Schema PEARPlus() {

        CountryModel country = new CountryModel(1, "RDC");
        country.getAdminLevels().add(new AdminLevelModel(1, "Province") );
        country.getAdminLevels().add(new AdminLevelModel(2, 1, "Zone de Sante"));
        country.getAdminLevels().add(new AdminLevelModel(3, 2, "Aire de Sante"));

        LocationTypeModel aireSante = new LocationTypeModel(1, "Aire de Sante");
        aireSante.setBoundAdminLevelId(3);
        country.getLocationTypes().add(aireSante);

        Schema schema = new Schema();
        UserDatabaseDTO pearPlus = new UserDatabaseDTO(1, "PEAR Plus");
        pearPlus.setCountry(country);
        pearPlus.setEditAllAllowed(true);
        pearPlus.setEditAllowed(true);
        
        schema.getDatabases().add(pearPlus);

        ActivityModel activity = new ActivityModel(11, "Reunificiation des Enfants");
        activity.setDatabase(pearPlus);
        activity.setLocationTypeId(1);
        activity.setReportingFrequency(ActivityModel.REPORT_MONTHLY);
        pearPlus.getActivities().add(activity);

        return schema;
    }

    public static AdminEntityResult PEARPlus_Provinces() {
        List<AdminEntityModel> list = new ArrayList<AdminEntityModel>();
        list.add(new AdminEntityModel(1, 1, "Ituri"));
        return new AdminEntityResult(list);
    }

    public static AdminEntityResult PEARPlus_ZS() {
        List<AdminEntityModel> list = new ArrayList<AdminEntityModel>();
        list.add(new AdminEntityModel(2, 11, 1, "Banana"));
        list.add(new AdminEntityModel(2, 12, 1, "Drodo"));
        return new AdminEntityResult(list);
    }

    public static AdminEntityResult PEARPlus_AS() {
        List<AdminEntityModel> list = new ArrayList<AdminEntityModel>();
        list.add(new AdminEntityModel(3, 111, 11, "Logo"));
        list.add(new AdminEntityModel(3, 112, 11, "Ndikpa"));
        list.add(new AdminEntityModel(3, 113, 11, "Zengo"));
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
