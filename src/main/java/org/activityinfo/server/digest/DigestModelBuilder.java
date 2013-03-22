package org.activityinfo.server.digest;

import java.io.IOException;
import java.util.Date;

import org.activityinfo.server.database.hibernate.entity.User;

public interface DigestModelBuilder {

    public abstract DigestModel createModel(User user, Date date, int days) throws IOException;

}