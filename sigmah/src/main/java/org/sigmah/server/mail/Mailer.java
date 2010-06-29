/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.mail;

import java.util.Locale;

public interface Mailer<T> {
    void send(T model, Locale locale) throws Exception;
}
