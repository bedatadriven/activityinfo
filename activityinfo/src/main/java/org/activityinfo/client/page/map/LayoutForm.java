package org.activityinfo.client.page.map;

import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.Application;
import org.activityinfo.shared.command.GetBaseMaps;
import org.activityinfo.shared.command.result.BaseMapResult;
import org.activityinfo.shared.map.BaseMap;
import org.activityinfo.shared.report.model.MapElement;

import java.util.List;
import java.util.ArrayList;
/*
 * @author Alex Bertram
 */

public class LayoutForm extends FormPanel {

    private final CommandService service;

    private ComboBox baseMapCombo;
    private ComboBox<PageSizeModel> pageSizeCombo;

    @Inject
    public LayoutForm(CommandService service) {

        this.service = service;

        setHeading(Application.CONSTANTS.pageLayout());
        setIcon(Application.ICONS.report());

        setLabelWidth(125);
        setFieldWidth(150);

        baseMapCombo = new ComboBox();
        baseMapCombo.setFieldLabel("Carte de Fond");
        baseMapCombo.setStore(getBaseMapStore());
        baseMapCombo.setDisplayField("name");
        baseMapCombo.setValueField("id");
        baseMapCombo.setTriggerAction(ComboBox.TriggerAction.ALL);
        baseMapCombo.setForceSelection(true);
        baseMapCombo.setAllowBlank(false);
        add(baseMapCombo);

        pageSizeCombo = new ComboBox<PageSizeModel>();
        pageSizeCombo.setFieldLabel("Taille de page");
        pageSizeCombo.setStore(getPageSizeStore());
        pageSizeCombo.setDisplayField("name");
        pageSizeCombo.setTriggerAction(ComboBox.TriggerAction.ALL);
        pageSizeCombo.setForceSelection(true);
        pageSizeCombo.setValue(pageSizeCombo.getStore().getAt(0));
        pageSizeCombo.setAllowBlank(false);
        add(pageSizeCombo);
    }

    public void updateElement(MapElement element) {
        PageSizeModel pageSize = pageSizeCombo.getValue();
        element.setWidth(pageSize.getWidth());
        element.setHeight(pageSize.getHeight());
        element.setBaseMapId((String)baseMapCombo.getValue().get("id"));
    }


    private ListStore getBaseMapStore() {

        DataProxy proxy = new DataProxy<ListLoadResult>() {
            public void load(DataReader<ListLoadResult> dataReader, Object loadConfig, final AsyncCallback<ListLoadResult> callback) {
                service.execute(new GetBaseMaps(), null, new AsyncCallback<BaseMapResult>() {
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    public void onSuccess(BaseMapResult result) {
                        List<ModelData> list = new ArrayList<ModelData>();
                        for(BaseMap baseMap : result.getBaseMaps()) {
                            ModelData data = new BaseModelData();
                            data.set("id", baseMap.getId());
                            data.set("name", baseMap.getName());
                            list.add(data);
                        }
                        callback.onSuccess(new BaseListLoadResult<ModelData>(list));
                    }
                });
            }
        };

        BaseListLoader loader = new BaseListLoader(proxy);
        loader.setRemoteSort(false);

        ListStore store = new ListStore(loader);
        return store;
    }

    private static class PageSizeModel extends BaseModelData {

        public PageSizeModel(String name, int width, int height) {
            set("name", name);
            set("width", width);
            set("height", height);
        }

        public int getWidth() {
            return (Integer)get("width");
        }

        public int getHeight() {
            return (Integer)get("height");
        }
    }

    private ListStore<PageSizeModel> getPageSizeStore() {

        ListStore<PageSizeModel> store = new ListStore<PageSizeModel>();
        store.add(new PageSizeModel("Feuille A4 (Portrait)", 492, 690));
        store.add(new PageSizeModel("Feuille A4 (Paysage)", 534, 474));
        store.add(new PageSizeModel("Slide PowerPoint", 648, 354));
        store.add(new PageSizeModel("Demi Slide PowerPoint", 324, 360));

        return store;
    }
}
