/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import org.sigmah.server.domain.element.FlexibleElement;
import org.sigmah.shared.command.result.ValueResult;


/**
 * Retrieves the value of a {@link FlexibleElement} referenced in a given project.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public class GetValue implements Command<ValueResult> {
	
	private static final long serialVersionUID = 5675515456984800856L;
	
	private int projectId;
	private long elementId;
	private String elementClassName;
	
	public GetValue() {
		// required, or serialization exception
	}
	
	public GetValue(int projectId, long elementId, String elementClassName) {
		this.projectId = projectId;
		this.elementId = elementId;
		this.elementClassName = elementClassName;
	}

	public int getProjectId() {
		return projectId;
	}
	
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	public long getElementId() {
		return elementId;
	}
	
	public void setElementId(long elementId) {
		this.elementId = elementId;
	}
	
	public String getElementClassName() {
		return elementClassName;
	}
	
	public void setElementClassName(String elementClassName) {
		this.elementClassName = elementClassName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GetValue other = (GetValue) obj;
		if (elementClassName == null) {
			if (other.elementClassName != null)
				return false;
		} else if (!elementClassName.equals(other.elementClassName))
			return false;
		if (elementId != other.elementId)
			return false;
		if (projectId != other.projectId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder();
		toString.append("GetValue:[projectId=").append(projectId);
		toString.append(";elementId=").append(elementId);
		toString.append(";elementClassName=").append(elementClassName);
		toString.append("]");
		return toString.toString();
	}

}

