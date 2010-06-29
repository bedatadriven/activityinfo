package org.sigmah.server.util;

import java.util.*;


public class XmlElement {

	private String name;
	private String namespace = null;
	
	private StringBuilder innerText = new StringBuilder(0);
	private boolean closed;
	private Map<String, XmlAttribute> attributes = new HashMap<String, XmlAttribute>();
	private List<XmlElement> children = new ArrayList<XmlElement>();
	
	private Map<String, String> namespacePrefixes = new HashMap<String, String>();
	
	public XmlElement(String name) {
		this.name = name;	
	}
	
	public XmlElement(String namespace, String name) {
		this.namespace = namespace;
		this.name = name;
	}
	
	public XmlElement nsPrefix(String prefix, String ns) {
		namespacePrefixes.put(ns, prefix);
		return this;
	}
	
	public String getName() { 
		return this.name;
	}
	
	protected XmlAttribute getAttribute(String namespace, String name) {
		String key = namespace + ":" + name;
		XmlAttribute attrib = attributes.get(key);
		
		if(attrib == null) {
			attrib = new XmlAttribute(namespace, name);

			attributes.put(key, attrib);
		}
		return attrib;
	}
	
	protected XmlAttribute getAttribute(String name) {
		XmlAttribute attrib = attributes.get(name);
		
		if(attrib == null) {
			attrib = new XmlAttribute(name);
			attributes.put(name, attrib);
		}
		return attrib;
	}
	
	public XmlElement at(String name, String value) {
		getAttribute(name).setValue(value);
		return this;
	}
	
	public XmlElement at(String namespace, String name, String value) {
		getAttribute(namespace, name).setValue(value);
		return this;
	}
	
	public XmlElement at(String name, boolean value) {
		getAttribute(name).setValue(value ? "true" : "false");
		return this;
	}
	
	public XmlElement styleName(String className) {
		if(className != null) {
			getAttribute("class").append(className, ' ');
		}
		return this;
	}
	
	public XmlElement styleName(String className, int suffix) {
		return styleName(className + suffix);
	}
	
	public XmlElement styleNameIf(String className, boolean condition) {
		if(condition) {
			styleName(className);
		}
		return this;
	}

	public Collection<XmlAttribute> getAttributes() {
		return attributes.values();
	}
	
	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}
	
	public XmlElement text(String text) {
		innerText.append(text);
		return this;
	}
	
	public XmlElement text(Collection<?> elements, String delimeter) {
		boolean first = true;
		for(Object element : elements) {
			if(!first) {
				innerText.append(delimeter);
			}
			innerText.append(element.toString());
			
			first = false;
		}
		return this;
	}
	
	public XmlElement nbsp() {
		innerText.append("&nbsp;");
		return this;
	}
	
	public XmlElement close() {
		closed = true;
		return this;
	}


	public String getInnerText() {
		if(innerText.length()==0) {
			return null;
		} else {
			return innerText.toString();
		}
	}

	public boolean isClosed() {
		return closed;
	}

	public String getNamespace() {
		return namespace == null ? "" : namespace;
	}
	
	public Map<String, String> getNamespacePrefixes() {
		return namespacePrefixes;
	}
}
