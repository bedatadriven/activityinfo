/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.exception;

import org.sigmah.shared.report.model.ReportElement;

/**
 * Exception relating to the (mal)definition of a report model
 * 
 * @author Alex Bertram
 *
 */
public class ReportModelException extends RuntimeException  {

	public ReportModelException() {
		super();
	}

	public ReportModelException(String message, Throwable cause, ReportElement element) {
		super(appendElementDetails(message, element), cause);
	}

	public ReportModelException(String message, ReportElement element) {
		super(appendElementDetails(message, element));
	}

	public ReportModelException(Throwable cause, ReportElement element) {
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
