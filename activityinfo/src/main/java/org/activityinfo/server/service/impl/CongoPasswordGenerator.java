package org.activityinfo.server.service.impl;

import com.google.inject.Singleton;
import org.activityinfo.server.service.PasswordGenerator;

import java.util.Random;

@Singleton
public class CongoPasswordGenerator implements PasswordGenerator {

	private static Random random = new Random();
	
	private static final String[] dict = new String[] { "bleu", "kivu", "congo", "roi", "fleuve", "kasongo", 
        "maniema", "kansimba", "hakuna", "jambo", "mbote", "mwana", "watoto", "kwanza", "mtoto",
        "habari", "mzuri", "wazuri", "gani", "magote", "mai", "juin", "juillet", "janvier", "septembre",
        "novembre" };

	@Override
	public String generate() {
		return dict[ random.nextInt(dict.length) ] + random.nextInt(999);
	}
}
