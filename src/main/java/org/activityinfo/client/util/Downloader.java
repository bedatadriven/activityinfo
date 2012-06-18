/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.util;

import org.activityinfo.client.i18n.I18N;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.google.gwt.user.client.ui.Anchor;

/**
 * Initiates a download by presenting the user with a link
 * 
 * <p>Many browsers will block downloads that are not directly tied to 
 * a user click. In many cases, when a user clicks in our app, we have to first 
 * send something to the server via GWT-RPC, to which we receive the location
 * of the temporary file generated, and then we initiate the download. Since this
 * second download is no longer tied to the original click, some browsers will interpret
 * it as an unwanted download and it block it. 
 * 
 *
 */
public class Downloader {
   
    public void initiateDownload(String url) {

    	Dialog dialog = new Dialog();
    	dialog.setHeading(I18N.CONSTANTS.downloadReady());
    	dialog.setWidth(250);
    	dialog.setHeight(150);
    	dialog.setLayout(new CenterLayout());
    	
    	Anchor anchor = new Anchor();
    	anchor.setHref(url);
    	anchor.setText(I18N.CONSTANTS.clickToDownload());
    	dialog.add(anchor);
    	dialog.setButtons(Dialog.CLOSE);
    	dialog.setHideOnButtonClick(true);
    	dialog.show();
    }
}

