/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * This widget displays a tab bar. Tabs are maintained by a {@link TabModel}.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class TabBar extends Composite {
    private TabModel model;
    private final HorizontalPanel tabContainer;
    
    private int selectedIndex = -1;
    
    /**
     * Creates a new TabBar and a new empty TabModel.
     */
    public TabBar() {
        this(new TabModel());
    }
    
    /**
     * Creates a new TabBar using the given TabModel.
     * @param model
     */
    public TabBar(final TabModel model) {
        this.model = model;
        model.addListener(new TabModel.Listener() {

            @Override
            public void tabAdded(Tab t) {
                addTab(t);
            }

            @Override
            public void tabChanged(int index) {
                setSelectedIndex(index);
            }

            @Override
            public void tabRemoved(int index) {
                removeTab(index);
                
                if(index == selectedIndex) {
                    if(index > 0)
                        index--;
                    else if(model.size() > 0)
                        index = 0;
                    else
                        index = -1;

                    if(index != -1)
                        displayTab(model.get(index));

                    setSelectedIndex(index);
                }
            }
        });
        
        final SimplePanel bar = new SimplePanel();
        bar.setStyleName("tab-bar");
        
        tabContainer = new HorizontalPanel();
        bar.setWidget(tabContainer);
        
        tabContainer.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        tabContainer.setHeight("100%");
        
        initWidget(bar);
    }

    public TabModel getModel() {
        return model;
    }

//    public void setModel(TabModel model) {
//        if(this.model != null)
//            this.model.removeListener(null); <-- Finish this
//        
//        this.model = model;
//        
//        tabContainer.clear();
//        
//        final int size = model.size();
//        for(int index = 0; index < size; index++) {
//            addTab(model.get(index));
//        }
//    }
    
    /**
     * Display the given tab.
     * @param tab the Tab to display.
     */
    protected void displayTab(final Tab tab) {
        History.newItem(tab.getAction());
    }
    
    private void addTab(final Tab tab) {
        final AbsolutePanel panel = new AbsolutePanel();
        panel.setStyleName("tab");

        final HTML title = new HTML(tab.getTitle());
        title.setWidth("100%");
        title.setHeight("100%");

        title.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                displayTab(tab);
                setSelectedIndex(model.indexOf(tab));
            }
        });

        panel.add(title);

        if(tab.isCloseable()) {
            final FocusPanel closeButton = new FocusPanel();
            closeButton.setStyleName("tab-close");

            closeButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    model.remove(tab);
                }
            });

            panel.add(closeButton, 136, 5);
        }

        tabContainer.add(panel);

        setSelectedIndex(tabContainer.getWidgetCount()-1);
    }
    
    private void removeTab(final int index) {
        tabContainer.remove(index);
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
}
