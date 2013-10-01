package org.activityinfo.client.importer;

import java.util.List;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.importer.column.ColumnBinding;
import org.activityinfo.client.local.command.handler.KeyGenerator;
import org.activityinfo.client.widget.wizard.Wizard;
import org.activityinfo.client.widget.wizard.WizardPage;
import org.activityinfo.shared.command.BatchCommand;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.CreateLocation;
import org.activityinfo.shared.command.CreateSite;
import org.activityinfo.shared.command.MatchLocation;
import org.activityinfo.shared.command.result.BatchResult;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.bouncycastle.jce.provider.symmetric.AES.KeyGen;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ImporterWizard extends Wizard {
    private ImportModel model;
    private PasteDataPage pastePage;
    private PreviewGridPage previewPage;
    private ActivityDTO activity;
    private Dispatcher dispatcher;
    
    public ImporterWizard(Dispatcher dispatcher, ActivityDTO activity) {
        this.dispatcher = dispatcher;
        this.activity = activity;
        
        model = new ImportModel();
        model.setActivity(activity);
        pastePage = new PasteDataPage(model);
        previewPage = new PreviewGridPage(model);
        
    }
    
    @Override
    public WizardPage[] getPages() {
        return new WizardPage[] { pastePage, previewPage };
    }

    @Override
    public String getTitle() {
        return "Import sites";
    }


    @Override
    public void finish(final AsyncCallback<Void> callback) {
        
        final KeyGenerator keyGenerator = new KeyGenerator();
   
        int numColums = model.getData().getNumColumns();
        ColumnBinding[] bindings = bindingsArray();
        
        
        // do a first pass to match the location
        List<Command> matchBatch = Lists.newArrayList();
        for(ImportRowModel row : model.getData().getRowStore().getModels()) {
            MatchLocation location = new MatchLocation();
            location.setLocationType(model.getActivity().getLocationTypeId());
            
            for(int i=0;i!=numColums;++i) {
                bindings[i].bindLocation(row.get(i), location);
            }
            matchBatch.add(location);
        }
        
        dispatcher.execute(new BatchCommand(matchBatch), new AsyncCallback<BatchResult>() {

            @Override
            public void onFailure(Throwable caught) {
                MessageBox.alert("Match locations failed", "Exception", null);
                
            }

            @Override
            public void onSuccess(BatchResult result) {
                submitSites((List)result.getResults(), callback);
            }

            
        });
        
    }

    protected ColumnBinding[] bindingsArray() {
        int numColumns = model.getData().getNumColumns();
        ColumnBinding[] bindings = new ColumnBinding[numColumns];
        for(int i=0;i!=numColumns;++i) {
            bindings[i] = model.getData().getColumns().get(i).getBinding();
        }
        return bindings;
    }

    private void submitSites(List<LocationDTO> results, final AsyncCallback<Void> callback) {
        
        int numColums = model.getData().getNumColumns();
        ColumnBinding[] bindings = bindingsArray();
        
        
        KeyGenerator keyGenerator = new KeyGenerator();
        
        // do a first pass to match the location
        List<Command> siteBatch = Lists.newArrayList();
        int rowIndex = 0;
        for(ImportRowModel row : model.getData().getRowStore().getModels()) {
            
            LocationDTO location = results.get(rowIndex);
            if(location.isNew()) {
                siteBatch.add(new CreateLocation(location));
            }
            SiteDTO site = new SiteDTO();
            site.setId(keyGenerator.generateInt());
            site.setReportingPeriodId(keyGenerator.generateInt());
            site.setActivityId(model.getActivity().getId());
            site.setLocationId(location.getId());
            site.setPartner(model.getActivity().getDatabase().getPartners().get(0));
            
            for(int i=0;i!=numColums;++i) {
                bindings[i].bindSite(row.get(i), site);
            }
            siteBatch.add(new CreateSite(site));
            rowIndex++;
        } 
        
        dispatcher.execute(new BatchCommand(siteBatch), new AsyncCallback<BatchResult>() {

            @Override
            public void onFailure(Throwable caught) {
              
               MessageBox.alert("Import failed", "Exception: " + caught.getMessage(), null);
               callback.onFailure(null);
            }

            @Override
            public void onSuccess(BatchResult result) {
                MessageBox.alert("Import succeeded!", "Refresh the data grid to see your new sites", null);
                callback.onSuccess(null);
            }
        });
        
    }
 }
