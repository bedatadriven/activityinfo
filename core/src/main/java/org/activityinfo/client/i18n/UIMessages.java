package org.activityinfo.client.i18n;

import java.util.Date;

/**
 * Messages for the application.
 * 
 * <p>
 * Note that this file is no longer generated, it should be edited directly
 * instead of editing UIMessages.properties (removed).
 */
public interface UIMessages extends com.google.gwt.i18n.client.Messages {

	/**
	 * Translated "{0} - {1}".
	 * 
	 * @return translated "{0} - {1}"
	 */
	@DefaultMessage("{0} - {1}")
	String activityTitle(String arg0, String arg1);

	/**
	 * Translated "added {0}".
	 * 
	 * @return translated "added {0}"
	 */
	@DefaultMessage("added {0}")
	String added(String arg0);

	/**
	 * Translated "Location [{0}] added".
	 * 
	 * @return translated "Location [{0}] added"
	 */
	@DefaultMessage("Location [{0}] added")
	String addedLocation(String arg0);

	/**
	 * Translated "Unable to create user ''{0}'' !".
	 * 
	 * @return translated "Unable to create user ''{0}'' !"
	 */
	@DefaultMessage("Unable to create user ''{0}'' !")
	String adminUserCreationFailure(String arg0);

	/**
	 * Translated "A problem has occurred while creating user ''{0}'' !".
	 * 
	 * @return translated "A problem has occurred while creating user ''{0}'' !"
	 */
	@DefaultMessage("A problem has occurred while creating user ''{0}'' !")
	String adminUserCreationNull(String arg0);

	/**
	 * Translated "New user has been successfully created under id ''{0}'' !".
	 * 
	 * @return translated
	 *         "New user has been successfully created under id ''{0}'' !"
	 */
	@DefaultMessage("New user has been successfully created under id ''{0}'' !")
	String adminUserCreationSuccess(String arg0);

	/**
	 * Translated "clustered {0} sites using {1} clustering method.".
	 * 
	 * @return translated "clustered {0} sites using {1} clustering method."
	 */
	@DefaultMessage("clustered {0} sites using {1} clustering method.")
	String amountSitesClusteredByClusteringMethod(String arg0, String arg1);

	/**
	 * Translated "You can include multiple indicators on this type of layer".
	 * 
	 * @return translated
	 *         "You can include multiple indicators on this type of layer"
	 */
	@DefaultMessage("You can include multiple indicators on this type of layer")
	String canIncludeMultipleIndicators();

	/**
	 * Translated "One indicator can be selected for this type of layer".
	 * 
	 * @return translated "One indicator can be selected for this type of layer"
	 */
	@DefaultMessage("One indicator can be selected for this type of layer")
	String canIncludeSingleIndicator();

	/**
	 * Translated "[show all {0} sites in {1}]".
	 * 
	 * @return translated "[show all {0} sites in {1}]"
	 */
	@DefaultMessage("[show all {0} sites in {1}]")
	String clickToShowAllSitesOfAdminEntity(String arg0, String arg1);

	/**
	 * Translated
	 * "Have you checked if a location already exists? \r\n\r\n Identical locations lead to bad quality reports. \r\n\r\n Please ensure there is not already a location in the area you specified."
	 * .
	 * 
	 * @return translated
	 *         "Have you checked if a location already exists? \r\n\r\n Identical locations lead to bad quality reports. \r\n\r\n Please ensure there is not already a location in the area you specified."
	 */
	@DefaultMessage("Have you checked if a location already exists? \r\n\r\n Identical locations lead to bad quality reports. \r\n\r\n Please ensure there is not already a location in the area you specified.")
	String confirmAddLocation();

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
	 * Translated "edited {0}".
	 * 
	 * @return translated "edited {0}"
	 */
	@DefaultMessage("edited {0}")
	String edited(String arg0);

	/**
	 * Translated "Last Sync''d: {0}".
	 * 
	 * @return translated "Last Sync''d: {0}"
	 */
	@DefaultMessage("Last Sync''d: {0}")
	String lastSynced(String arg0);

	/**
	 * Translated "{0} locations without GPS".
	 * 
	 * @return translated "{0} locations without GPS"
	 */
	@DefaultMessage("{0} locations without GPS")
	String locationsWithoutGps(String arg0);

	/**
	 * Translated
	 * "Found no existing locations. Make geography less specific, or add a new location."
	 * .
	 * 
	 * @return translated
	 *         "Found no existing locations. Make geography less specific, or add a new location."
	 */
	@DefaultMessage("Found no existing sites. Make geography less specific, or add a new location.")
	String noLocationsFound();

	/**
	 * Translated "Database {0} has no projects defined".
	 * 
	 * @return translated "Database {0} has no projects defined"
	 */
	@DefaultMessage("Database {0} has no projects defined")
	String noProjectsDefinedForDatabase(String arg0);

	/**
	 * Translated "No results found for search query \"{0}\".".
	 * 
	 * @return translated "No results found for search query \"{0}\"."
	 */
	@DefaultMessage("No results found for search query \"{0}\".")
	String noSearchResults(String arg0);

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
	@DefaultMessage("{0}Q{1}")
	String quarter(int year, int quarter);

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

	/**
	 * Translated
	 * "Showing locks for database [{0}], project [{1}] for activity [{2}]".
	 * 
	 * @return translated
	 *         "Showing locks for database [{0}], project [{1}] for activity [{2}]"
	 */
	@DefaultMessage("Showing locks for database [{0}], project [{1}] for activity [{2}]")
	String showLockedPeriodsTitle(String arg0, String arg1, String arg2);


	/**
	 * Translated
	 * "Too many ({0}) locations found. Please refine criteria to see possible locations.  "
	 * .
	 * 
	 * @return translated
	 *         "Too many ({0}) locations found. Please refine criteria to see possible locations.  "
	 */
	@DefaultMessage("Too many ({0}) sites found. Please refine criteria to see possible sites.  ")
	String tooManyLocationsFound(String arg0);

	/**
	 * Translated "ActivityInfo r{0}".
	 * 
	 * @return translated "ActivityInfo r{0}"
	 */
	@DefaultMessage("ActivityInfo r{0}")
	String versionedActivityInfoTitle(String arg0);

	@DefaultMessage("Add new intervention for activity ''{0}''")
	String addNewSiteForActivity(String activityName);
	
	@DefaultMessage("{0,number} matching sites")
	String matchingLocations(int count); 
	
	@DefaultMessage("Use site ''{0}''")
	String useLocation(String name);
	
	@DefaultMessage("Targets for database {0}")
	String targetsForDatabase(String arg0);

	@DefaultMessage("Please enter email address to subscribe.")
	String noEmailAddress();
	
	@DefaultMessage("{0} Users")
	String users(String groupName);
	
	@DefaultMessage("Report ''{0}'' added to dashboard.")
	String addedToDashboard(String reportName);
	
	@DefaultMessage("Report ''{0}'' removed from dashboard.")
	String removedFromDashboard(String reportName);

	@DefaultMessage("The report ''{0}'' has been saved.")
	String reportSaved(String name);
	
	@DefaultMessage("Link \"{0}\" to \"{1}\"")
	String link(String source, String dest);
	
	
	@DefaultMessage("Unlink \"{0}\" to \"{1}\"")
	String unlink(String source, String dest);

	@DefaultMessage("Are you sure you want to delete the report \"{0}\"")
	String confirmDeleteReport(String reportTitle);
}

