package org.activityinfo.client.page.entry.editor;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.client.AppEvents;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.client.event.SiteEvent;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.shared.command.CreateEntity;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.Bounds;
import org.activityinfo.shared.dto.PartnerModel;
import org.activityinfo.shared.dto.SiteModel;

import java.util.Map;

public class SiteFormPresenter implements SiteFormLeash {

    public interface View {

        public void init(SiteFormPresenter presenter,
                         ActivityModel activity,
                         ListStore<PartnerModel> partnerStore,
                         ListStore<SiteModel> assessmentStore);

        public void setSite(SiteModel site);

        public AdminFieldSetPresenter.View createAdminFieldSetView(ActivityModel activity);

        public MapPresenter.View createMapView();

        public boolean validate();

        public boolean isDirty();
        public Map<String, Object> getChanges();
        public  Map<String, Object> getPropertyMap();


        public AsyncMonitor getMonitor();

        public void setActionEnabled(String actionId, boolean enabled);

        public void hide();

    }

    private final EventBus eventBus;
    private final CommandService service;

    private SiteModel currentSite;
    private ActivityModel currentActivity;

    protected MapPresenter mapPresenter;
    protected AdminFieldSetPresenter adminPresenter;
    private View view;

    public SiteFormPresenter(EventBus eventBus, CommandService service, ActivityModel activity, View view) {
        super();
        this.eventBus = eventBus;
        this.service = service;
        this.view = view;

        init(activity);
    }


    protected ListStore<PartnerModel> createPartnerStore() {
        ListStore<PartnerModel> store = new ListStore<PartnerModel>();

        if(currentActivity.getDatabase().isEditAllAllowed()) {

            for(PartnerModel partner : currentActivity.getDatabase().getPartners()) {
                if(partner.isOperational()) {
                    store.add(partner);
                }
            }
            store.sort("name", Style.SortDir.ASC);

        } else {

            store.add(currentActivity.getDatabase().getMyPartner());

        }
        return store;
    }

    protected ListStore<SiteModel> createAsssessmentStore() {
        return new ListStore<SiteModel>();
    }

    public void init(ActivityModel activity) {

        this.currentActivity = activity;

        mapPresenter = new MapPresenter(view.createMapView());

        adminPresenter = new AdminFieldSetPresenter(service, currentActivity, view.createAdminFieldSetView(currentActivity));
        adminPresenter.setListener(new AdminFieldSetPresenter.Listener() {
            @Override
            public void onBoundsChanged(String name, Bounds bounds) {
                mapPresenter.setBounds(name, bounds);
            }

            @Override
            public void onModified() {
                SiteFormPresenter.this.onModified();
            }
        });

        view.init(this, currentActivity, createPartnerStore(), createAsssessmentStore() );

    }

    public void setSite(SiteModel site) {
        currentSite = site;

        adminPresenter.setSite(site);
        mapPresenter.setSite(site, adminPresenter.getBoundsName(), adminPresenter.getBounds());
        view.setSite(site);

        view.setActionEnabled(UIActions.save, false);
    }

    public void newSite() {
        setSite(new SiteModel());
    }

    public void onUIAction(String actionId) {

        if(UIActions.save.equals(actionId)) {
            onSave();
        } else if(UIActions.cancel.equals(actionId)) {

        }
    }

    public void onModified() {
        view.setActionEnabled(UIActions.save, view.isDirty() || adminPresenter.isDirty());
    }


    public void onSave() {

        if(currentSite.hasId()) {

            final Map<String, Object> changes = view.getChanges();
            if(adminPresenter.isDirty()) {
                changes.putAll(adminPresenter.getPropertyMap());
            }

            service.execute(new UpdateEntity("Site", currentSite.getId(), changes), view.getMonitor(), new AsyncCallback<VoidResult>() {
                @Override
                public void onFailure(Throwable throwable) {
                    // let monitor display
                }

                @Override
                public void onSuccess(VoidResult voidResult) {
                    // update model
                    for(Map.Entry<String,Object> change : changes.entrySet()) {
                        currentSite.set(change.getKey(), change.getValue());
                    }

                    eventBus.fireEvent(new SiteEvent(AppEvents.SiteChanged, SiteFormPresenter.this, currentSite));
                    view.hide();
                }
            });
        } else {

            final Map<String,Object> properties = view.getPropertyMap();
            properties.putAll(adminPresenter.getPropertyMap());
            properties.put("activityId", currentActivity.getId());

            service.execute(new CreateEntity("Site", properties), view.getMonitor(), new AsyncCallback<CreateResult>() {
                @Override
                public void onFailure(Throwable throwable) {
                    // let monitor display
                }

                @Override
                public void onSuccess(CreateResult result) {
                    currentSite.setProperties(properties);
                    currentSite.setId(result.getNewId());

                    eventBus.fireEvent(new SiteEvent(AppEvents.SiteCreated, SiteFormPresenter.this, currentSite));
                    view.hide();
                }
            });
        }
    }

    public int getActivityId() {
        return currentActivity.getId();
    }

    public void destroy() {

    }
}
