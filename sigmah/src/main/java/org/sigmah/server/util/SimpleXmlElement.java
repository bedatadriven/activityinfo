package org.sigmah.server.util;

public class SimpleXmlElement {

	public String namespace;
	public String name;
	public StringBuilder text;
	
	public SimpleXmlElement(String namespace, String name) {
		this.namespace = namespace;
		this.name = name;
		this.text = new StringBuilder();
	}
	
	public SimpleXmlElement(String namespace, String name, String text) {
		this.namespace = namespace;
		this.name = name;
		this.text = new StringBuilder(text);
	}
	
	
}
