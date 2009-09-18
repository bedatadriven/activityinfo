package org.activityinfo.server.dao.filter;

import com.google.inject.Inject;

import javax.persistence.EntityManager;
import java.util.*;

import org.hibernate.criterion.*;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.Session;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class FrenchFilterParser implements FilterParser {

    private final EntityManager em;

    @Inject
    public FrenchFilterParser(EntityManager em) {
        this.em = em;
    }

    @Override
    public Criterion parse(String filter) {

        // create token parsers
        Session session = ((HibernateEntityManager) em).getSession();

        List<TokenMatcher> tokenMatchers = new ArrayList<TokenMatcher>();
        tokenMatchers.add( new AdminTokenMatcher(session, Locale.FRENCH));
        tokenMatchers.add( new PartnerTokenMatcher(session, Locale.FRENCH) );
        tokenMatchers.add( new DateTokenMatcher(Locale.FRENCH) );

        // tokenize the filter
        List<String> tokens = Arrays.asList(filter.toLowerCase(Locale.FRENCH).split("\\s+"));

        // initialize token parsers
        for(TokenMatcher matcher : tokenMatchers) {
            matcher.init(tokens);
        }

        // start parsing

        Conjunction root = Restrictions.conjunction();

        if(generate(root, tokenMatchers, tokens, 0)) {
            return root;
        } else {
            return Restrictions.sqlRestriction("1=0");
        }
    }

    protected boolean generate(Conjunction parent, List<TokenMatcher> tokenMatchers, List<String> tokens, int index) {

        List<Conjunction> branches = new ArrayList<Conjunction>();

        for(TokenMatcher matcher : tokenMatchers) {

            MatchResult result = matcher.match(tokens, index);
            if(result != null) {

                Conjunction conj = Restrictions.conjunction();
                conj.add(result.getCriterion());

                if(index+result.getTokenCount() == tokens.size() ||
                    generate(conj, tokenMatchers, tokens, index+result.getTokenCount())) {

                    branches.add(conj);
                }
            }
        }

        if(branches.size() == 0) {
            return false;
        } else if(branches.size() == 1) {
            parent.add(branches.get(0));
            return true;
        } else {
            Disjunction disj = Restrictions.disjunction();
            for(Conjunction branch : branches) {
                disj.add(branch);
            }
            parent.add(disj);
            return true;
        }
    }

}
