package org.activityinfo.server;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
/*
 * @author Alex Bertram
 */

public class SchemaMigrationTest {

    @Test
    public void test() {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("activityInfo");
        EntityManager em = emf.createEntityManager();

        em.createQuery("select a from Activity a").setMaxResults(10).getResultList();
        em.createQuery("select a from AdminEntity  a").setMaxResults(10).getResultList();
        em.createQuery("select a from Attribute a").setMaxResults(10).getResultList();
        em.createQuery("select a from AttributeGroup a").setMaxResults(10).getResultList();
        em.createQuery("select a from AttributeValue a").setMaxResults(10).getResultList();
        em.createQuery("select a from Authentication a").setMaxResults(10).getResultList();
        em.createQuery("select c from Country c").setMaxResults(10).getResultList();
        em.createQuery("select i from Indicator i").setMaxResults(10).getResultList();
        em.createQuery("select i from IndicatorValue i").setMaxResults(10).getResultList();
        em.createQuery("select l from Location l").setMaxResults(10).getResultList();
        em.createQuery("select l from LocationType l").setMaxResults(10).getResultList();
        em.createQuery("select p from Partner p").setMaxResults(10).getResultList();
        em.createQuery("select u from UserDatabase u").setMaxResults(10).getResultList();
        em.createQuery("select r from ReportDefinition r").setMaxResults(10).getResultList();
        em.createQuery("select a from Authentication a").setMaxResults(10).getResultList();
        em.createQuery("select s from Site s").setMaxResults(10).getResultList();
        em.createQuery("select u from User u").setMaxResults(10).getResultList();
        em.createQuery("select u from UserPermission u").setMaxResults(10).getResultList();
        


    }

}
