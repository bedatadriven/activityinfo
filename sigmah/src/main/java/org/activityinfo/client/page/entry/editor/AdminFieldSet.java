package org.activityinfo.client.page.entry.editor;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import org.activityinfo.client.Application;
import org.activityinfo.client.page.common.widget.RemoteComboBox;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminFieldSet extends AbstractFieldSet implements AdminFieldSetPresenter.View  {

    private AdminFieldSetPresenter presenter;
    private Map<Integer, ComboBox<AdminEntityDTO>> comboBoxes =
            new HashMap<Integer, ComboBox<AdminEntityDTO>>();

    public AdminFieldSet(ActivityDTO activity) {
        super(Application.CONSTANTS.location(), 100, 200);

        for(final AdminLevelDTO level : activity.getAdminLevels()) {
            final int levelId = level.getId();

            final ComboBox<AdminEntityDTO> comboBox = new RemoteComboBox<AdminEntityDTO>();
            comboBox.setFieldLabel(level.getName());
            comboBox.setStore(new ListStore<AdminEntityDTO>());
            comboBox.setTypeAhead(false);
           // comboBox.setQueryDelay(0);
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
//            comboBox.addListener(Events.Blur, new Listener<FieldEvent>() {
//                public void handleEvent(FieldEvent be) {
//
//                    if(comboBox.getRawValue() == null || comboBox.getRawValue().length() == 0) {
//                        // the user has erased the text of the field
//
//                        presenter.onSelectionChanged(levelId, null);
//
//                    } else if(comboBox.getValue() != null &&
//                              comboBox.getValue().get("name").equals(comboBox.getRawValue())) {
//
//                        // the user typed in a new name that doesn't match the current selection
//                        presenter.onSelectionChanged(levelId, comboBox.getValue() );
//
//                    } else if(comboBox.getStore().getCount() <= 1 &&
//                            comboBox.getStore().getLoader() != null) {
//
//                        final String name = comboBox.getRawValue();
//                        // need to load the list first
//                        comboBox.markInvalid("Chargement de " + level.getName() + " en cours");
//                        final Loader loader = comboBox.getStore().getLoader();
//                        loader.addListener(Loader.Load, new Listener<LoadEvent>() {
//                            public void handleEvent(LoadEvent be) {
//                                loader.removeListener(Loader.Load, this);
//                                ListLoadResult<AdminEntityDTO> result = be.getData();
//                                AdminEntityDTO sel = findEntity(name, result.getData());
//                                if(sel == null) {
//                                    comboBox.markInvalid("Sélection non-valide");
//                                } else {
//                                    presenter.onSelectionChanged(levelId, sel);
//                                    comboBox.clearInvalid();
//                                }
//                            }
//                        });
//
//                    } else {
//                        comboBox.markInvalid("Sélection non-valide");
//                    }
//
//                }
//            });
            comboBoxes.put(levelId, comboBox);
            add(comboBox);
        }

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


    //	private boolean validateAdminChoices() {
//
//		UIMessages messages = GWT.create(UIMessages.class);
//		
//		// first validate the selections using the bounding boxes
//		AdminEntityCombo[] combos = this.levelCombos.values().toArray(new AdminEntityCombo[0]);
//		
//		for(AdminEntityCombo combo : combos) {
//			combo.clearInvalid();
//		}
//	
//		for(int i = 0; i!=combos.length; ++i) {
//			AdminEntity a = combos[i].getValue();
//			if(a != null && a.hasBounds()) {
//				for(int j=i+1; j!=combos.length; ++j) {
//
//					AdminEntity b = combos[j].getValue();
//
//					if(b!=null && b.hasBounds() && a.getBounds().distance(b.getBounds()) > 0.25 ) {
//						combos[i].forceInvalid(messages.adminConflict(a.getName(),combos[j].getLevel().getName(), b.getName()));
//						combos[j].forceInvalid(messages.adminConflict(b.getName(), combos[i].getLevel().getName(), a.getName()));
//						return false;
//					}
//				}
//			}
//		}	
//		return true;
//	}

    public ComboBox<AdminEntityDTO> getCombo(int levelId) {
        return comboBoxes.get(levelId);
    }
}
