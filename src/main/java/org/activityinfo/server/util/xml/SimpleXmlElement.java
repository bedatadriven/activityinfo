

package org.activityinfo.server.util.xml;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

public class SimpleXmlElement {

	private String namespace;
	private String name;
	private StringBuilder text;
	
	public SimpleXmlElement(String namespace, String name) {
		this.setNamespace(namespace);
		this.setName(name);
		this.setText(new StringBuilder());
	}
	
	public SimpleXmlElement(String namespace, String name, String text) {
		this.setNamespace(namespace);
		this.setName(name);
		this.setText(new StringBuilder(text));
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setText(StringBuilder text) {
		this.text = text;
	}

	public StringBuilder getText() {
		return text;
	}
}
