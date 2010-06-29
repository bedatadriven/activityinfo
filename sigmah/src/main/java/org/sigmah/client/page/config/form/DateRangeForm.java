/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.config.form;

import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import org.sigmah.client.Application;

public class DateRangeForm extends FormPanel {

	private DateField fromDate;
	private DateField toDate;
	
	public DateRangeForm() {

		fromDate = new DateField();
		fromDate.setFieldLabel(Application.CONSTANTS.fromDate());
		add(fromDate);
		                              
		toDate = new DateField();
		toDate.setFieldLabel(Application.CONSTANTS.toDate());
		add(toDate);
		
	}
	

}
