package org.sigmah.server.endpoint.gwtrpc;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.ejb.HibernateEntityManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.server.endpoint.gwtrpc.handler.ComputeMapTreeHandler;
import org.sigmah.shared.command.ComputeMapTree;
import org.sigmah.shared.command.result.MapTree;
import org.sigmah.shared.command.result.MapTree.Node;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.InjectionSupport;


@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class ComputeMapTreeTest extends CommandTestCase {

	@Test
	public void works() throws CommandException, FileNotFoundException {
		
		MapTree tree = execute(new ComputeMapTree());
		
		assertThat(tree.getRoot().getChildren().size(), equalTo(1));
		
		writeGraphViz(tree, "works");
		
	}
	
	
	public static void main(String[] args) throws CommandException, FileNotFoundException {
		
		Properties config = new Properties();
		config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		config.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		config.setProperty("hibernate.connection.username", "root");
		config.setProperty("hibernate.connection.password", "adminpwd");
		config.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/activityinfo15");
		
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("activityInfo", config);
		ComputeMapTreeHandler handler = new ComputeMapTreeHandler((HibernateEntityManager) emf.createEntityManager());
		
		MapTree tree = (MapTree) handler.execute(new ComputeMapTree(), new User());
			
		writeGraphViz(tree, "drc");
	}
	
	private static void writeGraphViz(MapTree tree, String filename) throws FileNotFoundException {
		
		PrintWriter writer = new PrintWriter(filename + ".dot");
		writer.println("digraph maptree {");
		writer.println("node [color=lightblue2, style=filled]; ");

		writeNodes(writer, tree.getRoot());
		writeEdges(writer, tree.getRoot());
		
		writer.println("}");
		writer.close();
		
	}
	
	private static void writeNodes(PrintWriter writer, Node<?> root) {
		for(Node child : root.getChildren()) {
			writer.println(child.hashCode() + " [label=" + quote(child.toString()) + "];");
			writeNodes(writer, child);
		}
	}


	private static void writeEdges(PrintWriter writer, Node<?> root) {
		for(Node child : root.getChildren()) {
			writer.println(root.hashCode() + " -> " + child.hashCode() + ";");
			writeEdges(writer, child);
		}
	}


	private static String quote(Object o) {
		return "\"" + o + "\"";
	}
	
}
