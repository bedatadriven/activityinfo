/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.map;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.command.GetBaseMaps;
import org.sigmah.shared.command.result.BaseMapResult;
import org.sigmah.shared.map.BaseMap;
import org.sigmah.shared.report.model.MapElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides user interface for selecting the layout and basemap of the
 * map.
 *
 * @author Alex Bertram
 */
public class LayoutForm extends FormPanel {

    private final Dispatcher service;

    private ComboBox baseMapCombo;
    private ComboBox<PageSizeModel> pageSizeCombo;

    @Inject
    public LayoutForm(Dispatcher service) {

        this.service = service;

        setHeading(I18N.CONSTANTS.pageLayout());
        setIcon(IconImageBundle.ICONS.report());

        setLabelWidth(125);
        setFieldWidth(150);

        baseMapCombo = new ComboBox();
        baseMapCombo.setFieldLabel(I18N.CONSTANTS.backgroundMap());
        baseMapCombo.setStore(getBaseMapStore());
        baseMapCombo.setDisplayField("name");
        baseMapCombo.setValueField("id");
        baseMapCombo.setTriggerAction(ComboBox.TriggerAction.ALL);
        baseMapCombo.setForceSelection(true);
        baseMapCombo.setAllowBlank(false);
        add(baseMapCombo);

        final ListLoader loader = baseMapCombo.getStore().getLoader();
        loader.addLoadListener(new LoadListener() {
            @Override
            public void loaderLoad(LoadEvent le) {
                baseMapCombo.setValue(baseMapCombo.getStore().getAt(0));
                loader.removeLoadListener(this);
            }
        });
        loader.load();

        pageSizeCombo = new ComboBox<PageSizeModel>();
        pageSizeCombo.setFieldLabel(I18N.CONSTANTS.pageSize());
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
        element.setBaseMapId((String) baseMapCombo.getValue().get("id"));
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
                        for (BaseMap baseMap : result.getBaseMaps()) {
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
            return (Integer) get("width");
        }

        public int getHeight() {
            return (Integer) get("height");
        }
    }

    private ListStore<PageSizeModel> getPageSizeStore() {

        ListStore<PageSizeModel> store = new ListStore<PageSizeModel>();
        store.add(new PageSizeModel(I18N.CONSTANTS.sheetA4Portrait(), 492, 690));
        store.add(new PageSizeModel(I18N.CONSTANTS.sheetA4Landscape(), 534, 474));
        store.add(new PageSizeModel(I18N.CONSTANTS.slidePowerPoint(), 648, 354));
        store.add(new PageSizeModel(I18N.CONSTANTS.halfSlidePowerPoint(), 324, 360));

        return store;
    }
}
