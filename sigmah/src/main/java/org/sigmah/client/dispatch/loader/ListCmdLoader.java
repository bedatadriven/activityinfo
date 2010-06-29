/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch.loader;

import com.extjs.gxt.ui.client.data.ListLoader;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.GetListCommand;
import org.sigmah.shared.command.result.ListResult;

public class ListCmdLoader<ResultT extends ListResult> extends
        AbstractListCmdLoader<ResultT, GetListCommand<ResultT>>
        implements ListLoader<ResultT> {

    public ListCmdLoader(Dispatcher service) {
        super(service);
    }


}
