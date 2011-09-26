package org.sigmah.shared.command.handler;

import org.sigmah.shared.command.CollapseLocations;
import org.sigmah.shared.command.CollapseLocations.CollapseLocationsResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class CollapseLocationsHandler implements CommandHandlerAsync<CollapseLocations, CollapseLocationsResult>{

	@Override
	public void execute(CollapseLocations command, ExecutionContext context,
			AsyncCallback<CollapseLocationsResult> callback) {
		/** Entities which use a locationID:
		 *  See http://stackoverflow.com/questions/193780/how-to-find-all-the-tables-in-mysql-with-specific-column-names-in-them
		 *  select distinct TABLE_NAME 
		 *  	from INFORMATION_SCHEMA.COLUMNS 
		 *  	where COLUMN_NAME IN ('locationid') AND TABLE_SCHEMA='activityinfo'
		 *  
		 *  
		 *  
		 */
		
	}
	
}
