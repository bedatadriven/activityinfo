package org.activityinfo.client.page.base;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.Style;
import com.google.inject.Inject;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.page.PageManager;
import org.activityinfo.client.page.base.GalleryView;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class GalleryPage extends LayoutContainer implements GalleryView {

    protected ListStore<GalleryModel> store;
    protected Html heading;
    protected Html introPara;

    public static class GalleryModel extends BaseModelData {

        private Place place;

        public GalleryModel(String name, String desc, String path, Place place) {
            set("name", name);
            set("path", path);
            set("desc", desc);

            this.place = place;
        }

        public Place getPlace() {
            return place;
        }
    }

    private final EventBus eventBus;

    @Inject
    public GalleryPage(final EventBus eventBus) {

        this.eventBus = eventBus;
        this.setStyleName("gallery");
        this.setScrollMode(Style.Scroll.AUTOY);

        setLayout(new FlowLayout());

        heading = new Html();
        heading.setTagName("h3");
        add(heading);

        introPara = new Html();
        introPara.setTagName("p");
        introPara.setStyleName("gallery-intro");
        add(introPara);

        store = new ListStore<GalleryModel>();

        ListView<GalleryModel> view = new ListView<GalleryModel>();
        view.setTemplate(getTemplate(""));
        view.setBorders(false);
        view.setStore(store);
        view.setItemSelector("dd");
        view.setOverStyle("over");

        view.addListener(Events.Select,
            new Listener<ListViewEvent<GalleryModel>>() {

                public void handleEvent(ListViewEvent<GalleryModel> event) {

                    eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested,
                            event.getModel().getPlace()));

                }
            });
        add(view);
    }

    public void setHeading(String html) {
        heading.setHtml(html);
    }

    public void setIntro(String html) {
        introPara.setHtml(html);
    }

    @Override
    public void add(String name, String desc, String path, Place place) {

        store.add(new GalleryModel(name, desc, path, place));
    }

    private native String getTemplate(String base) /*-{
              return ['<dl><tpl for=".">',
              '<dd>',
              '<img src="' + base + 'thumbs/{path}" title="{name}">',
              '<div>',
              '<h4>{name}</h4><p>{desc}</p></div>',
              '</tpl>',
              '<div style="clear:left;"></div></dl>'].join("");

              }-*/;

}
