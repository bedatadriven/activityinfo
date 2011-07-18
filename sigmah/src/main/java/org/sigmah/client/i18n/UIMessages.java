package org.sigmah.client.i18n;

/**
 * Interface to represent the messages contained in resource bundle:
 * 	/home/alex/dev/activityinfo/sigmah/src/main/java/org/sigmah/client/i18n/UIMessages.properties'.
 */
public interface UIMessages extends com.google.gwt.i18n.client.Messages {
  
  /**
   * Translated "Activities completed after {0}".
   * 
   * @return translated "Activities completed after {0}"
   */
  @DefaultMessage("Activities completed after {0}")
  @Key("activitiesCompletedAfter")
  String activitiesCompletedAfter(String arg0);

  /**
   * Translated "Activities completed before {0}".
   * 
   * @return translated "Activities completed before {0}"
   */
  @DefaultMessage("Activities completed before {0}")
  @Key("activitiesCompletedBefore")
  String activitiesCompletedBefore(String arg0);

  /**
   * Translated "Activities completed between {0} and {1}".
   * 
   * @return translated "Activities completed between {0} and {1}"
   */
  @DefaultMessage("Activities completed between {0} and {1}")
  @Key("activitiesCompletedBetween")
  String activitiesCompletedBetween(String arg0,  String arg1);

  /**
   * Translated "{0} at {1}".
   * 
   * @return translated "{0} at {1}"
   */
  @DefaultMessage("{0} at {1}")
  @Key("activityAt")
  String activityAt(String arg0,  String arg1);

  /**
   * Translated "{0} - {1}".
   * 
   * @return translated "{0} - {1}"
   */
  @DefaultMessage("{0} - {1}")
  @Key("activityTitle")
  String activityTitle(String arg0,  String arg1);

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
   * Translated "Amendment #{0}.{1}".
   * 
   * @return translated "Amendment #{0}.{1}"
   */
  @DefaultMessage("Amendment #{0}.{1}")
  @Key("amendmentName")
  String amendmentName(String arg0,  String arg1);

  /**
   * Translated "Are you sure you want to delete the activity at {0} ?".
   * 
   * @return translated "Are you sure you want to delete the activity at {0} ?"
   */
  @DefaultMessage("Are you sure you want to delete the activity at {0} ?")
  @Key("confirmDelete")
  String confirmDelete(String arg0);

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
   * Translated "Are you sure you want to delete the file ''{0}'' ?".
   * 
   * @return translated "Are you sure you want to delete the file ''{0}'' ?"
   */
  @DefaultMessage("Are you sure you want to delete the file ''{0}'' ?")
  @Key("flexibleElementFilesListConfirmDelete")
  String flexibleElementFilesListConfirmDelete(String arg0);

  /**
   * Translated "Are you sure you want to delete the version #{0} ?".
   * 
   * @return translated "Are you sure you want to delete the version #{0} ?"
   */
  @DefaultMessage("Are you sure you want to delete the version #{0} ?")
  @Key("flexibleElementFilesListConfirmVersionDelete")
  String flexibleElementFilesListConfirmVersionDelete(String arg0);

  /**
   * Translated "{0} file(s) max.".
   * 
   * @return translated "{0} file(s) max."
   */
  @DefaultMessage("{0} file(s) max.")
  @Key("flexibleElementFilesListLimitReached")
  String flexibleElementFilesListLimitReached(String arg0);

  /**
   * Translated "Are you sure you want to remove the indicator ''{0}'' from the list ?".
   * 
   * @return translated "Are you sure you want to remove the indicator ''{0}'' from the list ?"
   */
  @DefaultMessage("Are you sure you want to remove the indicator ''{0}'' from the list ?")
  @Key("flexibleElementIndicatorsListConfirmRemove")
  String flexibleElementIndicatorsListConfirmRemove(String arg0);

  /**
   * Translated "This element is linked to the category ''{0}''.".
   * 
   * @return translated "This element is linked to the category ''{0}''."
   */
  @DefaultMessage("This element is linked to the category ''{0}''.")
  @Key("flexibleElementQuestionCategory")
  String flexibleElementQuestionCategory(String arg0);

  /**
   * Translated "This element is linked to the quality criterion ''{0}''.".
   * 
   * @return translated "This element is linked to the quality criterion ''{0}''."
   */
  @DefaultMessage("This element is linked to the quality criterion ''{0}''.")
  @Key("flexibleElementQuestionQuality")
  String flexibleElementQuestionQuality(String arg0);

  /**
   * Translated "Enter or select a date between {0} and {1} (inclusive interval).".
   * 
   * @return translated "Enter or select a date between {0} and {1} (inclusive interval)."
   */
  @DefaultMessage("Enter or select a date between {0} and {1} (inclusive interval).")
  @Key("flexibleElementTextAreaDateRange")
  String flexibleElementTextAreaDateRange(String arg0,  String arg1);

  /**
   * Translated "Enter a number value ({0}) between {1} and {2} (inclusive interval).".
   * 
   * @return translated "Enter a number value ({0}) between {1} and {2} (inclusive interval)."
   */
  @DefaultMessage("Enter a number value ({0}) between {1} and {2} (inclusive interval).")
  @Key("flexibleElementTextAreaNumberRange")
  String flexibleElementTextAreaNumberRange(String arg0,  String arg1,  String arg2);

  /**
   * Translated "Enter your text (maximum {0} characters allowed).".
   * 
   * @return translated "Enter your text (maximum {0} characters allowed)."
   */
  @DefaultMessage("Enter your text (maximum {0} characters allowed).")
  @Key("flexibleElementTextAreaTextLength")
  String flexibleElementTextAreaTextLength(String arg0);

  /**
   * Translated "Last Sync''d: {0}".
   * 
   * @return translated "Last Sync''d: {0}"
   */
  @DefaultMessage("Last Sync''d: {0}")
  @Key("lastSynced")
  String lastSynced(String arg0);

  /**
   * Translated "Do you want to add a monitored point to the file ''{0}'' ?".
   * 
   * @return translated "Do you want to add a monitored point to the file ''{0}'' ?"
   */
  @DefaultMessage("Do you want to add a monitored point to the file ''{0}'' ?")
  @Key("monitoredPointAddWithFile")
  String monitoredPointAddWithFile(String arg0);

  /**
   * Translated "New {0}".
   * 
   * @return translated "New {0}"
   */
  @DefaultMessage("New {0}")
  @Key("newSite")
  String newSite(String arg0);

  /**
   * Translated "There is already data entered for the partner {0}. Before deleting this partner, you must delete the partner''s data.".
   * 
   * @return translated "There is already data entered for the partner {0}. Before deleting this partner, you must delete the partner''s data."
   */
  @DefaultMessage("There is already data entered for the partner {0}. Before deleting this partner, you must delete the partner''s data.")
  @Key("partnerHasDataWarning")
  String partnerHasDataWarning(String arg0);

  /**
   * Translated "Activate the phase ''{0}''".
   * 
   * @return translated "Activate the phase ''{0}''"
   */
  @DefaultMessage("Activate the phase ''{0}''")
  @Key("projectActivate")
  String projectActivate(String arg0);

  /**
   * Translated "Are you sure you want to close the phase ''{0}'' and to activate the phase ''{1}'' ? The closed phase cannot be changed later.".
   * 
   * @return translated "Are you sure you want to close the phase ''{0}'' and to activate the phase ''{1}'' ? The closed phase cannot be changed later."
   */
  @DefaultMessage("Are you sure you want to close the phase ''{0}'' and to activate the phase ''{1}'' ? The closed phase cannot be changed later.")
  @Key("projectCloseAndActivate")
  String projectCloseAndActivate(String arg0,  String arg1);

  /**
   * Translated "Are you sure you want to close the phase ''{0}'' and ends the project ?".
   * 
   * @return translated "Are you sure you want to close the phase ''{0}'' and ends the project ?"
   */
  @DefaultMessage("Are you sure you want to close the phase ''{0}'' and ends the project ?")
  @Key("projectEnd")
  String projectEnd(String arg0);

  /**
   * Translated "Private draft. Last saved the {0} at {1}.".
   * 
   * @return translated "Private draft. Last saved the {0} at {1}."
   */
  @DefaultMessage("Private draft. Last saved the {0} at {1}.")
  @Key("reportDraftHeader")
  String reportDraftHeader(String arg0,  String arg1);

  /**
   * Translated "Key-Question #{0}".
   * 
   * @return translated "Key-Question #{0}"
   */
  @DefaultMessage("Key-Question #{0}")
  @Key("reportKeyQuestionDialogTitle")
  String reportKeyQuestionDialogTitle(String arg0);

  /**
   * Translated "Key-Questions answered : {0}/{1}".
   * 
   * @return translated "Key-Questions answered : {0}/{1}"
   */
  @DefaultMessage("Key-Questions answered : {0}/{1}")
  @Key("reportKeyQuestions")
  String reportKeyQuestions(String arg0,  String arg1);

  /**
   * Translated "Open Report ''{0}''".
   * 
   * @return translated "Open Report ''{0}''"
   */
  @DefaultMessage("Open Report ''{0}''")
  @Key("reportOpenReport")
  String reportOpenReport(String arg0);

  /**
   * Translated "Do you really want to remove the report ''{0}''?".
   * 
   * @return translated "Do you really want to remove the report ''{0}''?"
   */
  @DefaultMessage("Do you really want to remove the report ''{0}''?")
  @Key("reportRemoveConfirm")
  String reportRemoveConfirm(String arg0);

  /**
   * Translated "Please select the ''{0}'' first.".
   * 
   * @return translated "Please select the ''{0}'' first."
   */
  @DefaultMessage("Please select the ''{0}'' first.")
  @Key("selectLevelFirst")
  String selectLevelFirst(String arg0);

  /**
   * Translated "{0} sites are missing geographic coordinates and will not appear on the map.".
   * 
   * @return translated "{0} sites are missing geographic coordinates and will not appear on the map."
   */
  @DefaultMessage("{0} sites are missing geographic coordinates and will not appear on the map.")
  @Key("sitesMissingCoordinates")
  String sitesMissingCoordinates(String arg0);

  /**
   * Translated "Synchronizing {0} [{1} rows updated so far]".
   * 
   * @return translated "Synchronizing {0} [{1} rows updated so far]"
   */
  @DefaultMessage("Synchronizing {0} [{1} rows updated so far]")
  @Key("synchronizerProgress")
  String synchronizerProgress(String arg0,  String arg1);

  /**
   * Translated "ActivityInfo r{0}".
   * 
   * @return translated "ActivityInfo r{0}"
   */
  @DefaultMessage("ActivityInfo r{0}")
  @Key("versionedActivityInfoTitle")
  String versionedActivityInfoTitle(String arg0);
}
