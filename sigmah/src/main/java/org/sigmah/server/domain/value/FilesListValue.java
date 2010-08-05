package org.sigmah.server.domain.value;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "files_list_value")
public class FilesListValue implements Serializable {

	private static final long serialVersionUID = 1632642808467119061L;

	private FilesListValueId id;
	private Long idList;
	private File file;

	@EmbeddedId
	@AttributeOverrides({
							@AttributeOverride(name = "idList", column = @Column(name = "id_files_list", nullable = false)),
							@AttributeOverride(name = "idFile", column = @Column(name = "id_file", nullable = false)) })
	public FilesListValueId getId() {
		return this.id;
	}

	public void setId(FilesListValueId id) {
		this.id = id;
	}

	public void setIdList(Long idList) {
		this.idList = idList;
	}

	@Column(name = "id_files_list", nullable = false, insertable = false, updatable = false)
	public Long getIdList() {
		return idList;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_file", nullable = false, insertable = false, updatable = false)
	public File getFile() {
		return file;
	}
}
