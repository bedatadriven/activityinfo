/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.charts;

import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.callback.DownloadCallback;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.SubscribeForm;
import org.sigmah.client.page.common.dialog.FormDialogCallback;
import org.sigmah.client.page.common.dialog.FormDialogImpl;
import org.sigmah.client.page.common.filter.AdminFilterPanel;
import org.sigmah.client.page.common.filter.DateRangePanel;
import org.sigmah.client.page.common.filter.IndicatorTreePanel;
import org.sigmah.client.page.common.filter.PartnerFilterPanel;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.common.toolbar.ExportCallback;
import org.sigmah.client.page.common.toolbar.ExportMenuButton;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.table.PivotGridPanel;
import org.sigmah.client.page.table.PivotGridPanel.PivotTableRow;
import org.sigmah.client.report.DimensionStoreFactory;
import org.sigmah.shared.command.CreateReportDef;
import org.sigmah.shared.command.CreateSubscribe;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.command.GenerateElement;
import org.sigmah.shared.command.RenderElement;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.report.content.PivotChartContent;
import org.sigmah.shared.report.model.DateDimension;
import org.sigmah.shared.report.model.DateUnit;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.Report;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.DelayedTask;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 *
 * Page that allows the user to build and view a PivotChartElement
 *
 * @author Alex Bertram
 */
public class ChartPage extends LayoutContainer implements Page, ActionListener{

	public static final PageId PAGE_ID = new PageId("charts");
	
    private final EventBus eventBus;
    private final Dispatcher service;
    private ActionToolBar toolBar;

    private ComboBox<Dimension> categoryCombo;
    private ChartTypeGroup typeGroup;

    private ChartOFCView preview;
    private ContentPanel center;
    private PivotGridPanel gridPanel;

    private IndicatorTreePanel indicatorPanel;
    private AdminFilterPanel adminFilterPanel;
    private DateRangePanel dateFilterPanel;
    private PartnerFilterPanel partnerFilterPanel;

	private ComboBox<Dimension> legendCombo;
	
	private SubscribeForm form;
	private FormDialogImpl dialog;

    @Inject
    public ChartPage(EventBus eventBus, Dispatcher service) {
        this.eventBus = eventBus;
        this.service = service;

        setLayout(new BorderLayout());

        createWest();
        createCenter();
        createToolBar();
        createChartPane();
        createDimBar();
        createGridPane();
    }
    
    @Override
	public void onUIAction(String actionId) {

        if (UIActions.REFRESH.equals(actionId)) {
            load();
        }
        else if(UIActions.SUBSCRIBE.equals(actionId)){
        	createSubscribeDialog();
        }
    }

    private void createToolBar() {
        toolBar = new ActionToolBar(this);

        toolBar.addRefreshButton();
        toolBar.add(new SeparatorToolItem());

        typeGroup = new ChartTypeGroup();
        typeGroup.addListener(Events.Select, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				load();
			}
        });

        toolBar.add(new LabelToolItem(I18N.CONSTANTS.chartType()));
        toolBar.add(typeGroup.getButtons());

        toolBar.add(new SeparatorToolItem());

        toolBar.add(new ExportMenuButton(RenderElement.Format.PowerPoint)
        	.withPowerPoint()
        	.withWord()
        	.withPdf()
        	.withPng()
        	.callbackTo(new ExportCallback() {
            @Override
			public void export(RenderElement.Format format) {
                ChartPage.this.export(format);
            }
        }));
        
        toolBar.addButton(UIActions.SUBSCRIBE, I18N.CONSTANTS.subscribed(), IconImageBundle.ICONS.email());

        center.setTopComponent(toolBar);

    }

    private void createWest() {

        ContentPanel westPanel = new ContentPanel(new AccordionLayout());
        westPanel.setHeading(I18N.CONSTANTS.filter());

        indicatorPanel = new IndicatorTreePanel(service, true);
        indicatorPanel.setHeaderVisible(true);
        westPanel.add(indicatorPanel);

        adminFilterPanel = new AdminFilterPanel(service);
        westPanel.add(adminFilterPanel);

        dateFilterPanel = new DateRangePanel();
        westPanel.add(dateFilterPanel);

        partnerFilterPanel = new PartnerFilterPanel(service);
        partnerFilterPanel.applyBaseFilter(new Filter());
        westPanel.add(partnerFilterPanel);
        
        BorderLayoutData west = new BorderLayoutData(Style.LayoutRegion.WEST, 0.30f);
        west.setCollapsible(true);
        west.setSplit(true);
        west.setMargins(new Margins(0, 5, 0, 0));

        add(westPanel, west);
    }

    private void createCenter() {

        center = new ContentPanel(new BorderLayout());
        center.setHeaderVisible(false);

        add(center, new BorderLayoutData(Style.LayoutRegion.CENTER));
    }

    private void createChartPane() {

        preview = new ChartOFCView();
        center.add(preview, new BorderLayoutData(Style.LayoutRegion.CENTER));
    }


    private void createGridPane() {
        BorderLayoutData south = new BorderLayoutData(Style.LayoutRegion.SOUTH, 0.30f);
        south.setCollapsible(true);
        south.setSplit(true);
        south.setMargins(new Margins(5, 0, 0, 0));

        gridPanel = new PivotGridPanel(eventBus);
        gridPanel.setHeading("Table");

        center.add(gridPanel, south);
    }

    private void createDimBar() {
        ToolBar dimBar = new ToolBar();
        ListStore<Dimension> store = DimensionStoreFactory.create(service);

        dimBar.add(new LabelToolItem(I18N.CONSTANTS.categories()));
        categoryCombo = new ComboBox<Dimension>();
        categoryCombo.setForceSelection(true);
        categoryCombo.setEditable(false);
        categoryCombo.setStore(store);
        categoryCombo.setDisplayField("caption");
        categoryCombo.setValue(new DateDimension(DateUnit.YEAR));
        dimBar.add(categoryCombo);

        dimBar.add(new FillToolItem());

        dimBar.add(new LabelToolItem(I18N.CONSTANTS.legend()));
        legendCombo = new ComboBox<Dimension>();
        legendCombo.setForceSelection(true);
        legendCombo.setEditable(false);
        legendCombo.setStore(store);
        legendCombo.setDisplayField("caption");
        dimBar.add(legendCombo);

        preview.setBottomComponent(dimBar);
    }


    public PivotChartReportElement getChartElement() {
        PivotChartReportElement element = new PivotChartReportElement();
        element.setType(typeGroup.getSelection());

        if (categoryCombo.getValue() instanceof DateDimension) {
            DateDimension dim = (DateDimension) categoryCombo.getValue();
            if (dim.getUnit() != DateUnit.YEAR) {
                element.addCategoryDimension(new DateDimension(DateUnit.YEAR));
            }
        }
        
        if(categoryCombo.getValue() != null){
        	element.addCategoryDimension(categoryCombo.getValue());
        }
        
        for (Integer indicatorId : indicatorPanel.getSelectedIds()) {
            element.addIndicator(indicatorId);
        }
        
        
        if(legendCombo.getValue() != null) {
        	element.addSeriesDimension(legendCombo.getValue());
        }

        List<PartnerDTO> partners = partnerFilterPanel.getSelection();
        for (PartnerDTO entity : partners) {
        	element.getFilter().addRestriction(DimensionType.Partner, entity.getId());
        }
        
        
        return element;
    }

    private void load() {
    	final PivotChartReportElement element = getChartElement();
		service.execute(new GenerateElement<PivotChartContent>(element), null, new AsyncCallback<PivotChartContent>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(PivotChartContent result) {
				element.setContent(result);
				setData(element);
			}
		});
    }
    
	private void export(RenderElement.Format format) {
		service.execute(new RenderElement(getChartElement(), format), null, new DownloadCallback(eventBus));
    }

    public void setData(PivotChartReportElement element) {
        preview.setContent(element);
        gridPanel.setData(element);
    }
    
    public void createSubscribeDialog(){
    	
    	form = new SubscribeForm();
    	
		dialog = new FormDialogImpl(form);
		dialog.setWidth(450);
		dialog.setHeight(400);
		dialog.setHeading(I18N.CONSTANTS.SubscribeTitle());
		
		dialog.show(new FormDialogCallback() {
			@Override
			public void onValidated() {
				if(form.validListField()){
					createReport();	
				} else{
					MessageBox.alert(I18N.CONSTANTS.error(), I18N.MESSAGES.noEmailAddress(), null);
				}
            }
		});
    }
    
 public void createReport(){
    	
    	final Report report = new Report();
    	report.addElement(getChartElement());
    	report.setDay(form.getDay());
    	report.setTitle(form.getTitle());
    	report.setFrequency(form.getReportFrequency());
		
		service.execute(new CreateReportDef(report), null, new AsyncCallback<CreateResult>() {
            public void onFailure(Throwable caught) {
            	dialog.onServerError();
            }

            public void onSuccess(CreateResult result) {
            	subscribeEmail(result.getNewId());
            }
        });
    	
    }

    public void subscribeEmail(int templateId){
    	
    	CreateSubscribe sub = new CreateSubscribe();
		sub.setReportTemplateId(templateId);
		sub.setEmailsList(form.getEmailList());
        
		service.execute(sub, null, new AsyncCallback<VoidResult>() {
            public void onFailure(Throwable caught) {
            	dialog.onServerError();
            }

            public void onSuccess(VoidResult result) {
            	dialog.hide();
            }
        });
    }

    public void bindReportElement(final PivotChartReportElement element){
    	
   	 new DelayedTask(new Listener<BaseEvent>() {
            @Override
			public void handleEvent(BaseEvent be) {
            	typeGroup.setSelection(element.getType());
            	
            	if(element.getCategoryDimensions() != null && element.getCategoryDimensions().size() >0){
            		categoryCombo.setValue((Dimension)element.getCategoryDimensions().get(0));
            	}
            	
            	List<Integer> indicators = element.getIndicators();
            	for(Integer id :indicators){
            		indicatorPanel.setSelection(id, true);
            	}
        
            	if(element.getSeriesDimension()!=null && element.getSeriesDimension().size()>0){
            		legendCombo.setValue((Dimension)element.getSeriesDimension().get(0));	
            	}
            	
            	load();
            }
        }).delay(10000);
   	
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
		return this;
	}

	@Override
	public void requestToNavigateAway(PageState place,
			NavigationCallback callback) {
		callback.onDecided(true);
		
	}

	@Override
	public String beforeWindowCloses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean navigate(PageState place) {
		return true;
	}
}
