/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project.reports;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RichTextArea;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.project.reports.images.ToolbarImages;
import org.sigmah.client.ui.FoldPanel;
import org.sigmah.shared.dto.report.KeyQuestionDTO;

/**
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class KeyQuestionDialog {
    private static Dialog keyQuestionDialog;

    private static Dialog getDialog() {
        if(keyQuestionDialog == null) {
            final Dialog dialog = new Dialog();
            dialog.setButtons(Dialog.OKCANCEL);
            dialog.setHeading(I18N.CONSTANTS.reportCreateReport());
            dialog.setModal(true);

            dialog.setWidth("640px");
            dialog.setResizable(false);

            dialog.setLayout(new RowLayout(Orientation.VERTICAL));

            // Question label
            final Label questionLabel = new Label("key-question");
            questionLabel.addStyleName("project-report-key-question-label");
            dialog.add(questionLabel);

            // Text area
            final RichTextArea textArea = new RichTextArea();
            textArea.setStyleName("project-report-key-question");
            dialog.add(textArea);

            // Cancel button
            dialog.getButtonById(Dialog.CANCEL).addSelectionListener(new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent ce) {
                    dialog.hide();
                }
            });

            keyQuestionDialog = dialog;
        }
        return keyQuestionDialog;
    }

    public static Dialog getDialog(final KeyQuestionDTO keyQuestion, final RichTextArea textArea,
            final FoldPanel panel, final int toolButtonIndex, final KeyQuestionState keyQuestionState) {
        final Dialog dialog = getDialog();

        // Question label
        final Label question = (Label) dialog.getWidget(0);
        question.setText(keyQuestion.getLabel());

        // Rich text editor
        final RichTextArea dialogTextArea = (RichTextArea) dialog.getWidget(1);
        dialogTextArea.setHTML(textArea.getHTML());

        final boolean wasValid = !"".equals(textArea.getText());

        // OK Button
        final Button okButton = dialog.getButtonById(Dialog.OK);

        okButton.removeAllListeners();
        okButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                dialog.hide();
                textArea.setHTML(dialogTextArea.getHTML());

                final boolean isValid = !"".equals(dialogTextArea.getText());
                
                final ToolbarImages images = GWT.create(ToolbarImages.class);
                if(isValid) {
                    panel.setToolButtonImage(toolButtonIndex, images.compasGreen());
                    
                    if(!wasValid)
                        keyQuestionState.increaseValids();

                } else {
                    panel.setToolButtonImage(toolButtonIndex, images.compasRed());

                    if(wasValid)
                        keyQuestionState.decreaseValids();
                }
            }
        });

        return dialog;
    }
}
