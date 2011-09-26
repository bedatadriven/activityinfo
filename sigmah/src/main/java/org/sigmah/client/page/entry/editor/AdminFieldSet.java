/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.client.page.common.widget.RemoteComboBox;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

public class AdminFieldSet extends LayoutContainer implements AdminFieldSetPresenter.View  {

    private AdminFieldSetPresenter presenter;
    private Map<Integer, ComboBox<AdminEntityDTO>> comboBoxes =
            new HashMap<Integer, ComboBox<AdminEntityDTO>>();
	private TextField<String> nameField;
	private Timer nameDelay;
	private static final int delay=250; // milliseconds 

    public AdminFieldSet(ActivityDTO activity) {
        //super(I18N.CONSTANTS.location(), 100, 200);
    	super();
        setLayout(new RowLayout(Orientation.HORIZONTAL));
        //setBorders(false);
        
        createTimers();
        
        if (activity.getLocationType().getBoundAdminLevelId() == null) {
            nameField = new TextField<String>();
            nameField.setName("locationName");
            nameField.setFieldLabel(activity.getLocationType().getName());
            nameField.setAllowBlank(false);
            nameField.addKeyListener(new KeyListener() {
				@Override
				public void componentKeyUp(ComponentEvent event) {
					super.componentKeyUp(event);
					nameDelay.schedule(delay);
				}
            });
            add(nameField);
            nameField.setStyleAttribute("border-width", "3px");
            nameField.setHeight("30px");
        }
        
        for(final AdminLevelDTO level : activity.getAdminLevels()) {
            final int levelId = level.getId();

            final ComboBox<AdminEntityDTO> comboBox = new RemoteComboBox<AdminEntityDTO>();
            comboBox.setFieldLabel(level.getName());
            comboBox.setStore(new ListStore<AdminEntityDTO>());
            comboBox.setTypeAhead(false);
            comboBox.setForceSelection(true);
            comboBox.setEditable(false);
            comboBox.setValueField("id");
            comboBox.setDisplayField("name");
            comboBox.setEnabled(false);
            comboBox.setTriggerAction(ComboBox.TriggerAction.ALL);

            comboBox.addListener(Events.Select, new Listener<FieldEvent>() {
                @Override
                public void handleEvent(FieldEvent be) {
                    AdminEntityDTO selected = (AdminEntityDTO) be.getField().getValue();

                    presenter.onSelectionChanged(levelId, selected);

                }
            });
            comboBox.addListener(Events.BrowserEvent, new Listener<FieldEvent>() {
                public void handleEvent(FieldEvent be) {
                    if(be.getEventTypeInt() == Event.ONKEYUP) {
                        if(be.getEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
                            comboBox.setValue(null);
                            presenter.onSelectionChanged(levelId, null);
                        }
                    }
                }
            });

            comboBoxes.put(levelId, comboBox);
            add(comboBox);
        }
    }
    
    

    @Override
	public boolean add(Widget widget) {
    	LayoutContainer container = new LayoutContainer();
        FormLayout formLayout = new FormLayout();
        formLayout.setLabelAlign(LabelAlign.TOP);
        container.setLayout(formLayout);
		container.add(widget, new RowData(102, -1, new Margins(4,2,4,2)));
		return super.add(container);
	}

	private void createTimers() {
    	nameDelay = new Timer() {
			@Override
			public void run() {
				presenter.onNameChanged(nameField.getValue());
			}
		};
	}
    
	private AdminEntityDTO findEntity(String name, List<AdminEntityDTO> models) {
        for(AdminEntityDTO entity : models) {
            if(entity.getName().equalsIgnoreCase(name)) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public void bindPresenter(AdminFieldSetPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setStore(int levelId, ListStore<AdminEntityDTO> store) {
        comboBoxes.get(levelId).setStore(store);
    }

    @Override
    public void setEnabled(int levelId, boolean enabled) {
        comboBoxes.get(levelId).setEnabled(enabled);
    }

    @Override
    public void setValue(int levelId, AdminEntityDTO value) {
        comboBoxes.get(levelId).setValue(value);
    }

    public ComboBox<AdminEntityDTO> getCombo(int levelId) {
        return comboBoxes.get(levelId);
    }

	public String getLocationName() {
		return nameField.getValue();
	}
}
