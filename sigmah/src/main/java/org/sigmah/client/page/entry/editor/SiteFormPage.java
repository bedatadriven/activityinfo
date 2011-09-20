/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor;

import java.util.Collections;
import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;
import org.sigmah.client.page.common.dialog.SaveChangesCallback;
import org.sigmah.client.page.common.dialog.SavePromptMessageBox;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteFormPage extends ContentPanel implements Page {

	public static final PageId EDIT_PAGE_ID = new PageId("site");
	public static final PageId NEW_PAGE_ID = new PageId("newSite");
	
	
    private final ToolBar toolBar = new ToolBar();

	private final Dispatcher service;
	private SiteDTO currentSite;					
	private SchemaDTO schema;
	private final  EventBus eventBus;
	private SiteFormPresenter presenter;
	
    @Inject
    public SiteFormPage(Dispatcher service, EventBus eventBus) {
		this.service=service;
		this.eventBus=eventBus;
		
		createToolBar();
		setLayout(new FitLayout());
	}


	private void createToolBar() {

        SelectionListener listener = new SelectionListener() {
            @Override
            public void componentSelected(ComponentEvent ce) {
                if (presenter != null) {
                    presenter.onUIAction(ce.getComponent().getItemId());
                }
            }
        };

        Button gridButton = new Button(I18N.CONSTANTS.returnToGrid(), IconImageBundle.ICONS.table(), listener);
        gridButton.setItemId(UIActions.gotoGrid);
        toolBar.add(gridButton);

        Button saveButton = new Button(I18N.CONSTANTS.save(), IconImageBundle.ICONS.save(), listener);
        saveButton.setItemId(UIActions.save);
        toolBar.add(saveButton);

        Button discardButton = new Button(I18N.CONSTANTS.discardChanges(),
                IconImageBundle.ICONS.cancel(), listener);
        discardButton.setItemId(UIActions.cancel);
        toolBar.add(discardButton);
        setTopComponent(toolBar);

    }


    public void promptSaveChanges(final SaveChangesCallback callback) {

        final SavePromptMessageBox box = new SavePromptMessageBox();

        box.show(callback);

    }


	@Override
	public void shutdown() {
		
	}


	@Override
	public PageId getPageId() {
		return EDIT_PAGE_ID;
	}


	@Override
	public Object getWidget() {
		return this;
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
	public boolean navigate(final PageState place) {

		if(!(place instanceof SiteFormPageState)) {
			return false;
		}
		
		service.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}		

			@Override
			public void onSuccess(SchemaDTO result) {
				schema = result;
				if (place instanceof EditPageState) {
					editSite((EditPageState) place);
				} else if(place instanceof NewPageState) {
					newSite((NewPageState) place);
				}
			}
		
		});

		return true;
	}


	private void editSite(final EditPageState siteState) {
		GetSites getSites= GetSites.byId(siteState.getSiteId());
		service.execute(getSites, null, new AsyncCallback<SiteResult>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO: handler failure
				System.out.println();
			}

			@Override
			public void onSuccess(SiteResult result) {
				SiteFormPage.this.currentSite=result.getData().get(0);
				createSiteForm(schema.getActivityById(currentSite.getActivityId()));
				presenter.setSite(currentSite);
			}
		});
	}
	
	
	private void newSite(NewPageState place) {
		ActivityDTO activity = schema.getActivityById(place.getActivityId());

		currentSite = new SiteDTO();
		currentSite.setActivityId(place.getActivityId());
        
		if (!activity.getDatabase().isEditAllAllowed()) {
			currentSite.setPartner(activity.getDatabase().getMyPartner());
        }

		createSiteForm(activity);
		presenter.setSite(currentSite);
	}

	
	private void createSiteForm(ActivityDTO activity) {
		this.removeAll();
		SiteForm siteForm = new SiteForm(service, eventBus);
		presenter = new SiteFormPresenter(eventBus, service, activity, siteForm);
		add(siteForm);
		layout();
	}
	
	
	/**
	 * Marker interface for this form's supported PageStates
	 *
	 */
	public interface SiteFormPageState extends PageState {
		
	}
	
	public static class EditPageState implements SiteFormPageState {
		private int siteId;
		
		public int getSiteId() {
			return siteId;
		}

		public EditPageState(int siteId) {
			this.siteId = siteId;
		}

		@Override
		public String serializeAsHistoryToken() {
			return Integer.toString(siteId);
		}

		@Override
		public PageId getPageId() {
			return EDIT_PAGE_ID;
		}

		@Override
		public List<PageId> getEnclosingFrames() {
			return Collections.singletonList(EDIT_PAGE_ID);
		}
	}
	
	
	public static class NewPageState implements SiteFormPageState {
		private int activityId;
		
		public int getActivityId() {
			return activityId;
		}

		public NewPageState(int activityId) {
			this.activityId = activityId;
		}

		@Override
		public String serializeAsHistoryToken() {
			return Integer.toString(activityId);
		}

		@Override
		public PageId getPageId() {
			return NEW_PAGE_ID;
		}

		@Override
		public List<PageId> getEnclosingFrames() {
			return Collections.singletonList(NEW_PAGE_ID);
		}
	}
	
	
	public static class EditPageStateParser implements PageStateParser {

		@Override
		public PageState parse(String token) {
			return new EditPageState(Integer.parseInt(token));
		}
	}

	public static class NewStateParser implements PageStateParser {

		@Override
		public PageState parse(String token) {
			return new NewPageState(Integer.parseInt(token));
		}
	}

		

}
