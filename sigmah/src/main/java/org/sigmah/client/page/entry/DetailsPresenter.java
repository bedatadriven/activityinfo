/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry;

import org.sigmah.client.AppEvents;
import org.sigmah.client.EventBus;
import org.sigmah.client.event.SiteEvent;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.page.common.Shutdownable;
import org.sigmah.client.page.entry.form.SiteRenderer;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.inject.Inject;

public class DetailsPresenter implements Shutdownable {

    public interface View {
        public void setHtml(String html);
        void setSelectionTitle(String title);
    }

    private final EventBus eventBus;
    private final ActivityDTO activity;
    private final UIConstants messages;
    private final View view;
    private final NumberFormat indicatorFormat;

    private SiteDTO currentSite;

    private boolean showEmptyRows = false;

    private Listener<SiteEvent> siteListener;
	private SiteRenderer siteRenderer = new SiteRenderer();

    @Inject
    public DetailsPresenter(EventBus eventBus, ActivityDTO activity, UIConstants messages, View view) {
        this.eventBus = eventBus;
        this.activity = activity;
        this.messages = messages;
        this.view = view;

        indicatorFormat = NumberFormat.getFormat("#,###");

        siteListener = new Listener<SiteEvent>() {
            public void handleEvent(SiteEvent be) {
                if(be.getType() == AppEvents.SiteSelected && be.getSite() != null) {
                    onSiteSelected(be.getSite());
                } else if(be.getType() == AppEvents.SiteChanged) {
                    if(currentSite.getId() == be.getSite().getId()) {
                        onSiteSelected(be.getSite());
                    }
                }
            }
        };
        eventBus.addListener(AppEvents.SiteSelected, siteListener);
        eventBus.addListener(AppEvents.SiteChanged, siteListener);
    }


    public void shutdown() {
        eventBus.removeListener(AppEvents.SiteSelected, siteListener);
        eventBus.removeListener(AppEvents.SiteChanged, siteListener);
    }

    private void onSiteSelected(SiteDTO site) {

        this.currentSite = site;
        view.setSelectionTitle(site.getLocationName());
        view.setHtml(siteRenderer.renderSite(site, activity, showEmptyRows, true));
    }

}
