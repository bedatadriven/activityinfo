/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.hibernate;

import com.google.inject.Inject;
import javax.persistence.EntityManager;
import org.sigmah.server.dao.PersonalEventDAO;
import org.sigmah.shared.domain.calendar.PersonalEvent;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class PersonalEventHibernateDAO extends GenericDAO<PersonalEvent, Integer> implements PersonalEventDAO {
    @Inject
    public PersonalEventHibernateDAO(EntityManager em) {
        super(em);
    }
}
