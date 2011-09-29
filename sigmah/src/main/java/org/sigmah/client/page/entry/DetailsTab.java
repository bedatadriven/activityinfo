/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry;

import org.sigmah.client.i18n.I18N;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class DetailsTab extends TabItem implements DetailsPresenter.View {
    private ContentPanel panel;
    private Html html;

    public DetailsTab() {
        initializeComponent();

        createPanel();
        createHtml();
    }

	private void createHtml() {
		html = new Html();
        html.setStyleName("details");
        panel.add(html);
	}

	private void createPanel() {
		panel = new ContentPanel();
        panel.setHeading(I18N.CONSTANTS.details());
        panel.setScrollMode(Style.Scroll.AUTOY);
        panel.setLayout(new FitLayout());
        add(panel);
	}

	private void initializeComponent() {
		setText(I18N.CONSTANTS.details());
        setLayout(new FitLayout());
	}

    public void setSelectionTitle(String title) {
        panel.setHeading(I18N.CONSTANTS.details() + " - " +  title);
    }

    public void setHtml(String content) {
        html.setHtml(content);
    }
}
