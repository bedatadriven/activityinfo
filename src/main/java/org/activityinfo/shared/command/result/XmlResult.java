/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command.result;

public class XmlResult implements SingleResult<String> {

	private String xml;
	
	public XmlResult() {
		
	}
	
	public XmlResult(String xml) {
		this.xml = xml;
	}
	
	public String getValue() {
		return xml;
	}
	
	public void setXml(String xml) {
		this.xml = xml;
	}
	
	@Override
	public String getResult() {
		return getValue();
	}
	
}
