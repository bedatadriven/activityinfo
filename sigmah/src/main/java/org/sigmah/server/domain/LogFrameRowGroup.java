package org.sigmah.server.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class LogFrameRowGroup implements Serializable {
	private static final long serialVersionUID = 3108497343483391507L;
	
	private int id;
	private String header;
	private int sortOrder;
	private List<LogFrameRow> rows;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getHeader() {
		return header;
	}
	
	public void setHeader(String header) {
		this.header = header;
	}
	
	public int getSortOrder() {
		return sortOrder;
	}
	
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	@OneToMany
	@org.hibernate.annotations.OrderBy(clause = "sortOrder")
	public List<LogFrameRow> getRows() {
		return rows;
	}
	
	public void setRows(List<LogFrameRow> rows) {
		this.rows = rows;
	}
}
