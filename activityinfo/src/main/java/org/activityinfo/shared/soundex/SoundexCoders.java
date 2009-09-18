package org.activityinfo.shared.soundex;

public class SoundexCoders {

	public static SoundexCoder createCoder(int countryId) {
		
		return new RdcCoder();
		
	}
	
}
