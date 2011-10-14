package org.sigmah.shared.command;

import java.io.Serializable;
import java.util.Map;

import org.sigmah.shared.command.SitesPerAdminEntity.SitesPerAdminEntityResult;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dto.AdminEntityDTO;

import com.google.common.collect.Maps;


/** Request a list of AdminEntities of an activity with amount of sites per admin entity */
public class SitesPerAdminEntity implements Command<SitesPerAdminEntityResult> {
	private int activityId;
	
	public SitesPerAdminEntity(int activityId) {
		this.activityId = activityId;
	}

	public SitesPerAdminEntity() {
	}

	public int getActivityId() {
		return activityId;
	}

	public static class SitesPerAdminEntityResult implements CommandResult {
		private Map<Integer, AmountPerAdminEntity> adminEntitiesById = Maps.newHashMap();
		
		public SitesPerAdminEntityResult(Map<Integer, AmountPerAdminEntity> adminEntitiesById) {
			this.adminEntitiesById = adminEntitiesById;
		}

		public SitesPerAdminEntityResult() {
		}

		public Map<Integer, AmountPerAdminEntity> getAdminEntitiesById() {
			return adminEntitiesById;
		}

		public static class AmountPerAdminEntity implements Serializable {
			
			private int amountSites;
			private AdminEntityDTO adminEntity;
			
			public AmountPerAdminEntity(int amountSites,
					AdminEntityDTO adminEntity) {
				super();
				this.amountSites = amountSites;
				this.adminEntity = adminEntity;
			}
			public AmountPerAdminEntity() {
			}
			
			public int getAmountSites() {
				return amountSites;
			}
			public AdminEntityDTO getAdminEntity() {
				return adminEntity;
			}
		}
	}

}
