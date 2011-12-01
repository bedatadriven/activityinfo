/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.admin;

import java.util.Iterator;
import java.util.Map;

import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.common.collect.Maps;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;

/**
 * Collection of {@link ComboBox} built with reference to an 
 * {@link AdminFieldSetPresenter}. Note that this is not a widget itself,
 * these AdminComboBoxes must still be added to a container. 
 */
public class AdminComboBoxSet implements Iterable<AdminComboBox>  {
    
    public interface Style {
    	void initializeComboBox(AdminComboBox comboBox, AdminLevelDTO level);
    	void onComboStateUpdated(AdminComboBox comboBox, boolean enabled);
    }

    private final Map<Integer, AdminComboBox> comboBoxes = Maps.newHashMap();
    private final Style style;
    
    public AdminComboBoxSet(final AdminFieldSetPresenter presenter, Style style) {
    	this.style = style;
       
        for(final AdminLevelDTO level : presenter.getAdminLevels()) {
            final int levelId = level.getId();

            final AdminComboBox comboBox = new AdminComboBox(level, presenter.getStore(levelId));
            style.initializeComboBox(comboBox, level);
            updateComboBoxState(comboBox, presenter.isLevelEnabled(levelId));
            comboBoxes.put(levelId, comboBox);
            
            comboBox.addListener(Events.SelectionChange, new Listener<SelectionChangedEvent>() {
                @Override
                public void handleEvent(SelectionChangedEvent be) {
                    AdminEntityDTO selected = (AdminEntityDTO) be.getSelectedItem();
                    presenter.setSelection(levelId, selected);
                }
            });
            comboBox.addListener(Events.BrowserEvent, new Listener<FieldEvent>() {
                @Override
				public void handleEvent(FieldEvent be) {
                    if(be.getEventTypeInt() == Event.ONKEYUP &&
                       be.getEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
                            comboBox.setValue(null);
                            presenter.setSelection(levelId, null);
                    }
                }
            });
            
            presenter.addListener(AdminSelectionEvent.TYPE, new Listener<AdminSelectionEvent>() {
				@Override
				public void handleEvent(AdminSelectionEvent event) {
					comboBoxes.get(event.getLevelId()).setValue(event.getValue());
				}
            });
            
            presenter.addListener(LevelStateChangeEvent.TYPE, new Listener<LevelStateChangeEvent>() {
				@Override
				public void handleEvent(LevelStateChangeEvent event) {
					updateComboBoxState(comboBoxes.get(event.getLevelId()), event.isEnabled());
				}
            });
        }
    }

    public AdminComboBoxSet(AdminFieldSetPresenter presenter) {
    	this(presenter, new Style() {
			
			@Override
			public void onComboStateUpdated(AdminComboBox comboBox, boolean enabled) {}
			
			@Override
			public void initializeComboBox(AdminComboBox comboBox, AdminLevelDTO level) {}
		});
    }
    
    public final Style getStyle() {
    	return style;
    }

	private void updateComboBoxState(final AdminComboBox comboBox, boolean enabled) {
		comboBox.setEnabled(enabled);
		style.onComboStateUpdated(comboBox, enabled);
	}
	   
	
	
	public boolean validate() {
		boolean valid = true;
		for(AdminComboBox comboBox : comboBoxes.values()) {
			if(!comboBox.validate()) {
				valid = false;
			}
		}
		return valid;
	}
	
	@Override
	public final Iterator<AdminComboBox> iterator() {
		return comboBoxes.values().iterator();
	}
}
