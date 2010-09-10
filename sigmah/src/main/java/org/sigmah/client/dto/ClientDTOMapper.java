/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dto;

import java.util.ArrayList;

import org.sigmah.shared.domain.UserPermission;
import org.sigmah.shared.domain.Activity;
import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.domain.AdminLevel;
import org.sigmah.shared.domain.Attribute;
import org.sigmah.shared.domain.AttributeGroup;
import org.sigmah.shared.domain.Country;
import org.sigmah.shared.domain.Indicator;
import org.sigmah.shared.domain.LocationType;
import org.sigmah.shared.domain.OrgUnit;
import org.sigmah.shared.domain.Site;
import org.sigmah.shared.domain.UserDatabase;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.AttributeDTO;
import org.sigmah.shared.dto.AttributeGroupDTO;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.DTOMapper;
import org.sigmah.shared.dto.EntityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.LocationTypeDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.dto.UserPermissionDTO;


/**
 * A tool for mapping domain objects to DTOs. 
 *
 * @author JDH
 */
public class ClientDTOMapper implements DTOMapper {

	
	private UserDatabaseDTO map(UserDatabase o) {
		UserDatabaseDTO d = new UserDatabaseDTO();
		d.setId(o.getId());
		d.setName(o.getName());
		d.setOwnerName(o.getOwner() == null ? null: o.getOwner().getName());
		d.setOwnerEmail(o.getOwner() == null ? null: o.getOwner().getEmail());
		d.setFullName(o.getFullName());
		ArrayList<ActivityDTO> arr = new ArrayList<ActivityDTO>();
		for (Activity c : o.getActivities()) {
			arr.add(map(c));
		}
		d.setActivities(arr);
		d.setCountry(map(o.getCountry()));
		ArrayList<PartnerDTO> arr2 = new ArrayList<PartnerDTO>();
		for (OrgUnit c : o.getPartners()) {
			arr2.add(map(c));
		}
		d.setPartners(arr2);
		/*
		d.setViewAllAllowed(o.ViewAllAllowed());
		d.setEditAllowed(o.getEditAllowed());
		d.setDesignAllowed(o.getDesignAllowed());
		d.setEditAllAllowed(o.getEditAllAllowed());
		d.setManageUsersAllowed(o.getManageUsersAllowed());
		d.setManageAllUsersAllowed(o.getManageAllUsersAllowed());
		d.setMyPartnerId(o.getMyPartnerId());
		d.setAmOwner(o.getAmOwner());
		*/
		return d;
	}

	private ActivityDTO map(Activity o) {
		ActivityDTO d = new ActivityDTO();
		d.setId(o.getId());
	    d.setName(o.getName());
	    d.setReportingFrequency(o.getReportingFrequency());
	    d.setAssessment(o.isAssessment());
	    LocationType l = o.getLocationType();
	    if (l != null)
	    	d.setLocationTypeId(l.getId());
	    d.setCategory(o.getCategory());
	    d.setMapIcon(o.getMapIcon());
		ArrayList<IndicatorDTO> arr = new ArrayList<IndicatorDTO>();
		for (Indicator c : o.getIndicators()) {
			arr.add(map(c));
		}
		d.setIndicators(arr);
		ArrayList<AttributeGroupDTO> arr2 = new ArrayList<AttributeGroupDTO>();
		for (AttributeGroup c : o.getAttributeGroups()) {
			arr2.add(map(c));
		}
		d.setAttributeGroups(arr2);
	    return d;
	}

	private AdminEntityDTO map(AdminEntity o) {
		AdminEntityDTO d = new AdminEntityDTO();
		d.setId(o.getId());
		d.setName(o.getName());
		d.setLevelId(o.getLevel() == null ? null : o.getLevel().getId());
		d.setParentId(o.getParent() == null ? null : o.getParent().getId());
	//	d.setBounds(o.getBounds());
		return d;
	}

	private AdminLevelDTO map(AdminLevel o) {
		AdminLevelDTO d = new AdminLevelDTO();
		d.setId(o.getId());
		d.setName(o.getName());
		d.setParentLevelId(o.getParent() == null ? null : o.getParent().getId());
		return d;
	}

	private AttributeDTO map(Attribute o) {
		AttributeDTO d = new AttributeDTO();
		d.setId(o.getId());
		d.setName(o.getName());
		return d;
	}

	private AttributeGroupDTO map(AttributeGroup o) {
		AttributeGroupDTO d = new AttributeGroupDTO();
		d.setId(o.getId());
		d.setName(o.getName());
		ArrayList<AttributeDTO> arr = new ArrayList<AttributeDTO>();
		for (Attribute c : o.getAttributes()) {
			arr.add(map(c));
		}
		d.setAttributes(arr);
		//d.setMultipleAllowed(o.getMultipleAllowed());
		return d;
	}
	
	private CountryDTO map(Country o) {
		CountryDTO d = new CountryDTO();
		d.setId(o.getId());
		d.setName(o.getName());
		ArrayList<LocationTypeDTO> arr = new ArrayList<LocationTypeDTO>();
		for (LocationType c : o.getLocationTypes()) {
			arr.add(map(c));
		}
		d.setLocationTypes(arr);
		// map name and id only
		/*
		ArrayList<AdminLevelDTO> arr = new ArrayList<AdminLevelDTO>();
		for (AdminLevel c : o.getAdminLevels()) {
			arr.add(map(c));
		}
		d.setAdminLevels(arr);
		ArrayList<LocationTypeDTO> arr2 = new ArrayList<LocationTypeDTO>();
		for (LocationType c : o.getLocationTypes()) {
			arr2.add(map(c));
		}
		d.setLocationTypes(arr2);
		*/
		
		//d.setBounds(o.getBounds());
		return d;
	}

	private IndicatorDTO map(Indicator o) {
		IndicatorDTO d = new IndicatorDTO();
		d.setId(o.getId());
		d.setName(o.getName());
		d.setUnits(o.getUnits());
		d.setListHeader(o.getListHeader());
		d.setDescription(o.getDescription());
		d.setCollectIntervention(o.getCollectIntervention());
		d.setCollectMonitoring(o.isCollectMonitoring());
		d.setAggregation(o.getAggregation());
		d.setCategory(o.getCategory());
		return d;
	}

	private LocationTypeDTO map(LocationType o) {
		LocationTypeDTO d = new LocationTypeDTO();
		d.setId(o.getId());
		d.setName(o.getName());
		d.setBoundAdminLevelId(o.getBoundAdminLevel() == null ? null : o.getBoundAdminLevel().getId());
		return d;
	}
	
	private PartnerDTO map(OrgUnit o) {
		PartnerDTO d = new PartnerDTO();
		d.setId(o.getId());
		d.setName(o.getName());
		d.setFullName(o.getFullName());
		return d;
	}
	
	private UserPermissionDTO map(UserPermission o) {
		UserPermissionDTO d = new UserPermissionDTO();
		d.setName(o.getUser() == null ? null: o.getUser().getName());
		d.setEmail(o.getUser() == null ? null: o.getUser().getEmail());
		d.setAllowView(o.isAllowView());
		d.setAllowDesign(o.isAllowDesign());
		d.setAllowViewAll(o.isAllowViewAll());
		d.setAllowEdit(o.isAllowEdit());
		d.setAllowEditAll(o.isAllowEditAll());
		d.setAllowManageUsers(o.isAllowManageUsers());
		d.setAllowManageAllUsers(o.isAllowManageAllUsers());
		d.setPartner(map(o.getPartner()));
		return d;
	}

	private SiteDTO map(Site o) {
		SiteDTO d = new SiteDTO();
		d.setActivityId(o.getActivity().getId());
		//d.setAdminEntity(levelId, value)
		d.setComments(o.getComments());
		d.setDate1(o.getDate1());
		d.setDate2(o.getDate2());
		d.setId(o.getId());
		d.setLocationAxe(o.getLocation().getAxe());
		d.setLocationName(o.getLocation().getName());
		d.setPartner(map(o.getPartner()));
		d.setX(o.getLocation().getX());
		d.setY(o.getLocation().getY());
		return d;
	}
	
	/**
     * Map a domain object to an instance of the given class. 
     *
     * @param o The domain object to match.
     * @param c Class of the target object.
     */
	@Override
	public Object map(Object o, Class c) {
		if (ActivityDTO.class.equals(c)) {
			return map((Activity)o);
		} else if (AdminLevelDTO.class.equals(c)) {
			return map((AdminLevel)o);
		} else if (CountryDTO.class.equals(c)) {		
			return map((Country)o);
		} else if (UserDatabaseDTO.class.equals(c)) {
			return map((UserDatabase)o);
		} else if (PartnerDTO.class.equals(c)) {
			return map((OrgUnit)o);
		} else if (AdminEntityDTO.class.equals(c)) {
			return map((AdminEntity)o);
		} else if (AttributeDTO.class.equals(c)) {
			return map((Attribute)o);
		} else if (AttributeGroupDTO.class.equals(c)) {
			return map((AttributeGroup)o);
		} else if (EntityDTO.class.equals(c)) {
			return map((AdminEntity)o);
		} else if (IndicatorDTO.class.equals(c)) {
			return map((Indicator)o);
		} else if (LocationTypeDTO.class.equals(c)) {
			return map((LocationType)o);
		} else if (SiteDTO.class.equals(c)) {	
			return map((Site)o);
		} else {
			// fail if someone tries to map an unknown type
			assert(false);
		}
		return null;
	}
}