package org.sigmah.client.page.project.logframe;

import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.dto.logframe.SpecificObjectiveDTO;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;

public class ProjectLogFrameActionMenu {

    /**
     * CSS style name for the entire menu.
     */
    private static final String CSS_LOG_FRAME_MENU_STYLE_NAME = "logframe-menu";

    /**
     * CSS style name for the menu button.
     */
    private static final String CSS_MENU_BUTTON_STYLE_NAME = CSS_LOG_FRAME_MENU_STYLE_NAME + "-button";

    public ProjectLogFrameActionMenu(SpecificObjectiveDTO specificObjective) {

        // Menu.
        final Menu menu = new Menu();

        final MenuItem upMenuItem = new MenuItem("monter", IconImageBundle.ICONS.up());
        upMenuItem.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                Window.alert("clicked!");
            }
        });

        menu.add(upMenuItem);
        menu.add(new MenuItem("descendre", IconImageBundle.ICONS.down()));
        menu.add(new MenuItem("supprimer", IconImageBundle.ICONS.delete()));

        final Anchor anchor = new Anchor("\u25BC");
        anchor.addStyleName(CSS_MENU_BUTTON_STYLE_NAME);
        anchor.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                menu.show(anchor);
            }
        });
    }

}
