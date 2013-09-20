package org.activityinfo.client.importer;

import java.util.List;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.widget.wizard.WizardPage;
import org.activityinfo.shared.command.BatchCommand;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.MatchLocation;
import org.activityinfo.shared.command.result.BatchResult;

import com.extjs.gxt.ui.client.widget.Label;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LocationMatchPage extends WizardPage {
    
    private ImportModel model;
    private LocationColumnPage columnPage;
    
    private Label statusLabel;
    private Dispatcher dispatcher;
    
    public LocationMatchPage(Dispatcher dispatcher, ImportModel model, LocationColumnPage columnPage) {
        super();
        this.dispatcher = dispatcher;
        this.model = model;
        this.columnPage = columnPage;
        
        statusLabel = new Label();
        add(statusLabel);
    }

    @Override
    public void beforeShow() {
        
        final List<Command> locationsToMatch = Lists.newArrayList();
        final List<ColumnBinder<MatchLocation>> binders = columnPage.getLocationBinders();
        
        model.getData().getRowStore().forEachRow(new ImportRowProcessor() {
            
            @Override
            public void process(int rowIndex, String[] columns) {
                MatchLocation match = new MatchLocation();
                match.setLocationType(model.getActivity().getLocationTypeId());
        
                ColumnBinders.bind(binders, columns, match);
                
                locationsToMatch.add(match);
            }
        });
        
        statusLabel.setText("Matching locations...");
        dispatcher.execute(new BatchCommand(locationsToMatch), new AsyncCallback<BatchResult>() {

            @Override
            public void onFailure(Throwable caught) {
                statusLabel.setText("Failed");
            }

            @Override
            public void onSuccess(BatchResult result) {
                statusLabel.setText("Succeeded");
                model.setLocations((List)result.getResults());
                
            }
        });
    }

    

}
