/**
 * JPA/Hibernate implementation of the Data Access layer
 *
 * The goal is to use the Java Persistence (JPA) API as much as possible,
 * not because there's any interest in replacing Hibernate, but it makes it easier
 * to leverage third-party tools targeting JPA and leverage existing knowledge of
 * people working on the code base.
 *
 * There are still Hibernate-specific things that we use like the Criterion API and
 * Filters which are not (yet) part of JPA.
 */
package org.sigmah.server.dao.hibernate;