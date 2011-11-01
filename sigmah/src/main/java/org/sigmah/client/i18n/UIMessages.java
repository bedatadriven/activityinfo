package org.sigmah.client.i18n;

import java.util.Date;

/**
 * Interface to represent the messages contained in resource bundle:
 * 	/home/alex/dev/activityinfo-2.3/sigmah/src/main/java/org/sigmah/client/i18n/UIMessages.properties'.
 */
public interface UIMessages extends com.google.gwt.i18n.client.Messages {
  
  /**
   * Translated "{0} - {1}".
   * 
   * @return translated "{0} - {1}"
   */
  @DefaultMessage("{0} - {1}")
  @Key("activityTitle")
  String activityTitle(String arg0,  String arg1);

  /**
   * Translated "added {0}".
   * 
   * @return translated "added {0}"
   */
  @DefaultMessage("added {0}")
  @Key("added")
  String added(String arg0);

  /**
   * Translated "Location [{0}] added".
   * 
   * @return translated "Location [{0}] added"
   */
  @DefaultMessage("Location [{0}] added")
  @Key("addedLocation")
  String addedLocation(String arg0);

  /**
   * Translated "Unable to create user ''{0}'' !".
   * 
   * @return translated "Unable to create user ''{0}'' !"
   */
  @DefaultMessage("Unable to create user ''{0}'' !")
  @Key("adminUserCreationFailure")
  String adminUserCreationFailure(String arg0);

  /**
   * Translated "A problem has occurred while creating user ''{0}'' !".
   * 
   * @return translated "A problem has occurred while creating user ''{0}'' !"
   */
  @DefaultMessage("A problem has occurred while creating user ''{0}'' !")
  @Key("adminUserCreationNull")
  String adminUserCreationNull(String arg0);

  /**
   * Translated "New user has been successfully created under id ''{0}'' !".
   * 
   * @return translated "New user has been successfully created under id ''{0}'' !"
   */
  @DefaultMessage("New user has been successfully created under id ''{0}'' !")
  @Key("adminUserCreationSuccess")
  String adminUserCreationSuccess(String arg0);

  /**
   * Translated "clustered {0} sites using {1} clustering method.".
   * 
   * @return translated "clustered {0} sites using {1} clustering method."
   */
  @DefaultMessage("clustered {0} sites using {1} clustering method.")
  @Key("amountSitesClusteredByClusteringMethod")
  String amountSitesClusteredByClusteringMethod(String arg0,  String arg1);

  /**
   * Translated "You can include multiple indicators on this type of layer".
   * 
   * @return translated "You can include multiple indicators on this type of layer"
   */
  @DefaultMessage("You can include multiple indicators on this type of layer")
  @Key("canIncludeMultipleIndicators")
  String canIncludeMultipleIndicators();

  /**
   * Translated "One indicator can be selected for this type of layer".
   * 
   * @return translated "One indicator can be selected for this type of layer"
   */
  @DefaultMessage("One indicator can be selected for this type of layer")
  @Key("canIncludeSingleIndicator")
  String canIncludeSingleIndicator();

  /**
   * Translated "[show all {0} sites in {1}]".
   * 
   * @return translated "[show all {0} sites in {1}]"
   */
  @DefaultMessage("[show all {0} sites in {1}]")
  @Key("clickToShowAllSitesOfAdminEntity")
  String clickToShowAllSitesOfAdminEntity(String arg0,  String arg1);

  /**
   * Translated "Have you checked if a location already exists? \r\n\r\n Identical locations lead to bad quality reports. \r\n\r\n Please ensure there is not already a location in the area you specified.".
   * 
   * @return translated "Have you checked if a location already exists? \r\n\r\n Identical locations lead to bad quality reports. \r\n\r\n Please ensure there is not already a location in the area you specified."
   */
  @DefaultMessage("Have you checked if a location already exists? \r\n\r\n Identical locations lead to bad quality reports. \r\n\r\n Please ensure there is not already a location in the area you specified.")
  @Key("confirmAddLocation")
  String confirmAddLocation();

  /**
   * Translated "Are you sure you want to delete the database <b>{0}</b>? <br><br>You will loose all activities and indicator results.".
   * 
   * @return translated "Are you sure you want to delete the database <b>{0}</b>? <br><br>You will loose all activities and indicator results."
   */
  @DefaultMessage("Are you sure you want to delete the database <b>{0}</b>? <br><br>You will loose all activities and indicator results.")
  @Key("confirmDeleteDb")
  String confirmDeleteDb(String arg0);

  /**
   * Translated "The coordinate falls outside of the bounds of {0}".
   * 
   * @return translated "The coordinate falls outside of the bounds of {0}"
   */
  @DefaultMessage("The coordinate falls outside of the bounds of {0}")
  @Key("coordOutsideBounds")
  String coordOutsideBounds(String arg0);

  /**
   * Translated "edited {0}".
   * 
   * @return translated "edited {0}"
   */
  @DefaultMessage("edited {0}")
  @Key("edited")
  String edited(String arg0);

  /**
   * Translated "Last Sync''d: {0}".
   * 
   * @return translated "Last Sync''d: {0}"
   */
  @DefaultMessage("Last Sync''d: {0}")
  @Key("lastSynced")
  String lastSynced(String arg0);

  /**
   * Translated "{0} locations without GPS".
   * 
   * @return translated "{0} locations without GPS"
   */
  @DefaultMessage("{0} locations without GPS")
  @Key("locationsWithoutGps")
  String locationsWithoutGps(String arg0);

  /**
   * Translated "Found no existing locations. Make geography less specific, or add a new location.".
   * 
   * @return translated "Found no existing locations. Make geography less specific, or add a new location."
   */
  @DefaultMessage("Found no existing locations. Make geography less specific, or add a new location.")
  @Key("noLocationsFound")
  String noLocationsFound();

  /**
   * Translated "Database {0} has no projects defined".
   * 
   * @return translated "Database {0} has no projects defined"
   */
  @DefaultMessage("Database {0} has no projects defined")
  @Key("noProjectsDefinedForDatabase")
  String noProjectsDefinedForDatabase(String arg0);

  /**
   * Translated "No results found for search query \"{0}\".".
   * 
   * @return translated "No results found for search query \"{0}\"."
   */
  @DefaultMessage("No results found for search query \"{0}\".")
  @Key("noSearchResults")
  String noSearchResults(String arg0);

  /**
   * Translated "There is already data entered for the partner {0}. Before deleting this partner, you must delete the partner''s data.".
   * 
   * @return translated "There is already data entered for the partner {0}. Before deleting this partner, you must delete the partner''s data."
   */
  @DefaultMessage("There is already data entered for the partner {0}. Before deleting this partner, you must delete the partner''s data.")
  @Key("partnerHasDataWarning")
  String partnerHasDataWarning(String arg0);

  /**
   * Translated "There is already data entered for the project {0}. Before deleting this project, you must delete the project''s data. ".
   * 
   * @return translated "There is already data entered for the project {0}. Before deleting this project, you must delete the project''s data. "
   */
  @DefaultMessage("There is already data entered for the project {0}. Before deleting this project, you must delete the project''s data. ")
  @Key("projectHasDataWarning")
  String projectHasDataWarning(String arg0);

  /**
   * Translated "Projects for database {0}<<<<<<< HEAD".
   * 
   * @return translated "Projects for database {0}<<<<<<< HEAD"
   */
  @DefaultMessage("Projects for database {0}<<<<<<< HEAD")
  @Key("projectsForDatabase")
  String projectsForDatabase(String arg0);

  /**
   * Translated "{0,number,integer}Q{1,number,integer}".
   * 
   * @return translated "{0,number,integer}Q{1,number,integer}"
   */
  @DefaultMessage("{0}Q{1}")
  @Key("quarter")
  String quarter(int year,  int quarter);

  @DefaultMessage("{0,date,MM}")
  String month(Date month);
  
  /**
   * Translated "{0} most recent added sites for search query".
   * 
   * @return translated "{0} most recent added sites for search query"
   */
  @DefaultMessage("{0} most recent added sites for search query")
  @Key("recentlyAddedSites")
  String recentlyAddedSites(String arg0);

  /**
   * Translated "Nothing entered to search on: please enter something you want to search for".
   * 
   * @return translated "Nothing entered to search on: please enter something you want to search for"
   */
  @DefaultMessage("Nothing entered to search on: please enter something you want to search for")
  @Key("searchQueryEmpty")
  String searchQueryEmpty();

  /**
   * Translated "Enter a search query with at least 3 characters".
   * 
   * @return translated "Enter a search query with at least 3 characters"
   */
  @DefaultMessage("Enter a search query with at least 3 characters")
  @Key("searchQueryTooShort")
  String searchQueryTooShort();

  /**
   * Translated "For query \"{0}\", found {1} databases, {2} activities and {3} indicators".
   * 
   * @return translated "For query \"{0}\", found {1} databases, {2} activities and {3} indicators"
   */
  @DefaultMessage("For query \"{0}\", found {1} databases, {2} activities and {3} indicators")
  @Key("searchResultsFound")
  String searchResultsFound(String arg0,  String arg1,  String arg2,  String arg3);

  /**
   * Translated "Showing locks for database [{0}], project [{1}] for activity [{2}]".
   * 
   * @return translated "Showing locks for database [{0}], project [{1}] for activity [{2}]"
   */
  @DefaultMessage("Showing locks for database [{0}], project [{1}] for activity [{2}]")
  @Key("showLockedPeriodsTitle")
  String showLockedPeriodsTitle(String arg0,  String arg1,  String arg2);

  /**
   * Translated "Synchronizing {0} [{1} rows updated so far] ".
   * 
   * @return translated "Synchronizing {0} [{1} rows updated so far] "
   */
  @DefaultMessage("Synchronizing {0} [{1} rows updated so far] ")
  @Key("synchronizerProgress")
  String synchronizerProgress(String arg0,  String arg1);

  /**
   * Translated "Too many ({0}) locations found. Please refine criteria to see possible locations.  ".
   * 
   * @return translated "Too many ({0}) locations found. Please refine criteria to see possible locations.  "
   */
  @DefaultMessage("Too many ({0}) locations found. Please refine criteria to see possible locations.  ")
  @Key("tooManyLocationsFound")
  String tooManyLocationsFound(String arg0);

  /**
   * Translated "ActivityInfo r{0}".
   * 
   * @return translated "ActivityInfo r{0}"
   */
  @DefaultMessage("ActivityInfo r{0}")
  @Key("versionedActivityInfoTitle")
  String versionedActivityInfoTitle(String arg0);
}
