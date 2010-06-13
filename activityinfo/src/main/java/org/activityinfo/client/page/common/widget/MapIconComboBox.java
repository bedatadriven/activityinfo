package org.activityinfo.client.page.common.widget;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.loader.ListCmdLoader;
import org.activityinfo.shared.command.GetMapIcons;
import org.activityinfo.shared.command.result.MapIconResult;
import org.activityinfo.shared.dto.MapIconDTO;
/*
 * @author Alex Bertram
 */

public class MapIconComboBox extends ComboBox<MapIconDTO> {

    public MapIconComboBox(Dispatcher service) {

        ListCmdLoader<MapIconResult> loader = new ListCmdLoader<MapIconResult>(service);
        loader.setCommand(new GetMapIcons());

        ListStore<MapIconDTO> store = new ListStore<MapIconDTO>(loader);
        this.setStore(store);
        this.setTemplate(getTemplateString());
        this.setItemSelector("span.mapIcon");
        this.setValueField("id");
        this.setDisplayField("id");
        this.setForceSelection(true);

    }

    private native String getTemplateString() /*-{
      return [
        '<tpl for="."><span class="mapIcon">',
        '<img src="mapicons/{id}.png">',
        '</span></tpl>'
      ].join("");
      }-*/;
}
