/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.login.client.xs;

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
