package org.sigmah.client.page.config;

import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import org.sigmah.client.Application;

public class AccountPanel extends FormPanel implements AccountEditor.View {

    public AccountPanel() {

        this.setHeading(Application.CONSTANTS.mySettings());

        FieldSet userInfoSet = new FieldSet();
        userInfoSet.setHeading(Application.CONSTANTS.userInformation());

        TextField<String> userNameField = new TextField<String>();
        userNameField.setFieldLabel(Application.CONSTANTS.yourName());
        userInfoSet.add(userNameField);

        TextField<String> userEmailField = new TextField<String>();
        userEmailField.setFieldLabel(Application.CONSTANTS.yourEmail());
        userInfoSet.add(userEmailField);

        ComboBox localeCombo = new ComboBox();
        localeCombo.setFieldLabel(Application.CONSTANTS.locale());
        localeCombo.setDisplayField("name");
        localeCombo.setValueField("code");

        // this.add(userInfoSet);


    }


}
