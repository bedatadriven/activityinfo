package org.sigmah.server.endpoint.file;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class FileModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(FileManager.class).to(FileManagerImpl.class).in(Singleton.class);
    }
}
