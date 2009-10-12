package org.activityinfo.client.page.table.filter;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import org.activityinfo.client.Application;
import org.activityinfo.client.page.common.FieldSetFitLayout;
import org.activityinfo.shared.dto.PartnerModel;
import org.activityinfo.shared.dto.Schema;
/*
 * @author Alex Bertram
 */

public class PartnerFieldSet extends AbstractFilterFieldSet {
    protected ListView<PartnerModel> checkList;

    public PartnerFieldSet(Schema schema) {

        setHeading("Filtrer par Partenaire");


        setLayout(new FieldSetFitLayout());
        setScrollMode(Style.Scroll.AUTO);
     
        TreeStore<PartnerModel> store = new TreeStore<PartnerModel>();
        store.add(schema.getVisiblePartnersList(), false);

        TreePanel<PartnerModel> tree = new TreePanel<PartnerModel>(store);
        tree.setCheckable(true);
        tree.setIconProvider(new ModelIconProvider<PartnerModel>() {
            public AbstractImagePrototype getIcon(PartnerModel model) {
                return Application.ICONS.group();
            }
        });

        add(tree);

        setHeight(250);

    }



}
