package org.activityinfo.shared.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;


import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.ModelData;

public class Schema extends BaseModel implements DTO {

	
	private List<UserDatabaseDTO> databases = new ArrayList<UserDatabaseDTO>(0);
	private List<CountryModel> countries = new ArrayList<CountryModel>(0);
	
	public Schema()
	{
	}
	
	public List<UserDatabaseDTO> getDatabases()
	{
		return databases;
	}
	
	public void setDatabases(List<UserDatabaseDTO> databases) {
		this.databases = databases;
	}

	public ActivityModel getActivityById(int id) {
		for(UserDatabaseDTO database : databases) {
			ActivityModel activity = getById(database.getActivities(), id);
			if(activity!=null)
				return activity;
		}
		return null;
	}

	public PartnerModel getPartnerById(int partnerId) {
		for(UserDatabaseDTO database : databases) {
			PartnerModel partner = getById(database.getPartners(), partnerId);
			if(partner!=null)
				return partner;
		}
		return null;
	}

	public CountryModel getCountryById(int countryId) {
		return getById(countries, countryId);
	}
	
	/**
	 * 
	 * @return The common country shared by all visible databases, or null if there are no
	 * databases or databases in different countries.
	 */
	public CountryModel getCommonCountry() {
		CountryModel country = null;
		
		for(UserDatabaseDTO database : databases) {
			if(country == null) {
				country = database.getCountry();
			} else if(country.getId() != database.getCountry().getId()) {
				return null;
			}
		}
		
		return country;
	}

	public UserDatabaseDTO getDatabaseById(int id) {
		for(UserDatabaseDTO database : databases) {
			if(database.getId() == id) {
				return database;
			}
		}
		return null;
	}
	
	public static <T extends ModelData> T getById(List<T> list, int id) {
		for(T m : list) {
			Integer mId = m.get("id");
			if(mId!=null && mId.equals(id)) {
				return m;
			}
		}
		return null;
	}

	public List<CountryModel> getCountries() {
		return countries;
	}

	public void setCountries(List<CountryModel> countries) {
		this.countries = countries;
	}

	public AdminLevelModel getAdminLevelById(int parentLevelId) {
		for(CountryModel country : countries) {
			AdminLevelModel level = country.getAdminLevelById(parentLevelId);
			if(level!=null)
				return level;
		}
		return null;
	}

    public ActivityModel getActivityByIndicatorId(int id) {
        for(UserDatabaseDTO db : databases) {
			for(ActivityModel act:  db.getActivities()) {
				for(IndicatorModel ind : act.getIndicators()) {
					if(ind.getId() == id) {
						return act;
					}
				}
			}

		}
		return null;
    }

	public IndicatorModel getIndicatorById(int id) {
		for(UserDatabaseDTO db : databases) {
			
			for(ActivityModel act:  db.getActivities()) {
				for(IndicatorModel ind : act.getIndicators()) {
					if(ind.getId() == id) {
						return ind;
					}
				}
			}
			
		}
		return null;
	}

    public Set<PartnerModel> getVisiblePartners() {

        Set<PartnerModel> partners = new HashSet<PartnerModel>();

        for(UserDatabaseDTO database : getDatabases()) {
            partners.addAll(database.getPartners());
        }

        return partners;
    }

    public List<PartnerModel> getVisiblePartnersList() {
        List<PartnerModel> list = new ArrayList<PartnerModel>();
        list.addAll(getVisiblePartners());
        return list;
    }

    public ActivityModel getFirstActivity() {
		for(UserDatabaseDTO database : getDatabases()) {

			for(ActivityModel activity : database.getActivities()) {
				return activity;
			}
		}
		return null;
	}

}
