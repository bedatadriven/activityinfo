package org.activityinfo.shared.command.result;

public class XmlResult implements SingleResult<String> {

	String xml;
	
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
	
	public String getResult() {
		return getValue();
	}
	
}
