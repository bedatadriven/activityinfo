package org.sigmah.client.mvp;
//
//import org.sigmah.shared.dto.DTO;
//
//import com.mvp4g.client.event.EventBus;
//
//public interface CrudEventBus<T extends DTO> extends EventBus {
//	
//	// Delete
//	void requestDelete (T itemRequestedToDelete);
//	void confirmDelete (T itemToDelete);
//	void cancelDelete();
//	
//	// Create
//	void requestCreate(T itemPrototype);
//	void confirmCreate(T newItem);
//	void cancelCreate();
//	
//	// Update
//	void requestUpdate(T itemRequestedToUpdate);
//	void confirmUpdate(T updatedItem);
//	void cancelUpdate(T item);
//		
//	// Pagination
//	void firstPage();
//	void previousPage();
//	void gotoPage(int pageNumber);
//	void nextPage();
//	void lastPage();
//	
//	// Grouping
//	void setGroup(String groupedProperty);
//	void removeGroup();
//	
//	// Grouping
//	void sortAscending(String sortProperty);
//	void sortDescending(String sortProperty);
//	
//	// Selection
//	void itemSelected(T newSelectedItem);
//	
//	void refresh();	
//}
