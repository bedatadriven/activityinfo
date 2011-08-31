package org.sigmah.shared.dto.portlets;

import java.io.Serializable;

public interface PortletDTO extends Serializable {
	String getName();
	String getDescription();
	int column();
}
