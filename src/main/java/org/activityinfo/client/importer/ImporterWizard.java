package org.activityinfo.client.importer;

import java.util.List;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.local.command.handler.KeyGenerator;
import org.activityinfo.client.widget.wizard.Wizard;
import org.activityinfo.client.widget.wizard.WizardPage;
import org.activityinfo.shared.command.BatchCommand;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.CreateLocation;
import org.activityinfo.shared.command.CreateSite;
import org.activityinfo.shared.command.result.BatchResult;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.dto.SiteDTO;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ImporterWizard extends Wizard {
    private ImportModel model;
    private PasteDataPage pastePage;
    private PreviewGridPage previewPage;
    private LocationColumnPage locationPage;
    private LocationMatchPage locationMatchPage;
    private DataColumnPage datePage;
    private IndicatorColumnPage indicatorPage;
    private ActivityDTO activity;
    private Dispatcher dispatcher;
    
    public ImporterWizard(Dispatcher dispatcher, ActivityDTO activity) {
        this.dispatcher = dispatcher;
        this.activity = activity;
        
        model = new ImportModel();
        model.setActivity(activity);
        pastePage = new PasteDataPage(model);
        previewPage = new PreviewGridPage(model);
        locationPage = new LocationColumnPage(activity, model);
        locationMatchPage = new LocationMatchPage(dispatcher, model, locationPage);
        datePage = new DataColumnPage(model);
        indicatorPage = new IndicatorColumnPage(activity, model);
        
    }
    
    @Override
    public WizardPage[] getPages() {
        return new WizardPage[] { pastePage, previewPage, 
            locationPage, locationMatchPage, 
            datePage, indicatorPage };
    }

    @Override
    public String getTitle() {
        return "Import sites";
    }


    @Override
    public void finish(final AsyncCallback<Void> callback) {
        
        final KeyGenerator keyGenerator = new KeyGenerator();
        
        final List<Command> commands = Lists.newArrayList();
        
   
        // now create the corresponding sites
        final List<ColumnBinder<SiteDTO>> siteBinders = Lists.newArrayList();
        siteBinders.addAll(datePage.getSiteBinders());
        siteBinders.addAll(indicatorPage.getBinders());
        
        model.getData().getRowStore().forEachRow(new ImportRowProcessor() {
            
            @Override
            public void process(int rowIndex, String[] columns) {
                LocationDTO location = model.getLocations().get(rowIndex);
                location.setLocationTypeId(activity.getLocationTypeId());

                SiteDTO site = new SiteDTO();
                site.setId(keyGenerator.generateInt());
                site.setReportingPeriodId(keyGenerator.generateInt());
                site.setLocationId(location.getId());
                site.setActivityId(activity.getId());
                site.setPartner(activity.getDatabase().getPartners().get(0));
                
                ColumnBinders.bind(siteBinders, columns, site);
                
                commands.add(new CreateLocation(location));
                commands.add(new CreateSite(site));
            }
        });
        
        // let'er rip
        dispatcher.execute(new BatchCommand(commands), new AsyncCallback<BatchResult>() {

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(BatchResult result) {
                callback.onSuccess(null);
            }
        });
    }

}
