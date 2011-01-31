package org.sigmah.client.page.admin.users;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import org.sigmah.client.UserInfo;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.admin.users.AdminUsersPresenter.AdminUsersActionListener;
import org.sigmah.client.page.admin.users.AdminUsersPresenter.AdminUsersStore;
import org.sigmah.client.page.admin.users.AdminUsersPresenter.View;
import org.sigmah.client.page.common.grid.ConfirmCallback;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.config.form.UserSigmahForm;
import org.sigmah.client.ui.StylableVBoxLayout;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.OrgUnitDTO;
import org.sigmah.shared.dto.OrganizationDTO;
import org.sigmah.shared.dto.UserDTO;
import org.sigmah.shared.dto.profile.ProfileDTO;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreFilter;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Displays users administration screen.
 * 
 * @author nrebiai
 * 
 */
public class AdminUsersView extends View {
	
	private final static String DEFAULT_FILTER = "default";
	private final static String STYLE_MAIN_BACKGROUND = "main-background";
	
	private final Grid<UserDTO> grid;
	private final Dispatcher dispatcher;
	private final UserInfo info;
	private final ContentPanel usersListPanel;
	private final AdminUsersPresenter.AdminUsersStore adminUsersStore;
	private String filterUser = DEFAULT_FILTER;
	
	public AdminUsersView(Dispatcher dispatcher, UserInfo info){
		
		this.dispatcher = dispatcher;
		this.info = info;
		
		usersListPanel = new ContentPanel(new FitLayout());
		usersListPanel.setHeaderVisible(true);
		usersListPanel.setHeading(I18N.CONSTANTS.adminUsersPanel());
		
        usersListPanel.setBorders(false);
        usersListPanel.setBodyBorder(false);
        final BorderLayoutData mainLayoutData = new BorderLayoutData(LayoutRegion.NORTH);
        mainLayoutData.setMargins(new Margins(0, 0, 0, 0));
		adminUsersStore = new AdminUsersPresenter.AdminUsersStore();
		grid = buildUsersGrid();
		
		final VBoxLayoutData topVBoxLayoutData = new VBoxLayoutData();
        topVBoxLayoutData.setFlex(1.0);
		usersListPanel.setTopComponent(initToolBar());
		usersListPanel.add(grid, topVBoxLayoutData);
		
		add(usersListPanel);
	}
	
	public ContentPanel getMainPanel() {
		return usersListPanel;
	}
	
	public ContentPanel getUsersPanel() {
		return usersListPanel;
	}
	
	private Grid<UserDTO> buildUsersGrid(){

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();  
		   
		ColumnConfig column = new ColumnConfig("id",I18N.CONSTANTS.adminUsersId(),30);  
		configs.add(column);
		
		column = new ColumnConfig("name",I18N.CONSTANTS.adminUsersName(),100);  
		configs.add(column);  
	   
		column = new ColumnConfig("firstName",I18N.CONSTANTS.adminUsersFirstName(),100);    
		configs.add(column);  
	   
		column = new ColumnConfig("email",I18N.CONSTANTS.adminUsersEmail(),150);   
		configs.add(column);  
		
		column = new ColumnConfig("locale",I18N.CONSTANTS.adminUsersLocale(),50);      
		configs.add(column); 
		
		column = new ColumnConfig("orgUnit",I18N.CONSTANTS.adminUsersOrgUnit(),75);
		column.setRenderer(new GridCellRenderer<UserDTO>() {
            @Override
            public Object render(UserDTO model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<UserDTO> store, Grid<UserDTO> grid) {
                final OrgUnitDTO orgUnit = (OrgUnitDTO) model.get(property);
                return createUserGridText(orgUnit != null ? 
                		(orgUnit.getFullName() != null ? orgUnit.getFullName() : "" ) : "");
            }
        });
		configs.add(column); 
		
		column = new ColumnConfig("pwdChangeKey",I18N.CONSTANTS.adminUsersPasswordChange(),120);   
		configs.add(column);
		
		column = new ColumnConfig("pwdChangeDate",I18N.CONSTANTS.adminUsersDatePasswordChange(),120);  
		final DateTimeFormat format = DateTimeFormat.getFormat(I18N.CONSTANTS.flexibleElementDateFormat());  
		column.setHeader(I18N.CONSTANTS.adminUsersDatePasswordChange());
		column.setDateTimeFormat(format);
        column.setRenderer(new GridCellRenderer<UserDTO>() {
            @Override
            public Object render(UserDTO model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<UserDTO> store, Grid<UserDTO> grid) {
                final Date d = (Date) model.get(property);
                return createUserGridText(d != null ? format.format(d) : "");
            }
        });  
		configs.add(column); 
		
		
		column = new ColumnConfig();  
		column.setId("profiles");  
		column.setHeader(I18N.CONSTANTS.adminUsersProfiles());   
		column.setWidth(300);  
	    column.setRenderer(new GridCellRenderer<UserDTO>(){

			@Override
			public Object render(UserDTO model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<UserDTO> store, Grid<UserDTO> grid) {
				
				String content = "";
				
				if(model.getProfilesDTO()!= null){																							
					for(ProfileDTO oneProfileDTO : model.getProfilesDTO()){
						content = oneProfileDTO.getName() + ", " + content;
					}
				}else{
					content = I18N.CONSTANTS.adminUsersNoProfiles();
				}				
				return createUserGridText(content);
				
			}
	    	
	    });  
		configs.add(column); 
		
		column = new ColumnConfig();    
		column.setWidth(75);  
		column.setAlignment(Style.HorizontalAlignment.RIGHT);
	    column.setRenderer(new GridCellRenderer<UserDTO>(){

			@Override
			public Object render(final UserDTO model, final String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<UserDTO> store, Grid<UserDTO> grid) {
				
				Button button = new Button(I18N.CONSTANTS.edit());
		        button.setItemId(UIActions.edit);
		        button.addListener(Events.OnClick, new Listener<ButtonEvent>(){

					@Override
					public void handleEvent(ButtonEvent be) {
						final Window window = new Window();
						UserSigmahForm form = AdminUsersView.this.showNewForm(window, new AsyncCallback<CreateResult>(){

							@Override
							public void onFailure(Throwable caught) {
								window.hide();								
							}

							@Override
							public void onSuccess(CreateResult result) {
								window.hide();
								//refresh view
								AdminUsersPresenter.refreshUserPanel(dispatcher, AdminUsersView.this);
							}			
						}, model);
						
						window.add(form);
				        window.show();
						
					}
		        	
		        });
		        
		        
				return button;
				
			}
	    	
	    });  
		configs.add(column); 
		
		ColumnModel cm = new ColumnModel(configs);
		
		final Grid<UserDTO> grid = new Grid<UserDTO>(adminUsersStore, cm); 
	    adminUsersStore.addListener(Events.Add, new Listener<StoreEvent<UserDTO>>() {

	            @Override
	            public void handleEvent(StoreEvent<UserDTO> be) {
	            }
	        });

	     adminUsersStore.addListener(Events.Clear, new Listener<StoreEvent<UserDTO>>() {

	            @Override
	            public void handleEvent(StoreEvent<UserDTO> be) {
	            }
	        });
	     adminUsersStore.addListener(Store.Filter, new Listener<StoreEvent<UserDTO>>() {

	            @Override
	            public void handleEvent(StoreEvent<UserDTO> be) {
	            }
	        });
	     
	     grid.getView().setForceFit(true);  
	     grid.setStyleAttribute("borderTop", "none");    
	     grid.setBorders(false);  
	     grid.setStripeRows(true);
	     grid.setAutoHeight(true);
	     return grid;
	}
	
	private void addFilterByUser(){		
		StoreFilter<UserDTO> filter = new StoreFilter<UserDTO>(){
			@Override
			public boolean select(Store<UserDTO> store, UserDTO parent,UserDTO item,
	                    String property) {
				boolean selected = false;
				selected = item.getName().toUpperCase().startsWith(filterUser.toUpperCase());					
				return selected;
			}
		};						
		adminUsersStore.addFilter(filter);					
	}
	
	private void applyFilterByUser(){
		if(filterUser != null 
				&& !filterUser.isEmpty() 
				&& !filterUser.trim().equals("") 
				&& !DEFAULT_FILTER.equals(filterUser)){
			adminUsersStore.applyFilters(null);
		}else{
			adminUsersStore.clearFilters();
		}		
	}

	@Override
	public AdminUsersStore getAdminUsersStore() {
		return adminUsersStore;
	}
	
    private ActionToolBar initToolBar() {
    	
		ActionToolBar toolBar = new ActionToolBar();
		toolBar.addButton(UIActions.add, I18N.CONSTANTS.addUser(), IconImageBundle.ICONS.addUser());
        toolBar.addButton(UIActions.delete, I18N.CONSTANTS.adminUserDisable(), IconImageBundle.ICONS.deleteUser());
        toolBar.add(new LabelToolItem(I18N.CONSTANTS.adminUsersSearchByName()));	   
        //Filtering by User name
        adminUsersStore.clearFilters();
        final TextField<String> filterUserName = new TextField<String>();        
        filterUserName.addKeyListener(new KeyListener(){
        	public void componentKeyUp(ComponentEvent event) {
        		filterUser = filterUserName.getValue();
        		applyFilterByUser();
        	}
        });
        addFilterByUser();
        toolBar.add(filterUserName);
        toolBar.add(new LabelToolItem(I18N.CONSTANTS.adminUsersSelectionMode()));  
        final SimpleComboBox<String> type = new SimpleComboBox<String>();  
        type.setTriggerAction(TriggerAction.ALL);  
        type.setEditable(false);  
        type.setFireChangeEventOnSetValue(true);  
	    type.setWidth(100);  
	    type.add("Row");  
	    type.add("Cell");  
	    type.setSimpleValue("Row");  
	    type.addListener(Events.Change, new Listener<FieldEvent>() {  
	    	public void handleEvent(FieldEvent be) {  
	    		boolean cell = type.getSimpleValue().equals("Cell");  
	    		grid.getSelectionModel().deselectAll();  
	    		if (cell) {  
	    			grid.setSelectionModel(new CellSelectionModel<UserDTO>());  
	    		} else {  
	    			grid.setSelectionModel(new GridSelectionModel<UserDTO>());  
	    		}  
	    	}  
	    });  
	    toolBar.add(type);
	    toolBar.setListener(new AdminUsersActionListener(this, dispatcher));
	    return toolBar;
    }

	@Override
	public Object getSelection() {
		GridSelectionModel<UserDTO> sm = grid.getSelectionModel();
        if (sm instanceof CellSelectionModel) {
            CellSelectionModel<UserDTO>.CellSelection cell = ((CellSelectionModel<UserDTO>) sm).getSelectCell();
            return cell == null ? null : cell.model;
        } else {
            return sm.getSelectedItem();
        }
	}

	@Override
	public void confirmDeleteSelected(ConfirmCallback confirmCallback) {
		confirmCallback.confirmed();
	}

	@Override
	public MaskingAsyncMonitor getDeletingMonitor() {
		return new MaskingAsyncMonitor(this.getMainPanel(), I18N.CONSTANTS.deleting());
	}
	
	@Override
	public MaskingAsyncMonitor getUsersLoadingMonitor() {
		return new MaskingAsyncMonitor(usersListPanel, I18N.CONSTANTS.loading());
	}

	@Override
	public UserSigmahForm showNewForm(Window window, AsyncCallback<CreateResult> callback, UserDTO userToUpdate) {
		
		
        window.setHeading(I18N.CONSTANTS.newUser());
        window.setSize(550, 350);
        window.setPlain(true);
        window.setModal(true);
        window.setBlinkModal(true);
        window.setLayout(new FitLayout());
        
        final UserSigmahForm form = new UserSigmahForm(dispatcher, info, callback, userToUpdate);

		return form;

	}
	
	private Object createUserGridText(String content) {
        final Text label = new Text(content);
        label.addStyleName("project-grid-leaf");
        
        return label;
    }

}
