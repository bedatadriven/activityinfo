/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.report;

import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.XMLParser;

public class ReportXmlForm extends FormPanel {

	private TextArea xmlArea;
	
	public ReportXmlForm() {
		setFrame(false);  
		setLayout(new FitLayout());

		xmlArea = new TextArea();
		
		/* Enable horizontal scroll bar on all browsers */
		xmlArea.setStyleAttribute("whiteSpace", "pre");
		xmlArea.setStyleAttribute("overflow", "auto");
		
		this.add(xmlArea);
		
	}

	public String getXml() {
		return xmlArea.getValue();
	}

	public void setXml(String xml) {
		xmlArea.setValue(xml);
	}

	public String validateXml() {
		try {
			XMLParser.parse(getXml());
			
			return null;
			
		} catch(DOMException ex) {
		
			return ex.getMessage();
		}
	}
	
}
