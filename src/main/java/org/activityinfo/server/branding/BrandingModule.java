package org.activityinfo.server.branding;

import org.activityinfo.server.database.hibernate.entity.Domain;
import org.activityinfo.server.endpoint.rest.RestApiModule;

public class BrandingModule extends RestApiModule {

    @Override
    protected void configureResources() {
        bind(Domain.class).toProvider(DomainProvider.class);
        bindResource(BrandingConfigResource.class);
    }
}
