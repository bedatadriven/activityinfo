/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.dialog;

public abstract class FormDialogCallback {

	/**
	 * Called after the user has clicked save and the form has been 
	 * validated
	 */
	public void onValidated() {

    }

    public void onValidated(FormDialogTether dlg) {
        onValidated();
    }

	public void onCancelled() {
		
	}
}
