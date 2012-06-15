/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page;

import com.extjs.gxt.ui.client.core.El;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Initiates a download of a file via an IFRAME.
 */
public final class Downloader {

	
   
    public static void initiateDownload(String url) {

		// This is a workaround for IE8 
		// http://stackoverflow.com/questions/4310441/ie8s-information-bar-blocking-a-scripted-file-download-in-response-to-jquery-aj

        // launch download in IFRAME (so our app window is not disturbed
        // if the connection fails before the client receives the Content-disposition header
        Frame frame = new Frame();
        
        String frameName = "idownload" + System.currentTimeMillis();
        
        El el = El.fly(frame.getElement());
		el.setElementAttribute("name", frameName);
        el.setStyleAttribute("width", 0);
		el.setStyleAttribute("height", 0);
		el.setStyleAttribute("position", "absolute");
		el.setStyleAttribute("border", 0);
		
		RootPanel.get().add(frame);
    
		Window.open(url, frameName, null);		
	
         // todo: feedback to user and monitor transfer, if possible.
    }
}

