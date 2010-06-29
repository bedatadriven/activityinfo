/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.apache.log4j.Logger;
import org.sigmah.server.util.logging.Trace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigModule extends AbstractModule {
    private static Logger logger = Logger.getLogger(ConfigModule.class);

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    @Trace
    public Properties provideConfigProperties() {
        Properties properties = new Properties();
        tryToLoadFromTomcatConfDirectory(properties);
        return properties;
    }

    private boolean tryToLoadFromTomcatConfDirectory(Properties properties) {
        try {
            File appConfig = new File("conf" + File.separator + "activityinfo.conf");
            logger.info("Trying to read properties from tomcat conf folder: " + appConfig.getAbsolutePath());
            if(appConfig.exists()) {
                logger.info("Reading properties from " + appConfig.getAbsolutePath());
                properties.load(new FileInputStream(appConfig));
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }
}
