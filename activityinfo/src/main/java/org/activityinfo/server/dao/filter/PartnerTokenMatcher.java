package org.activityinfo.server.dao.filter;

import org.activityinfo.server.domain.Partner;
import org.hibernate.Session;
import org.hibernate.criterion.*;

import java.util.Locale;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PartnerTokenMatcher extends AbstractEntityTokenMatcher {

    public PartnerTokenMatcher(Session session, Locale locale) {
        super(session, Partner.class, locale);
    }

    @Override
    protected Criterion createCriterion(String name) {
        return Restrictions.eq("partner.name", name);
    }
}
