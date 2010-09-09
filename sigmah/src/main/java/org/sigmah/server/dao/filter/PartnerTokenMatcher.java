/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.filter;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.sigmah.shared.domain.OrgUnit;

import java.util.Locale;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PartnerTokenMatcher extends AbstractEntityTokenMatcher {

    public PartnerTokenMatcher(Session session, Locale locale) {
        super(session, OrgUnit.class, locale);
    }

    @Override
    protected Criterion createCriterion(String name) {
        return Restrictions.eq("partner.name", name);
    }
}
