/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.widget;

import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.i18n.I18N;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class LoadingPlaceHolder extends LayoutContainer implements AsyncMonitor {

    private Html html;

    public LoadingPlaceHolder() {
        setLayout(new CenterLayout());
        html = new Html();
        html.addStyleName("loading-placeholder");
        html.setHtml(I18N.CONSTANTS.loadingComponent());
        add(html);
    }

    @Override
    public void beforeRequest() {

    }

    @Override
    public void onCompleted() {
        html.setHtml("Loaded.");
    }

    @Override
    public void onConnectionProblem() {
        html.setHtml(I18N.CONSTANTS.connectionProblem());
    }

    @Override
    public boolean onRetrying() {
        html.setHtml(I18N.CONSTANTS.retrying());
        return false;
    }

    @Override
    public void onServerError() {
        html.setHtml(I18N.CONSTANTS.serverError());
    }
}
