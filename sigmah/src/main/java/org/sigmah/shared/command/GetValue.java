/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

//import org.sigmah.server.domain.element.FlexibleElement;
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
	/*
	 * Do not use the getClass().getName() on client side to identify a 
	 * flexible element type.
	 * Always use the getEntityName() !
	 */
	private String elementEntityName;

        private Integer amendmentId;
	
	public GetValue() {
		// required, or serialization exception
	}
	
	public GetValue(int projectId, long elementId, String elementEntityName) {
		this.projectId = projectId;
		this.elementId = elementId;
		this.elementEntityName = elementEntityName;
	}

        public GetValue(int projectId, long elementId, String elementEntityName, Integer amendmentId) {
		this(projectId, elementId, elementEntityName);
                this.amendmentId = amendmentId;
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
	
	public String getElementEntityName() {
		return elementEntityName;
	}
	
	public void setElementEntityName(String elementClassName) {
		this.elementEntityName = elementClassName;
	}

	public Integer getAmendmentId() {
		return amendmentId;
	}


	public void setAmendmentId(Integer amendmentId) {
		this.amendmentId = amendmentId;
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
		if (elementEntityName == null) {
			if (other.elementEntityName != null)
				return false;
		} else if (!elementEntityName.equals(other.elementEntityName))
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
		toString.append(";elementEntityName=").append(elementEntityName);
		toString.append("]");
		return toString.toString();
	}

}

