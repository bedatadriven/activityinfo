/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.dispatch.loader;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.shared.command.GetListCommand;
import org.activityinfo.shared.command.result.ListResult;

import com.extjs.gxt.ui.client.data.ListLoader;

/**
 * Command-pattern-based implementation of the GXT {@link com.extjs.gxt.ui.client.data.ListLoader}
 *
 * @deprecated Use standard GXT loader with custom {@link com.extjs.gxt.ui.client.data.DataProxy}
 * @param <ResultT>
 */
public class ListCmdLoader<ResultT extends ListResult> extends
        AbstractListCmdLoader<ResultT, GetListCommand<ResultT>>
        implements ListLoader<ResultT> {

    public ListCmdLoader(Dispatcher service) {
        super(service);
    }


}
