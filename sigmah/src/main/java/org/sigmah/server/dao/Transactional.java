/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a method should be run within a single Transaction.
 * See {@link org.sigmah.server.dao.hibernate.TransactionalInterceptor} for implementation.
 *
 * <strong>Important note:</strong> AOP can <strong>only</strong> applied to methods
 * with <code>protected</code> visiblity. If you apply this annotation to a <code>private</code>
 * method, Guice will fail silently to override the method and your method will not be
 * executed within a transaction.
 *
 * @author Alex Bertram
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transactional {
}
