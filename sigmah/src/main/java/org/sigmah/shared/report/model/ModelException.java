package org.sigmah.shared.report.model;

/**
 * Exception relating to the (mal)definition of a report model
 * 
 * @author Alex Bertram
 *
 */
public class ModelException extends RuntimeException  {

	public ModelException() {
		super();
	}

	public ModelException(String message, Throwable cause, ReportElement element) {
		super(appendElementDetails(message, element), cause);
	}

	public ModelException(String message, ReportElement element) {
		super(appendElementDetails(message, element));
	}

	public ModelException(Throwable cause, ReportElement element) {
		super(appendElementDetails("", element), cause);
	}

	private static String appendElementDetails(String message, ReportElement element) {
		try {
			message += "In " + element.getClass().toString() + ", ";
			if(element.getTitle() == null) {
				message += " untitled";
			} else {
				message += " titled '" + element.getTitle() + "'."; 
			}
		} catch(Throwable caught) { 
		}
		return message;
	}
}
