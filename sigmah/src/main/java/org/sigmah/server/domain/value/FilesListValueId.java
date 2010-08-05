package org.sigmah.server.domain.value;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Embeddable
public class FilesListValueId implements java.io.Serializable {

	private static final long serialVersionUID = 7309173168011463617L;

	private Long idList;
	private Long idFile;

	public FilesListValueId() {
	}

	public FilesListValueId(Long idList, Long fileId) {
		this.idList = idList;
		this.idFile = fileId;
	}

	@Column(name = "id_files_list", nullable = false)
	public Long getIdList() {
		return this.idList;
	}

	public void setIdList(Long idList) {
		this.idList = idList;
	}

	@Column(name = "id_file", nullable = false)
	public Long getIdFile() {
		return this.idFile;
	}

	public void setIdFile(Long fileId) {
		this.idFile = fileId;
	}
}
