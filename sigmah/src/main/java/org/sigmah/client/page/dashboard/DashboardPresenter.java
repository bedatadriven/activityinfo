/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.page.dashboard;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import org.sigmah.client.EventBus;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.TabPage;
import org.sigmah.client.page.charts.ChartPageState;
import org.sigmah.client.page.config.DbListPageState;
import org.sigmah.client.page.entry.SiteGridPageState;
import org.sigmah.client.page.map.MapPageState;
import org.sigmah.client.page.report.ReportHomePageState;
import org.sigmah.client.page.table.PivotPageState;

/**
 * Home screen of sigmah. Displays the main menu and a reminder of urgent tasks.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class DashboardPresenter implements Page, TabPage {
    public static final PageId PAGE_ID = new PageId("welcome");
    
    private EventBus eventBus;
    private Widget widget;

    @Inject
    public DashboardPresenter(EventBus eventBus) {
        this.eventBus = eventBus;
        
        final ContentPanel dashboardPanel = new ContentPanel(new BorderLayout());
        dashboardPanel.setHeaderVisible(false);
        dashboardPanel.setBorders(false);
        
        // Left bar
        final ContentPanel leftPanel = new ContentPanel();
        final VBoxLayout leftPanelLayout = new VBoxLayout();
        leftPanelLayout.setVBoxLayoutAlign(VBoxLayout.VBoxLayoutAlign.STRETCH);
        leftPanelLayout.setPadding(new Padding(0));
        leftPanel.setLayout(leftPanelLayout);
        leftPanel.setHeaderVisible(false);
        leftPanel.setBorders(false);
        leftPanel.setBodyBorder(false);
        
            // Left bar content
            final VBoxLayoutData vBoxLayoutData = new VBoxLayoutData();
            vBoxLayoutData.setFlex(1.0);
        
            final ContentPanel remindersPanel = new ContentPanel(new FitLayout());
            remindersPanel.setHeading(I18N.CONSTANTS.reminders());
            leftPanel.add(remindersPanel, vBoxLayoutData);
            
            final ContentPanel importantPointsPanel = new ContentPanel(new FitLayout());
            importantPointsPanel.setHeading(I18N.CONSTANTS.importantPoints());
            leftPanel.add(importantPointsPanel, vBoxLayoutData);
            
            final ContentPanel menuPanel = new ContentPanel();
            final VBoxLayout menuPanelLayout = new VBoxLayout();
            menuPanelLayout.setVBoxLayoutAlign(VBoxLayout.VBoxLayoutAlign.STRETCH);
            menuPanel.setLayout(menuPanelLayout);
            menuPanel.setHeading(I18N.CONSTANTS.menu());
            
                // Menu
                addNavLink(menuPanel, I18N.CONSTANTS.dataEntry(), IconImageBundle.ICONS.dataEntry(), new SiteGridPageState());
                addNavLink(menuPanel, I18N.CONSTANTS.reports(), IconImageBundle.ICONS.report(), new ReportHomePageState());
                addNavLink(menuPanel, I18N.CONSTANTS.charts(), IconImageBundle.ICONS.barChart(), new ChartPageState());
                addNavLink(menuPanel, I18N.CONSTANTS.maps(), IconImageBundle.ICONS.map(), new MapPageState());
                addNavLink(menuPanel, I18N.CONSTANTS.tables(), IconImageBundle.ICONS.table(), new PivotPageState());
                addNavLink(menuPanel, I18N.CONSTANTS.setup(), IconImageBundle.ICONS.setup(), new DbListPageState());
        
            leftPanel.add(menuPanel, vBoxLayoutData);
            
        final BorderLayoutData leftLayoutData = new BorderLayoutData(LayoutRegion.WEST, 250);
        leftLayoutData.setSplit(true);
//        leftLayoutData.setCollapsible(true);
        dashboardPanel.add(leftPanel, leftLayoutData);
            
        // Main panel
        final ContentPanel mainPanel = new ContentPanel();
        mainPanel.setHeading(I18N.CONSTANTS.indicators());
        mainPanel.setBorders(false);
                
        final BorderLayoutData mainLayoutData = new BorderLayoutData(LayoutRegion.CENTER);
        dashboardPanel.add(mainPanel, mainLayoutData);
        
        widget = dashboardPanel;
    }
    
    private void addNavLink(ContentPanel panel, String text, AbstractImagePrototype icon, final PageState place) {
        final Button button = new Button(text, icon, new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, place));
            }
        });
        
        final VBoxLayoutData vBoxLayoutData = new VBoxLayoutData();
        vBoxLayoutData.setFlex(1.0);
        panel.add(button, vBoxLayoutData);
    }
    
//    public static Folder getTreeModel() {
//    Folder[] folders = new Folder[] {
//        new Folder("Beethoven", new Folder[] {
//
//            new Folder("Quartets", new Music[] {
//                new Music("Six String Quartets", "Beethoven", "Quartets"),
//                new Music("Three String Quartets", "Beethoven", "Quartets"),
//                new Music("Grosse Fugue for String Quartets", "Beethoven", "Quartets"),}),
//            new Folder("Sonatas", new Music[] {
//                new Music("Sonata in A Minor", "Beethoven", "Sonatas"),
//                new Music("Sonata in F Major", "Beethoven", "Sonatas"),}),
//
//            new Folder("Concertos", new Music[] {
//                new Music("No. 1 - C", "Beethoven", "Concertos"),
//                new Music("No. 2 - B-Flat Major", "Beethoven", "Concertos"),
//                new Music("No. 3 - C Minor", "Beethoven", "Concertos"),
//                new Music("No. 4 - G Major", "Beethoven", "Concertos"),
//                new Music("No. 5 - E-Flat Major", "Beethoven", "Concertos"),}),
//
//            new Folder("Symphonies", new Music[] {
//                new Music("No. 1 - C Major", "Beethoven", "Symphonies"),
//                new Music("No. 2 - D Major", "Beethoven", "Symphonies"),
//                new Music("No. 3 - E-Flat Major", "Beethoven", "Symphonies"),
//                new Music("No. 4 - B-Flat Major", "Beethoven", "Symphonies"),
//                new Music("No. 5 - C Minor", "Beethoven", "Symphonies"),
//                new Music("No. 6 - F Major", "Beethoven", "Symphonies"),
//                new Music("No. 7 - A Major", "Beethoven", "Symphonies"),
//                new Music("No. 8 - F Major", "Beethoven", "Symphonies"),
//                new Music("No. 9 - D Minor", "Beethoven", "Symphonies"),}),}),
//        new Folder("Brahms", new Folder[] {
//            new Folder("Concertos", new Music[] {
//                new Music("Violin Concerto", "Brahms", "Concertos"),
//                new Music("Double Concerto - A Minor", "Brahms", "Concertos"),
//                new Music("Piano Concerto No. 1 - D Minor", "Brahms", "Concertos"),
//                new Music("Piano Concerto No. 2 - B-Flat Major", "Brahms", "Concertos"),}),
//            new Folder("Quartets", new Music[] {
//                new Music("Piano Quartet No. 1 - G Minor", "Brahms", "Quartets"),
//                new Music("Piano Quartet No. 2 - A Major", "Brahms", "Quartets"),
//                new Music("Piano Quartet No. 3 - C Minor", "Brahms", "Quartets"),
//                new Music("String Quartet No. 3 - B-Flat Minor", "Brahms", "Quartets"),}),
//            new Folder("Sonatas", new Music[] {
//                new Music("Two Sonatas for Clarinet - F Minor", "Brahms", "Sonatas"),
//                new Music("Two Sonatas for Clarinet - E-Flat Major", "Brahms", "Sonatas"),}),
//            new Folder("Symphonies", new Music[] {
//                new Music("No. 1 - C Minor", "Brahms", "Symphonies"),
//                new Music("No. 2 - D Minor", "Brahms", "Symphonies"),
//                new Music("No. 3 - F Major", "Brahms", "Symphonies"),
//                new Music("No. 4 - E Minor", "Brahms", "Symphonies"),}),}),
//        new Folder("Mozart", new Folder[] {new Folder("Concertos", new Music[] {
//            new Music("Piano Concerto No. 12", "Mozart", "Concertos"),
//            new Music("Piano Concerto No. 17", "Mozart", "Concertos"),
//            new Music("Clarinet Concerto", "Mozart", "Concertos"),
//            new Music("Violin Concerto No. 5", "Mozart", "Concertos"),
//            new Music("Violin Concerto No. 4", "Mozart", "Concertos"),}),}),};
//
//    Folder root = new Folder("root");
//    for (int i = 0; i < folders.length; i++) {
//      root.add((Folder) folders[i]);
//    }
//
//    return root;
//  }


    @Override
    public PageId getPageId() {
        return PAGE_ID;
    }

    @Override
    public Object getWidget() {
        return widget;
    }

    @Override
    public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    @Override
    public boolean navigate(PageState place) {
        return true;
    }

    @Override
    public void shutdown() {
    }

    @Override
    public String getTabTitle() {
        return I18N.CONSTANTS.dashboard();
    }
}
