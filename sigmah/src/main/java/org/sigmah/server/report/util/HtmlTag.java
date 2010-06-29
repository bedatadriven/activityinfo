/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HtmlTag {

	private String name;
	private StringBuilder innerText = new StringBuilder(0);
	private boolean closed;
	private Map<String, HtmlAttribute> attributes = new HashMap<String, HtmlAttribute>();
	
	public HtmlTag(String name) {
		this.name = name;
		
	}
	
	
	public String getName() { 
		return this.name;
	}
	
	protected HtmlAttribute getAttribute(String name) {
		HtmlAttribute attrib = attributes.get(name);
		
		if(attrib == null) {
			attrib = new HtmlAttribute(name);
			attributes.put(name, attrib);
		}
		return attrib;
	}
	
	public HtmlTag at(String name, String value) {
		getAttribute(name).setValue(value);
		return this;
	}


	public HtmlTag at(String name, int value) {
		getAttribute(name).setValue(value);
		return this;
	}
	
	public HtmlTag styleName(String className) {
		if(className != null) {
			getAttribute("class").append(className, ' ');
		}
		return this;
	}
	
	public HtmlTag styleName(String className, int suffix) {
		return styleName(className + suffix);
	}
	
	public HtmlTag styleNameIf(String className, boolean condition) {
		if(condition) {
			styleName(className);
		}
		return this;
	}

	public Collection<HtmlAttribute> getAttributes() {
		return attributes.values();
	}
	
	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}
	
	public HtmlTag text(String text) {
		innerText.append(text);
		return this;
	}
	
	public HtmlTag text(Collection<?> elements, String delimeter) {

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
	
	public HtmlTag nbsp() {
		innerText.append("&nbsp;");
		return this;
	}
	
	public HtmlTag close() {
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
	

}
