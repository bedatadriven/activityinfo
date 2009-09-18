package org.activityinfo.shared.soundex;

public interface SoundexCoder {

	public String encode(String text, boolean preserveSpaces);
}
