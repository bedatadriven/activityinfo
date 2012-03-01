/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.authentication;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * Identifier Generator that generates unique IDs for our authentication that are sufficiently
 * random so that they cannot be guessed.
 *
 * @author Alex Bertram
 */
@SuppressWarnings({"UnusedDeclaration"})  // This class is referenced by name by the Authentication domain object
public class SecureSequenceGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        return SecureTokenGenerator.generate();
    }
}
