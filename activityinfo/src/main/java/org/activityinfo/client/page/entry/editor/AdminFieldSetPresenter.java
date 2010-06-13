package org.activityinfo.client.page.entry.editor;

import com.extjs.gxt.ui.client.store.ListStore;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.loader.ListCmdLoader;
import org.activityinfo.client.page.common.AdminBoundsHelper;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.result.AdminEntityResult;
import org.activityinfo.shared.dto.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class AdminFieldSetPresenter {


    public interface View {

        public void bindPresenter(AdminFieldSetPresenter presenter);

        public void setStore(int levelId, ListStore<AdminEntityDTO> store);

        public void setEnabled(int levelId, boolean enabled);

        public void setValue(int levelId, AdminEntityDTO value);
    }

    public interface Listener {

        public void onModified();

        public void onBoundsChanged(String name, BoundingBoxDTO bounds);

    }

    private final View view;

    private ActivityDTO currentActivity;

    private List<AdminLevelDTO> levels;
    private Map<Integer, ListCmdLoader<AdminEntityResult>> loaders =
            new HashMap<Integer, ListCmdLoader<AdminEntityResult>>();
    private Map<Integer, ListStore<AdminEntityDTO>> stores = new
            HashMap<Integer, ListStore<AdminEntityDTO>>();
    private Map<Integer, Integer> originalValues;
    private Map<Integer, AdminEntityDTO> selection = new HashMap<Integer, AdminEntityDTO>();

    private BoundingBoxDTO bounds;
    private String boundsName;

    private Listener listener;

    public AdminFieldSetPresenter(Dispatcher service, ActivityDTO activity, View view) {
        this.view = view;
        this.view.bindPresenter(this);
        this.currentActivity = activity;

        this.levels = activity.getAdminLevels();

        for (AdminLevelDTO level : levels) {
            int levelId = level.getId();


            ListCmdLoader<AdminEntityResult> loader = new ListCmdLoader<AdminEntityResult>(service);
            loaders.put(levelId, loader);

            ListStore<AdminEntityDTO> store = new ListStore<AdminEntityDTO>(loader);
            stores.put(levelId, store);

            if (level.isRoot()) {
                loader.setCommand(new GetAdminEntities(level.getId()));
            }

            view.setStore(levelId, store);
            view.setEnabled(levelId, level.isRoot());
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void onSelectionChanged(int levelId, AdminEntityDTO selected) {

        if ((selected == null && selection.get(levelId) != null) ||
                (selected != null && selection.get(levelId) == null) ||
                (selected != null && selection.get(levelId) != null &&
                        selected.getId() != selection.get(levelId).getId())) {


            selection.put(levelId, selected);


            // clear the value of child levels and
            // reconfigure loaders
            updateChildren(levelId, selected == null ? null : selected.getId());

            // alert the listener this has been modified
            if (listener != null) {
                listener.onModified();
            }

            // determine whether our effective bounds have changed
            checkBounds(true);
        }
    }

    protected void updateChildren(int parentLevelId, Integer selectedId) {

        for (AdminLevelDTO child : levels) {
            if (!child.isRoot() && child.getParentLevelId() == parentLevelId) {

                int childLevelId = child.getId();
                ListCmdLoader loader = loaders.get(childLevelId);

                // clear the child value
                // (it is no longer valid as the parent has changed)
                view.setValue(child.getId(), null);

                // reconfigure this combo
                configureCombo(child.getId(), parentLevelId, selectedId);

                // do the same for all the children of this child and so on
                updateChildren(child.getId(), null);
            }
        }
    }

    public void setSite(SiteDTO site) {

        // store values for dirty checking
        originalValues = new HashMap<Integer, Integer>();
        for (AdminLevelDTO level : levels) {
            AdminEntityDTO entity = site.getAdminEntity(level.getId());
            originalValues.put(level.getId(), entity == null ? null : entity.getId());
            selection.put(level.getId(), entity);
        }

        recursivelySetSelection(site, null, null);

        checkBounds(false);
    }


    public boolean isDirty() {

        for (AdminLevelDTO level : levels) {
            if (isDirty(level)) {
                return true;
            }

        }
        return false;
    }

    private boolean isDirty(AdminLevelDTO level) {
        AdminEntityDTO selected = selection.get(level.getId());
        Integer originalId = originalValues.get(level.getId());

        if ((originalId == null && selected != null) ||
                (originalId != null && selected == null) ||
                (originalId != null && selected != null && selected.getId() != originalId)) {
            return true;
        }
        return false;
    }

    public Map<String, Object> getPropertyMap() {

        Map<String, Object> map = new HashMap<String, Object>();

        for (AdminLevelDTO level : levels) {

            map.put(AdminEntityDTO.getPropertyName(level.getId()),
                    selection.get(level.getId()));

        }
        return map;
    }


    private void recursivelySetSelection(SiteDTO site, Integer parentLevelId, Integer parentEntityId) {

        for (AdminLevelDTO level : levels) {
            if (parentLevelId == null && level.isRoot() ||
                    parentLevelId != null && parentLevelId.equals(level.getParentLevelId())) {

                AdminEntityDTO entity = site.getAdminEntity(level.getId());

                ListStore<AdminEntityDTO> store = stores.get(level.getId());
                store.removeAll();
                if (entity != null) {
                    store.add(entity);
                }

                view.setValue(level.getId(), entity);

                configureCombo(level.getId(), parentLevelId, parentEntityId);

                recursivelySetSelection(site, level.getId(), entity == null ? null : entity.getId());
            }
        }
    }

    private void configureCombo(int levelId, Integer parentLevelId, Integer parentEntityId) {

        // enable the combo if it is a root level or if its immediate parent
        // has a selection

        view.setEnabled(levelId, parentLevelId == null || parentEntityId != null);

        // configure the loader

        if (parentLevelId == null) {
            loaders.get(levelId).setCommand(new GetAdminEntities(levelId));
        } else if (parentEntityId == null) {
            loaders.get(levelId).setCommand(null);
        } else {
            loaders.get(levelId).setCommand(new GetAdminEntities(levelId, parentEntityId));
        }

    }


//    private void fillAdminCombosRecursively(SiteDTO assessment, AdminEntityCombo parentCombo) {
//
//        for(AdminEntityCombo combo : levelCombos) {
//            if(combo.getParentCombo() == parentCombo) {
//
//                // only fill if this combo is blank or equal to that of the assessment's
//
//                int levelId = combo.getLevel().getId();
//                AdminEntityDTO assmEntity = assessment.getAdminEntity(levelId);
//
//                if(assmEntity != null) {
//                    if(parentCombo!=null)
//                        assmEntity.setParentId(parentCombo.getValue().getId());
//
//                    if(combo.getValue() != null && combo.getValue().getId() == assmEntity.getId()) {
//                        // already set to this value
//                        fillAdminCombosRecursively(assessment, combo);
//                    } else  {
//                        // change to the assessment value
//                        combo.setValue(assmEntity);
//                        fillAdminCombosRecursively(assessment, combo);
//                    }
//                }
//            }
//        }
//    }


    public BoundingBoxDTO getBounds() {
        return bounds;
    }

    public String getBoundsName() {
        return boundsName;
    }

    private void checkBounds(boolean fireEvent) {

        // update bounds
        BoundingBoxDTO oldBounds = bounds;
        bounds = AdminBoundsHelper.calculate(currentActivity, levels, new AdminBoundsHelper.Getter() {
            public AdminEntityDTO get(int levelId) {
                return selection.get(levelId);
            }
        });

        if (((bounds == null) != (oldBounds == null) ||
                ((bounds != null && !bounds.equals(oldBounds))))) {

            boundsName = AdminBoundsHelper.name(currentActivity, bounds, levels, new AdminBoundsHelper.Getter() {
                public AdminEntityDTO get(int levelId) {
                    return selection.get(levelId);
                }
            });

            if (fireEvent && listener != null) {
                listener.onBoundsChanged(boundsName, bounds);
            }
        }
    }
}
