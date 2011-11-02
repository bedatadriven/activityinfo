package org.sigmah.shared.dto;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.server.database.hibernate.entity.User;

import com.extjs.gxt.ui.client.data.BaseModelData;

/*
 * Represents a user configured dashboard
 */
public class DashboardSettingsDTO extends BaseModelData implements DTO {
	List<FavoriteDTO> favorites = new ArrayList<FavoriteDTO>();
	
	public DashboardSettingsDTO() {
		super();
		
		favorites.add(new FavoriteDTO()
					  .setName("SomeReport")
					  .setFavoriteType(FavoriteType.Report)
					  .setId(1));
		favorites.add(new FavoriteDTO()
					  .setName("SomeSite")
					  .setFavoriteType(FavoriteType.Site)
					  .setId(3));
		favorites.add(new FavoriteDTO()
					  .setName("SomePartner")
					  .setFavoriteType(FavoriteType.Partner)
					  .setId(3));
	}
	
	public void setAmountColumns(int amountColumns) {
		set("amountColumns", amountColumns);
	}

	public int getAmountColumns() {
		return (Integer)get("amountColumns");
	}

	public void setUser(User user) {
		set("user", user);
	}

	public User getUser() {
		return (User)get("user");
	}
	
	/*
	 * Returns a new DashboardSettingsDTO instance with default settings
	 */
	public static DashboardSettingsDTO createDefault() {
		DashboardSettingsDTO defaultDashBoard = new DashboardSettingsDTO();
		
		defaultDashBoard.setAmountColumns(3);
		
		return defaultDashBoard;
	}

	public List<FavoriteDTO> getFavorites() {
		return favorites;
	}
}