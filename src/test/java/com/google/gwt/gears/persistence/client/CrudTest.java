/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package com.google.gwt.gears.persistence.client;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * @author Alex Bertram
 */
public class CrudTest extends GWTTestCase {

//    public interface SimpleCrud extends Crud<Simple, Integer> {   }
//    public interface AutoIdCrud extends Crud<AutoIdEntity, Integer> {   }
//    public interface ChildCrud extends Crud<Child, Integer> { }

  @Override
  public String getModuleName() {
    return "com.google.gwt.gears.persistence.Jpa";
  }
//
//    public void testInsert() throws Throwable {
//
//        Connection conn = DriverManager.getConnection("test-db");
//
//        Simple obj = new Simple();
//        obj.setId(3);
//        obj.setName("Fred");
//        obj.setAvailable(true);
//
//        SimpleCrud dao = GWT.create(SimpleCrud.class);
//        dao.setConnection(conn);
//        dao.dropTableIfExists();
//        dao.createTable();
//        dao.insert(obj);
//
//        Statement stmt = conn.createStatement();
//        ResultSet rs = stmt.executeQuery("select id, name, available from Simple");
//
//        Assert.assertTrue(rs.next());
//        Assert.assertEquals(3, rs.getInt(1));
//        Assert.assertEquals("Fred", rs.getString(2));
//        Assert.assertTrue(rs.getInt(3) != 0 );
//        rs.close();
//    }
//
//    public void testFind() throws Throwable {
//
//        Connection conn = DriverManager.getConnection("test-db");
//
//        Simple obj = new Simple();
//        obj.setId(3);
//        obj.setName("Flinstone");
//        obj.setAvailable(true);
//
//        SimpleCrud dao = GWT.create(SimpleCrud.class);
//        dao.setConnection(conn);
//        dao.dropTableIfExists();
//        dao.createTable();
//        dao.insert(obj);
//
//        Simple copy = dao.find(3);
//
//        Assert.assertEquals("id", obj.getId(), copy.getId());
//        Assert.assertEquals("name", obj.getName(), copy.getName());
//        Assert.assertEquals("available", obj.isAvailable(), copy.isAvailable());
//    }
//
//
//    public void testAutoincrement() throws Throwable {
//
//        Connection conn = DriverManager.getConnection("test-db2");
//
//        AutoIdEntity o = new AutoIdEntity();
//        o.setName("Frank");
//
//        AutoIdCrud dao = GWT.create(AutoIdCrud.class);
//        dao.setConnection(conn);
//        dao.dropTableIfExists();
//        dao.createTable();
//
//        dao.insert(o);
//
//        Assert.assertTrue(o.getId() != 0);
//    }
//
//    public void testRuntimeQuery() throws Throwable {
//
//        Connection conn = DriverManager.getConnection("test-db2");
//
//        AutoIdCrud dao = GWT.create(AutoIdCrud.class);
//        dao.setConnection(conn);
//        dao.dropTableIfExists();
//        dao.createTable();
//
//        for(int i=0; i!=100;++i) {
//            AutoIdEntity o = new AutoIdEntity();
//            o.setName(i % 2 == 0 ? "red" : "blue");
//            dao.insert(o);
//        }
//
//        List<AutoIdEntity> list = dao.createNativeQuery("select * from AutoIdEntity where name = :name")
//                .setParameter("name", "blue")
//                .getResultList();
//
//        Assert.assertEquals(50, list.size());
//        assertEquals("blue", list.get(0).getName());
//    }
//
//    public void testManyToOneSaved() throws Throwable {
//
//
//        Parent parent = new Parent();
//        parent.setId(1);
//        parent.setName("Poppi");
//
//        Child child = new Child();
//        child.setId(11);
//        child.setParent(parent);
//        child.setName("Kiddo");
//
//
//        Connection conn = DriverManager.getConnection("db-test");
//
//        ChildCrud crud = GWT.create(ChildCrud.class);
//        crud.setConnection(conn);
//        crud.dropTableIfExists();
//        crud.createTable();
//        crud.insert(child);
//
//        Statement stmt = conn.createStatement();
//        ResultSet rs = stmt.executeQuery("select childId, parentId from Child");
//        assertTrue(rs.next());
//        assertEquals("childId", 11, rs.getInt(1));
//        assertEquals("parentId", 1, rs.getInt(2));
//        rs.close();
//
//        Child reread = crud.find(11);
//        Assert.assertEquals(11, reread.getId());
//        Assert.assertEquals("Kiddo", reread.getName());
//        Assert.assertNotNull("empty parent object is created", reread.getParent());
//        Assert.assertEquals("parent id", 1, reread.getParent().getId());
//
//    }
//
//     public void testNullParent() throws Throwable {
//
//        Child child = new Child();
//        child.setId(11);
//        child.setName("Kiddo");
//        child.setParent(null);
//
//        Connection conn = DriverManager.getConnection("db-test");
//
//        ChildCrud crud = GWT.create(ChildCrud.class);
//        crud.setConnection(conn);
//        crud.dropTableIfExists();
//        crud.createTable();
//        crud.insert(child);
//
//        Child reread = crud.find(11);
//        Assert.assertEquals(11, reread.getId());
//        Assert.assertEquals("Kiddo", reread.getName());
//        Assert.assertNull("parent is null", reread.getParent());
//    }
//
//    public void testPagingRuntimeQuery() throws Throwable {
//
//        Connection conn = DriverManager.getConnection("test-db2");
//
//        AutoIdCrud dao = GWT.create(AutoIdCrud.class);
//        dao.setConnection(conn);
//        dao.dropTableIfExists();
//        dao.createTable();
//
//        for(int i=0; i!=100;++i) {
//            AutoIdEntity o = new AutoIdEntity();
//            o.setName(i % 2 == 0 ? "red" : "blue");
//            dao.insert(o);
//        }
//
//        List<AutoIdEntity> list = dao.createNativeQuery("select * from AutoIdEntity where name = :name")
//                .setParameter("name", "blue")
//                .setMaxResults(10)
//                .getResultList();
//
//        Assert.assertEquals(10, list.size());
//        assertEquals("blue", list.get(0).getName());
//    }
//
//    public interface ContactCrud extends Crud<Contact, String> { }
//
//    public void testEmbedded() throws Throwable {
//
//        Connection conn = DriverManager.getConnection("test-db2");
//
//        ContactCrud crud = GWT.create(ContactCrud.class);
//        crud.setConnection(conn);
//        crud.dropTableIfExists();
//        crud.createTable();
//
//        Contact contact = new Contact();
//        contact.setId("AB643");
//        contact.setName("The Bertrams");
//        contact.setAddress(new Address("13 Kirby Lane", "Apt 2", "Sterling Heights", "MI", 16901, 3024));
//
//        crud.insert(contact);
//
//        Contact reread = crud.find("AB643");
//
//        assertNotNull("Address is created and set", reread.getAddress());
//        assertEquals(contact, reread);
//    }
//
//    public interface ProvinceCrud extends Crud<Province, String> { }
//
//    public void testNullEmbedded() throws Throwable {
//
//        Connection conn = DriverManager.getConnection("testNullEmbedded");
//
//        ProvinceCrud crud = GWT.create(ProvinceCrud.class);
//        crud.setConnection(conn);
//        crud.dropTableIfExists();
//        crud.createTable();
//
//        Province kunduz = new Province("31", "Kunduz", null, 3100);
//        crud.insert(kunduz);
//
//        Province baghdis = new Province("41", "Badghis", new Bounds(0d, 0d, 100d, 200d), null);
//        crud.insert(baghdis);
//
//
//        Province reduz = crud.find("31");
//        assertEquals(kunduz, reduz);
//
//        Province rebad = crud.find("41");
//        assertEquals(baghdis, rebad);
//
//        conn.close();
//    }

}
