/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.util.xml;

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
