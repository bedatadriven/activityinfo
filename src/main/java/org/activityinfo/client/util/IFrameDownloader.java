package org.activityinfo.client.util;

import com.extjs.gxt.ui.client.core.El;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Subclass of Downloader that initiates the download
 * via an iframe for browsers that will allow this.
 *
 */
public class IFrameDownloader extends Downloader {

	@Override
	public void initiateDownload(String url) {
		com.google.gwt.user.client.ui.Frame frame = new com.google.gwt.user.client.ui.Frame(url);
		El el = El.fly(frame.getElement());
		el.setStyleAttribute("width", 0);
		el.setStyleAttribute("height", 0);
		el.setStyleAttribute("position", "absolute");
		el.setStyleAttribute("border", 0);
		RootPanel.get().add(frame);
	}

}
