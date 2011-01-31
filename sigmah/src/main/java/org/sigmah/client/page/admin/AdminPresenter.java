package org.sigmah.client.page.admin;

import org.sigmah.client.EventBus;
import org.sigmah.client.UserInfo;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.Frame;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.TabPage;
import org.sigmah.client.page.admin.users.AdminUsersPresenter;
import org.sigmah.client.page.project.SubPresenter;
import org.sigmah.client.ui.ToggleAnchor;
import org.sigmah.client.util.state.IStateManager;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

/**
 * Loads data for administration screen.
 * 
 * @author nrebiai
 * 
 */
public class AdminPresenter implements TabPage, Frame {
	
    private final static String[] MAIN_TABS = {I18N.CONSTANTS.adminUsers()};
	public static final PageId PAGE_ID = new PageId(I18N.CONSTANTS.adminboard());
	
    private final View view;

    private final SubPresenter[] presenters;
    private AdminPageState currentState;
    private Page activePage;
    
    
	@ImplementedBy(AdminView.class)
    public interface View {
		
		public void setMainPanel(Widget widget);

		public ContentPanel getTabPanel();

    }
	
	@Inject
    public AdminPresenter(final EventBus eventBus, final Dispatcher dispatcher, 
    		final View view, final UserInfo info,
            final Authentication authentication, IStateManager stateMgr) {
        this.view = view;
        this.presenters = new SubPresenter[] {
        		new AdminUsersPresenter(dispatcher, info),
        };
        
        for (int i = 0; i < MAIN_TABS.length; i++) {
            final int index = i;

            String tabTitle = MAIN_TABS[i];

            final HBoxLayoutData layoutData = new HBoxLayoutData();
            layoutData.setMargins(new Margins(0, 10, 0, 0));

            final ToggleAnchor anchor = new ToggleAnchor(tabTitle);
            anchor.setAnchorMode(true);

            anchor.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, currentState
                            .deriveTo(index)));
                }
            });

            this.view.getTabPanel().add(anchor, layoutData);
        }
	}
	
	@Override
	public void shutdown() {
	}

	@Override
	public PageId getPageId() {
		return PAGE_ID;
	}

	@Override
	public Object getWidget() {
		 return view;
	}

	@Override
	public void requestToNavigateAway(PageState place,
			NavigationCallback callback) {
		callback.onDecided(true);
	}

	@Override
	public String beforeWindowCloses() {
		return null;
	}

	@Override
	public void setActivePage(Page page) {
		this.activePage = page;
		
	}

	@Override
	public Page getActivePage() {
		return this.activePage;
	}
	
	@Override
	public AsyncMonitor showLoadingPlaceHolder(PageId pageId,
			PageState loadingPlace) {
		return null;
	}

	@Override
	public String getTabTitle() {
		return I18N.CONSTANTS.adminboard();
	}
	
	@Override
	public boolean navigate(PageState place) {
		final AdminPageState adminPageState = (AdminPageState) place;
		currentState = adminPageState;

        selectTab(adminPageState.getCurrentSection(), false);
		return true;
	}
	
	private void selectTab(int index, boolean force) {
        /*final ToggleAnchor anchor = (ToggleAnchor) view.getTabPanel().getWidget(index);

        if (currentTab != anchor) {
            if (currentTab != null)
                currentTab.toggleAnchorMode();

            anchor.toggleAnchorMode();
            currentTab = anchor;*/
			Log.debug("AdminPresenter getting view of SubPresenter " + index);
            view.setMainPanel(presenters[index].getView());
            presenters[index].viewDidAppear();
       /* } else if (force) {
            view.setMainPanel(presenters[index].getView());
            presenters[index].viewDidAppear();
        }*/
    }
}
