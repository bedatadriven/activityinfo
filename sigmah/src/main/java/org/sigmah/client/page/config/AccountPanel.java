/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.config;

import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import org.sigmah.client.i18n.I18N;

public class AccountPanel extends FormPanel implements AccountEditor.View {

    public AccountPanel() {

        this.setHeading(I18N.CONSTANTS.mySettings());

        FieldSet userInfoSet = new FieldSet();
        userInfoSet.setHeading(I18N.CONSTANTS.userInformation());

        TextField<String> userNameField = new TextField<String>();
        userNameField.setFieldLabel(I18N.CONSTANTS.yourName());
        userInfoSet.add(userNameField);

        TextField<String> userEmailField = new TextField<String>();
        userEmailField.setFieldLabel(I18N.CONSTANTS.yourEmail());
        userInfoSet.add(userEmailField);

        ComboBox localeCombo = new ComboBox();
        localeCombo.setFieldLabel(I18N.CONSTANTS.locale());
        localeCombo.setDisplayField("name");
        localeCombo.setValueField("code");
    }
}
