/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.ui;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.animation.client.Animation;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import org.sigmah.client.EventBus;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.page.NavigationHandler;

/**
 * This widget displays a tab bar. Tabs are maintained by a {@link TabModel}.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class TabBar extends Composite {
    public final static String TAB_ID_PREFIX = "tab";
    
    private EventBus eventBus;
    private TabModel model;
    private final HorizontalPanel tabContainer;
    
    private int selectedIndex = -1;

    // Scrolling
    private int direction = 1;
    private int initialPosition;
    private double distance;
    private int leftTabIndex;

    /**
     * Creates a new TabBar using the given TabModel.
     * @param model
     */
    public TabBar(final TabModel model, EventBus eventBus) {
        this.eventBus = eventBus;

        final AbsolutePanel scrollPanel = new AbsolutePanel();
        scrollPanel.setSize("100%", "100%");

        tabContainer = new HorizontalPanel();
        tabContainer.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        tabContainer.setHeight("100%");

        scrollPanel.add(tabContainer, 0, 0);

        final Animation animation  = new Animation() {
            @Override
            protected void onUpdate(double progress) {
                int x = (int) (distance * progress) * direction + initialPosition;

                scrollPanel.setWidgetPosition(tabContainer, x, 0);
            }
        };

        final Button scrollLeftButton = new Button();
        scrollLeftButton.addStyleName("tab-button-left");
        RootPanel.get("arrows").add(scrollLeftButton);

        final Button scrollRightButton = new Button();
        scrollRightButton.addStyleName("tab-button-right");
        RootPanel.get("arrows").add(scrollRightButton);

        scrollLeftButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int currentPosition = scrollPanel.getWidgetLeft(tabContainer);
                distance = initialPosition + distance*direction - currentPosition;
                if(distance < 0)
                    distance = -distance;

                if(leftTabIndex > 0) {
                    leftTabIndex--;
                    distance += getTabWidth(TAB_ID_PREFIX, model.get(leftTabIndex).getId());
                }

                direction = 1;

                initialPosition = currentPosition;
                animation.run(200);
            }
        });

        scrollRightButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int currentPosition = scrollPanel.getWidgetLeft(tabContainer);
                distance = initialPosition + distance*direction - currentPosition;
                if(distance < 0)
                    distance = -distance;

                if(leftTabIndex < model.size()-1) {
                    distance += getTabWidth(TAB_ID_PREFIX, model.get(leftTabIndex).getId());
                    leftTabIndex++;
                }

                direction = -1;

                initialPosition = currentPosition;
                animation.run(200);
            }
        });

        linkWith(model);
        this.model = model;
        
        initWidget(scrollPanel);
//        initWidget(tabContainer);
    }

    public TabModel getModel() {
        return model;
    }

    private void linkWith(final TabModel model) {
        final int size = model.size();
        for(int index = 0; index < size; index++) {
            addTab(model.get(index));
        }
        
        model.addListener(new TabModel.Listener() {
            @Override
            public void tabAdded(Tab t) {
                addTab(t);
            }

            @Override
            public void tabChanged(int index) {
                final Tab tab = model.get(index);
                
                final AbsolutePanel panel = (AbsolutePanel) tabContainer.getWidget(index);
                final HTML title = (HTML) panel.getWidget(1);
                title.setHTML(tab.getTitle()); // Warning, this is code injection sensitive.
                
                setSelectedIndex(index);
            }

            @Override
            public void tabRemoved(int index) {
                removeTab(index);
                
                if(index == selectedIndex) {
                    if(index == model.size())
                        index--;

                    if(index != -1)
                        displayTab(model.get(index));

                    setSelectedIndex(index);
                }
            }
        });
    }
    
    /**
     * Display the given tab.
     * @param tab the Tab to display.
     */
    protected void displayTab(final Tab tab) {
        eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, tab.getState()));
    }
    
    private void addTab(final Tab tab) {
        // If you change this method, remember to change "linkWith>tabChanged" too.
        // The current "tabChanged" listener waits for a specific widget organization (AbsolutePanel>HTML)
        
        final AbsolutePanel panel = new AbsolutePanel();
        panel.setStylePrimaryName("tab");

        panel.getElement().setId(TAB_ID_PREFIX + tab.getId());

        final SimplePanel leftBorder = new SimplePanel();
        leftBorder.addStyleName("left");

        final HTML title = new HTML(tab.getTitle());
        title.addStyleName("center");

        final SimplePanel rightBorder = new SimplePanel();
        rightBorder.addStyleName("right");

        title.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                displayTab(tab);
                setSelectedIndex(model.indexOf(tab));
            }
        });

        panel.add(leftBorder);
        panel.add(title);
        panel.add(rightBorder);

        if(tab.isCloseable()) {
            final FocusPanel closeButton = new FocusPanel();
            closeButton.setStyleName("tab-close");

            closeButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    model.remove(tab);
                }
            });

            panel.add(closeButton);
        }

        tabContainer.add(panel);

        setSelectedIndex(tabContainer.getWidgetCount()-1);
    }

    private void removeTab(final int index) {
        tabContainer.remove(index);
    }

    public void addTabStyleName(int index, String style) {
        tabContainer.getWidget(index).addStyleName(style);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }
    
    public void setSelectedIndex(int index) {
        if(selectedIndex != -1 && selectedIndex < tabContainer.getWidgetCount())
            tabContainer.getWidget(selectedIndex).removeStyleDependentName("active");
            
        selectedIndex = index;
        if(index != -1)
            tabContainer.getWidget(selectedIndex).addStyleDependentName("active");
    }

    private native int getTabWidth(String prefix, int id) /*-{
        var width = 0;

        var element = $wnd.document.getElementById(prefix + id);
        var style = $wnd.getComputedStyle(element, null);
        width += parseInt(style.width) +
                  parseInt(style.borderLeftWidth) +
                  parseInt(style.borderRightWidth) +
                  parseInt(style.marginLeft) +
                  parseInt(style.marginRight) +
                  parseInt(style.paddingLeft) +
                  parseInt(style.paddingRight);

        return width;
    }-*/;
}
