package org.sigmah.shared.dto.portlets;

public class FavoritesDTO implements PortletDTO {

	@Override
	public String getName() {
		return "Saved pages";
	}

	@Override
	public String getDescription() {
		return "Displays a list of \"favorites\": pages the user has saved for quick access";
	}

}
