package org.sigmah.shared.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class LogFrame implements Serializable {
	private static final long serialVersionUID = 3670543377662206665L;
	
	private int id;
	private List<LogFrameRowGroup> groups;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@OneToMany
	@org.hibernate.annotations.OrderBy(clause = "sortOrder")
	public List<LogFrameRowGroup> getGroups() {
		return groups;
	}
	
	public void setGroups(List<LogFrameRowGroup> groups) {
		this.groups = groups;
	}
}
