package org.sigmah.server.search;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.dao.DAO;
import org.sigmah.shared.domain.SearchDocument;

/*
 * Creates an index from scratch for all the items in the database
 */
public class SearchIndexer {
	List<DAO> daos = new ArrayList<DAO>();

	/*
	 * Iterates over all daos providing SearchDocuments, then adds all the documents to the index 
	 */
	public void index() {
		createDaoList();
		
		for (DAO dao : daos) {
			List<SearchDocument> documents = new ArrayList<SearchDocument>();
			
			addDocumentsToIndex(documents);
		}
	}

	private void addDocumentsToIndex(List<SearchDocument> documents) {
		// TODO Auto-generated method stub
		
	}

	private void createDaoList() {
		// Add daos to list
	}
	
}
