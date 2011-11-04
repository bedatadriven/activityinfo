package org.sigmah.shared.util.mapping;

public enum Published {

	NOT_PUBLISHED(0), ALL_ARE_PUBLISHED(1), ONLY_SOME_SITES_ARE_PUBLISHED(2);

	private final int index;

	private Published(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
}