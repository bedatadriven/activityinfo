package org.sigmah.client.page.entry;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import org.sigmah.client.Application;

public class DetailsTab extends TabItem implements DetailsPresenter.View {

    private ContentPanel panel;
    private Html html;

    public DetailsTab() {

        setText(Application.CONSTANTS.details());
        setLayout(new FitLayout());

        panel = new ContentPanel();
        panel.setHeading(Application.CONSTANTS.details());
        panel.setScrollMode(Style.Scroll.AUTOY);
        panel.setLayout(new FitLayout());
        add(panel);

        html = new Html();
        html.setStyleName("details");
        panel.add(html);
    }

    public void setSelectionTitle(String title) {
        panel.setHeading(Application.CONSTANTS.details() + " - " +  title);

    }

    public void setHtml(String content) {
        html.setHtml(content);
    }
}
