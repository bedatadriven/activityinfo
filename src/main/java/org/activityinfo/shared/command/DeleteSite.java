package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.VoidResult;

public class DeleteSite implements MutatingCommand<VoidResult> {
	private int id;

	public DeleteSite(int id) {
		super();
		this.id = id;
	}

	public DeleteSite() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeleteSite other = (DeleteSite) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DeleteSite [id=" + id + "]";
	}
}
