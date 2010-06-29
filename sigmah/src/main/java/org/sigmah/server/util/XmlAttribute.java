package org.sigmah.server.util;


public class XmlAttribute {

	private String namespace = null;
	private String name;
	private StringBuilder value = new StringBuilder();
	
	public XmlAttribute(String namespace, String name) {
		this.name = name;
		this.namespace = namespace;
	}


	public XmlAttribute(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value.toString();
	}

	protected void clear() { 
		if(value.length()!=0) {
			value = new StringBuilder();
		}
	}
	
	public void setValue(String value) {
		clear();
		this.value.append(value);
	}
	
	public void setValue(int value) {
		clear();
		this.value.append(value);
	}
	
	public XmlAttribute append(String value) {
		this.value.append(value);
		return this;
	}
	
	public XmlAttribute append(String value, char delimeter) {
		if(this.value.length() != 0) {
			this.value.append(delimeter);
		}
		this.value.append(value);
		return this;
	}

	public String getNamespace() {
		return namespace;
	}
	
}
