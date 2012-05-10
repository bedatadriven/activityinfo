package org.activityinfo.shared.dto;

public enum Published {

	NOT_PUBLISHED(0), ALL_ARE_PUBLISHED(1), ONLY_SOME_SITES_ARE_PUBLISHED(2);

	private final int index;

	private Published(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public static Published fromIndex(int index) {
		for(Published value : values()) {
			if(value.getIndex() == index) {
				return value;
			}
		}
		throw new IllegalArgumentException(Integer.toString(index));
	}
}