/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.filter;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public abstract class AbstractEntityTokenMatcher implements TokenMatcher {

    protected final Session session;
    protected final Locale locale;

    private Set<String> names;
    private final Class entityClass;


    public AbstractEntityTokenMatcher(Session session, Class entityClass, Locale locale) {
        this.session = session;
        this.entityClass = entityClass;
        this.locale = locale;
    }

    @Override
    public void init(List<String> tokens) {

        Criteria criteria = session.createCriteria(entityClass);
        Disjunction disj = Restrictions.disjunction();
        for(String token : tokens) {
            disj.add(Restrictions.like("name", token, MatchMode.START));
        }
        criteria.add(disj);
        criteria.setProjection( Projections.distinct( Projections.property("name") ));


        names = new HashSet<String>();
        for(Object name : criteria.list()) {
            names.add(((String)name).toLowerCase(locale));
        }
    }

    @Override
    public MatchResult match(List<String> tokens, int startIndex) {

        List<String> cumul = FilterHelper.cumulativeList(tokens, startIndex);

        for(int i=cumul.size()-1; i>=0; i--) {
            if(names.contains(cumul.get(i))) {
                return new MatchResult(i+1, createCriterion(cumul.get(i)));
            }
        }
        return null;
    }

    protected abstract Criterion createCriterion(String name);
}
