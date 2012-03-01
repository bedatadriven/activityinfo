/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.util.logging;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class LoggingModule extends AbstractModule {

    @Override
    protected void configure() {
        LoggingInterceptor interceptor = new LoggingInterceptor();
        requestInjection(interceptor);

        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Trace.class), interceptor);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(LogException.class), interceptor);
    }
}
