package org.sigmah.server.domain.value;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.sigmah.server.domain.Project;
import org.sigmah.server.domain.User;
import org.sigmah.server.domain.element.CheckboxElement;
import org.sigmah.server.domain.element.FlexibleElement;
import org.sigmah.server.domain.element.TextAreaElement;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "value", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"id_flexible_element", "id_project" }) })
public class Value implements Serializable {

	private static final long serialVersionUID = -2578586689736955636L;

	private Long id;
	private FlexibleElement element;
	private Project parentProject;
	private String value;
	private User lastModificationUser;
	private Date lastModificationDate;
	private Character lastModificationAction;

	public void setId(Long id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_value")
	public Long getId() {
		return id;
	}

	public void setElement(FlexibleElement element) {
		this.element = element;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_flexible_element", nullable = false)
	public FlexibleElement getElement() {
		return element;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "value", nullable = true, length = 8192)
	public String getValue() {
		return value;
	}

	public void setParentProject(Project parentProject) {
		this.parentProject = parentProject;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_project", nullable = false)
	public Project getParentProject() {
		return parentProject;
	}

	public void setLastModificationUser(User lastModificationUser) {
		this.lastModificationUser = lastModificationUser;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_user_last_modif", nullable = false)
	public User getLastModificationUser() {
		return lastModificationUser;
	}

	public void setLastModificationDate(Date lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_last_modif", nullable = false)
	public Date getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationAction(Character lastModificationAction) {
		this.lastModificationAction = lastModificationAction;
	}

	@Column(name = "action_last_modif", nullable = false)
	public Character getLastModificationAction() {
		return lastModificationAction;
	}

	/**
	 * Returns if the value can be consider as <code>valid</code> (correctly
	 * filled) according to its type.
	 * 
	 * @return if the value is valid.
	 */
	@Transient
	public boolean isValid() {

		if (!element.isValidates()) {
			return true;
		}

		if (element instanceof CheckboxElement) {
			if (value.equals("true")) {
				return true;
			}
		} else if (element instanceof TextAreaElement) {
			if (!value.equals("")) {
				return true;
			}
		}

		return false;
	}
}
