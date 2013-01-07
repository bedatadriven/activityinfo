package org.activityinfo.client.i18n;

import java.util.Date;

/**
 * Messages for the application.
 * 
 * <p>
 * Note that this file is no longer generated, it should be edited directly here
 * instead of editing UIMessages.properties (removed).
 */
public interface UIMessages extends com.google.gwt.i18n.client.Messages {



	/**
	 * Translated "Are you sure you want to delete the database <b>{0}</b>? <br>
	 * <br>
	 * You will loose all activities and indicator results.".
	 * 
	 * @return translated "Are you sure you want to delete the database
	 *         <b>{0}</b>? <br>
	 * <br>
	 *         You will loose all activities and indicator results."
	 */
	@DefaultMessage("Are you sure you want to delete the database <b>{0}</b>? <br><br>You will loose all activities and indicator results.")
	String confirmDeleteDb(String arg0);

	/**
	 * Translated "The coordinate falls outside of the bounds of {0}".
	 * 
	 * @return translated "The coordinate falls outside of the bounds of {0}"
	 */
	@DefaultMessage("The coordinate falls outside of the bounds of {0}")
	String coordOutsideBounds(String arg0);

	/**
	 * Translated "Last Sync''d: {0}".
	 * 
	 * @return translated "Last Sync''d: {0}"
	 */
	@DefaultMessage("Last Sync''d: {0}")
	String lastSynced(String arg0);


	/**
	 * Translated
	 * "There is already data entered for the partner {0}. Before deleting this partner, you must delete the partner''s data."
	 * .
	 */
	@DefaultMessage("There is already data entered for the partner {0}. Before deleting this partner, you must delete the partner''s data.")
	String partnerHasDataWarning(String arg0);

	/**
	 * Translated
	 * "There is already data entered for the project {0}. Before deleting this project, you must delete the project''s data. "
	 * .
	 */
	@DefaultMessage("There is already data entered for the project {0}. Before deleting this project, you must delete the project''s data. ")
	String projectHasDataWarning(String arg0);

	/**
	 * Translated "Projects for database {0}".
	 */
	@DefaultMessage("Projects for database {0}")
	String projectsForDatabase(String arg0);

	/**
	 * Translated "{0,number,integer}Q{1,number,integer}".
	 * 
	 * @return translated "{0,number,integer}Q{1,number,integer}"
	 */
	@DefaultMessage("{0,number,#}Q{1}")
	String quarter(int year, int quarter);
	
	@DefaultMessage("{0,number,#}W{1}")
	String week(int year, int week);

	@DefaultMessage("{0,date,MMM}")
	String month(Date month);

	/**
	 * Translated "{0} most recent added sites for search query".
	 * 
	 * @return translated "{0} most recent added sites for search query"
	 */
	@DefaultMessage("{0} most recent added sites for search query")
	String recentlyAddedSites(String arg0);

	/**
	 * Translated
	 * "Nothing entered to search on: please enter something you want to search for"
	 * .
	 * 
	 * @return translated
	 *         "Nothing entered to search on: please enter something you want to search for"
	 */
	@DefaultMessage("Nothing entered to search on: please enter something you want to search for")
	String searchQueryEmpty();

	/**
	 * Translated "Enter a search query with at least 3 characters".
	 * 
	 * @return translated "Enter a search query with at least 3 characters"
	 */
	@DefaultMessage("Enter a search query with at least 3 characters")
	String searchQueryTooShort();

	/**
	 * Translated
	 * "For query \"{0}\", found {1} databases, {2} activities and {3} indicators"
	 * .
	 * 
	 * @return translated
	 *         "For query \"{0}\", found {1} databases, {2} activities and {3} indicators"
	 */
	@DefaultMessage("For query \"{0}\", found {1} databases, {2} activities and {3} indicators")
	String searchResultsFound(String arg0, String arg1, String arg2, String arg3);

	@DefaultMessage("Add new intervention for activity ''{0}''")
	String addNewSiteForActivity(String activityName);
	
	@DefaultMessage("{0,number} matching sites")
	String matchingLocations(int count); 
	
	@DefaultMessage("Use site ''{0}''")
	String useLocation(String name);
	
	@DefaultMessage("Targets for database {0}")
	String targetsForDatabase(String arg0);

	@DefaultMessage("Report ''{0}'' added to dashboard.")
	String addedToDashboard(String reportName);
	
	@DefaultMessage("Report ''{0}'' removed from dashboard.")
	String removedFromDashboard(String reportName);

	@DefaultMessage("The report ''{0}'' has been saved.")
	String reportSaved(String name);

	@DefaultMessage("Are you sure you want to delete the report \"{0}\"")
	String confirmDeleteReport(String reportTitle);
	
	@DefaultMessage("You are not the owner of this report.<br/>Do you want to save a new copy?")
	String confirmSaveCopy(); 

	@DefaultMessage("The activity \"{0}\" has not been marked as public by the database owner and so cannot be embedded in a public web page. Please contact the database owner and request that the activity be published.")
	String activityNotPublic(String name);
	
	@DefaultMessage("In order to embed this sheet in a public web page, the activity \"{0}\" must be made public. Do you want to make this activity public now?")
	String promptPublishActivity(String name);

	@DefaultMessage("{0} minutes ago")
	String minutesAgo(int minutes);
	
	@DefaultMessage("{0} hours ago")
	String hoursAgo(int hours);
	
	@DefaultMessage("{0} days ago")
	String daysAgo(int hours);

	@DefaultMessage("{0}: New {1} at {2} by {3}")
	String newSiteSubject(String databaseName, String activityName, String locationName, String partnerName);
	
	@DefaultMessage("{0}: Updated {1} at {2}")
	String updatedSiteSubject(String databaseName, String activityName, String locationName);
		
	@DefaultMessage("Hi {0},")
	String sitechangeGreeting(String userName);
	
	@DefaultMessage("{0} ({1}) updated the {2} at {3} in the {4} database on {5,date,dd-MM-yyyy 'at' HH:mm}. Here are the details:")
	String siteChangeIntro(String userName, String userEmail, String activityName, String locationName, String database, Date date);

	@DefaultMessage("{0} ({1}) created a new {2} at {3} in the {4} database on {5,date,dd-MM-yyyy 'at' HH:mm}. Here are the details:")
	String siteCreateIntro(String userName, String userEmail, String activityName, String locationName, String databaseName, Date date);

	@DefaultMessage("Best regards,<br>The ActivityInfo Team")
	String sitechangeSignature();
	
	@DefaultMessage("{0,date,dd-MM-yyyy - HH:mm} {1} ({2}) created the site.")
	String siteHistoryCreated(Date date, String userName, String userEmail);

	@DefaultMessage("{0,date,dd-MM-yyyy - HH:mm} {1} ({2}) updated the site:")
	String siteHistoryUpdated(Date date, String userName, String userEmail);
	
	@DefaultMessage("was: {0}")
	String siteHistoryOldValue(Object oldValue);

	@DefaultMessage("was: blank")
	String siteHistoryOldValueBlank();
	
	@DefaultMessage("Added attribute {0}")
	String siteHistoryAttrAdd(String attrName);

	@DefaultMessage("Removed attribute {0}")
	String siteHistoryAttrRemove(String attrName);
}

