/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.login.client.xs;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.impl.FormPanelImplHost;
import com.google.gwt.user.client.ui.impl.FormPanelImplIE6;

/**
 * See http://development.lombardi.com/?p=611
 */
public class WindowNameFormPanelImplIE6 extends FormPanelImplIE6 {


    @Override
    public native void hookEvents(Element iframe, Element form,
      FormPanelImplHost listener) /*-{
    if (iframe) {
        iframe.onreadystatechange = function() {
        // If there is no __formAction yet, this is a spurious onreadystatechange
        // generated when the iframe is first added to the DOM.
        if (!iframe.__formAction)
          return;

        if (iframe.readyState == 'complete') {
          // If the iframe's contentWindow has not navigated to the expected action
          // url, then it must be an error, so we ignore it.
          if (!iframe.__sameDomainRestored) {
            iframe.__sameDomainRestored = true;
            iframe.contentWindow.location ="about:blank";
            return;
          }
          listener.@com.google.gwt.user.client.ui.impl.FormPanelImplHost::onFrameLoad()();
        }
      };
    }

    form.onsubmit = function() {
      // Hang on to the form's action url, needed in the
      // onload/onreadystatechange handler.
      if (iframe) {
        iframe.__formAction = form.action;
        iframe.__sameDomainRestored = false;
      }
      return listener.@com.google.gwt.user.client.ui.impl.FormPanelImplHost::onFormSubmit()();
    };
  }-*/;
}
