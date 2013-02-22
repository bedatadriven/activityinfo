package org.activityinfo.shared.command;

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

import java.util.List;

import org.activityinfo.shared.command.GetSiteHistory.GetSiteHistoryResult;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.SiteHistoryDTO;

import com.google.common.collect.Lists;

public class GetSiteHistory implements Command<GetSiteHistoryResult> {
	private static final long serialVersionUID = 1475811548735657666L;
	
	private int siteId;

	public GetSiteHistory() {
	}
	
	public GetSiteHistory(int siteId) {
		this.siteId = siteId;
	}
	
	public int getSiteId() {
		return siteId;
	}
	
	public static class GetSiteHistoryResult implements CommandResult {
		private static final long serialVersionUID = -3044469805146309286L;
		
		private List<SiteHistoryDTO> siteHistories = Lists.newArrayList();

		public GetSiteHistoryResult() {
		}

		public GetSiteHistoryResult(List<SiteHistoryDTO> siteHistories) {
			if (siteHistories != null) {
				this.siteHistories = siteHistories;
			}
		}

		public List<SiteHistoryDTO> getSiteHistories() {
			return siteHistories;
		}
		
		public boolean hasHistories() {
			return (siteHistories != null && siteHistories.size() > 0);
		}
		
		public List<Integer> collectLocationIds() {
			List<Integer> ids = Lists.newArrayList();
			for (SiteHistoryDTO dto : getSiteHistories()) {
				Object id = dto.getJsonMap().get("locationId");
				if (id != null) {
					ids.add(Integer.parseInt(id.toString()));
				}
			}
			return ids;
		}
	}
}
