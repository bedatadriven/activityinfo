/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import org.sigmah.shared.dao.DAO;
import org.sigmah.shared.domain.calendar.PersonalEvent;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public interface PersonalEventDAO extends DAO<PersonalEvent, Integer> {
    void merge(PersonalEvent event);
}
