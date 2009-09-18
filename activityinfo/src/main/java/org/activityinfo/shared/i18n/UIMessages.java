package org.activityinfo.shared.i18n;

/**
 * Interface to represent the messages contained in resource bundle:
 * 	C:/Users/Owner/Eclipse/activityinfo/src/main/java/org/activityinfo/shared/i18n/UIMessages.properties'.
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
   * Translated "Please select the ''{0}'' first.".
   * 
   * @return translated "Please select the ''{0}'' first."
   */
  @DefaultMessage("Please select the ''{0}'' first.")
  @Key("selectLevelFirst")
  String selectLevelFirst(String arg0);
}
