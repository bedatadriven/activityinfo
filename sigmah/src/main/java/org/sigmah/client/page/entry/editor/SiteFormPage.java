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

	public static final PageId PAGEID = new PageId("site");
    private final ToolBar toolBar = new ToolBar();
	private final Dispatcher service;
	private SiteDTO currentSite;					
	private SchemaDTO schema;
	private final  EventBus eventBus;
	
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
//                if (presenter != null) {
//                    presenter.onUIAction(ce.getComponent().getItemId());
//                }
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
		return PAGEID;
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
	public boolean navigate(PageState place) {
		assert(place instanceof SitePageState);
		if (place instanceof SitePageState) {
			final SitePageState siteState = (SitePageState) place;
			
			service.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void onSuccess(SchemaDTO result) {
					SiteFormPage.this.schema=result;
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
							createSiteForm();
						}

					});
				}
			});

			return true;
		}
		return false;
	}
	private void createSiteForm() {
		this.removeAll();
		SiteForm siteForm = new SiteForm();
		ActivityDTO activity = schema.getActivityById(currentSite.getActivityId());
		SiteFormPresenter presenter = new SiteFormPresenter(eventBus, service, activity, siteForm);
		presenter.setSite(currentSite);
		add(siteForm);
		layout();
	}
	
	public static class SitePageState implements PageState {
		private int siteId;
		
		public int getSiteId() {
			return siteId;
		}

		public SitePageState(int siteId) {
			this.siteId = siteId;
		}

		@Override
		public String serializeAsHistoryToken() {
			return Integer.toString(siteId);
		}

		@Override
		public PageId getPageId() {
			return PAGEID;
		}

		@Override
		public List<PageId> getEnclosingFrames() {
			return Collections.singletonList(PAGEID);
		}
	}
	
	public static class SitePageStateParser implements PageStateParser {

		@Override
		public PageState parse(String token) {
			return new SitePageState(Integer.parseInt(token));
		}
		
	}
	

}
