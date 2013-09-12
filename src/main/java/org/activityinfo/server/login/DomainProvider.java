package org.activityinfo.server.login;

import org.activityinfo.server.database.hibernate.entity.Domain;

public interface DomainProvider {

    public abstract Domain findDomain();

}