/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project.reports;

import com.extjs.gxt.ui.client.widget.Label;
import org.sigmah.client.i18n.I18N;

/**
 * Count and display the number of key questions and how many are valids.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class KeyQuestionState {
    private int valids;
    private int count;
    private Label label;

    public void clear() {
        valids = 0;
        count = 0;
        label = null;
    }

    public Label getLabel() {
        if(label == null) {
            label = new Label();
            updateLabel();
        }
        return label;
    }

    public void increaseCount() {
        this.count++;
    }

    public void increaseValids() {
        this.valids++;

        if(label != null)
            updateLabel();
    }

    public void decreaseValids() {
        this.valids--;

        if(label != null)
            updateLabel();
    }

    private void updateLabel() {
        label.setText(I18N.MESSAGES.reportKeyQuestions(Integer.toString(valids), Integer.toString(count)));
    }
}
