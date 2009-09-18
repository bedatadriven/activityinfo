package org.activityinfo.client.common.widget;

import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.store.ListStore;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.loader.ListCmdLoader;
import org.activityinfo.shared.command.GetMapIcons;
import org.activityinfo.shared.command.result.MapIconResult;
import org.activityinfo.shared.dto.MapIconModel;
/*
 * @author Alex Bertram
 */

public class MapIconComboBox extends ComboBox<MapIconModel> {

    public MapIconComboBox(CommandService service) {

        ListCmdLoader<MapIconResult> loader = new ListCmdLoader<MapIconResult>(service);
        loader.setCommand(new GetMapIcons());

        ListStore<MapIconModel> store = new ListStore<MapIconModel>(loader);
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
