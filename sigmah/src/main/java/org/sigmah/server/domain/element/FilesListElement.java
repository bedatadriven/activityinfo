package org.sigmah.server.domain.element;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Files list element entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "files_list_element")
public class FilesListElement extends FlexibleElement {

	private static final long serialVersionUID = 4866208826790848338L;

}
