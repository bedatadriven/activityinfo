/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.table.filter;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import org.sigmah.client.Application;
import org.sigmah.client.page.common.FieldSetFitLayout;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.SchemaDTO;
/*
 * @author Alex Bertram
 */

public class PartnerFieldSet extends AbstractFilterFieldSet {
    protected ListView<PartnerDTO> checkList;

    public PartnerFieldSet(SchemaDTO schema) {

        setHeading(Application.CONSTANTS.filterByPartner());


        setLayout(new FieldSetFitLayout());
        setScrollMode(Style.Scroll.AUTO);

        TreeStore<PartnerDTO> store = new TreeStore<PartnerDTO>();
        store.add(schema.getVisiblePartnersList(), false);

        TreePanel<PartnerDTO> tree = new TreePanel<PartnerDTO>(store);
        tree.setCheckable(true);
        tree.setIconProvider(new ModelIconProvider<PartnerDTO>() {
            public AbstractImagePrototype getIcon(PartnerDTO model) {
                return Application.ICONS.group();
            }
        });

        add(tree);

        setHeight(250);

    }


}
