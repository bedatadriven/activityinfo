/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.entry.admin;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activityinfo.client.data.proxy.AdminEntityProxy;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.map.AdminBoundsHelper;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.dto.HasAdminEntityValues;
import org.activityinfo.shared.util.mapping.Extents;

import org.activityinfo.client.Log;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Presenter which drives the selection of a set of heirarchial admin
 * levels.
 */
public class AdminFieldSetPresenter extends BaseObservable implements HasAdminEntityValues {
	
    private class Level {
    	private AdminLevelDTO level;
    	private AdminEntityProxy proxy;
    	private ListLoader<ListLoadResult<AdminEntityDTO>> loader;
    	private ListStore<AdminEntityDTO> store;
    	private AdminEntityDTO selection;
    	private List<Level> children;
    	private boolean enabled;
    	
    	public Level(AdminLevelDTO level) {
    		this.level = level;
    		this.proxy = new AdminEntityProxy(dispatcher, level.getId());
    		this.loader = new BaseListLoader<ListLoadResult<AdminEntityDTO>>(proxy);
    		this.store = new ListStore<AdminEntityDTO>(loader);
    		this.children = Lists.newArrayList();
    		
    		if(level.isRoot()) {
    			this.loader.load();
    			this.enabled = true;
    		} else {
    			this.enabled = false;
    		}
    	}
    	
    	/**
    	 * 
    	 * @return the AdminLevel id
    	 */
		public int getId() {
			return level.getId();
		}

		public String getPropertyName() {
			return AdminEntityDTO.getPropertyName(level.getId());
		}
		
		public ListStore<AdminEntityDTO> getStore() {
			return store;
		}
	
		public AdminEntityDTO getSelection() {
			return selection;
		}
		
		/**
		 * Sets the new selection, returning true if it is different
		 * from the existing selection
		 */
		public boolean setSelection(AdminEntityDTO newSelection) {
			
			trace("setSelection(" + newSelection + "), was = " + selection);
			
			if(selection == null && newSelection != null) {
				updateSelection(newSelection);
				return true;
			}
			if(selection != null && newSelection == null) {
				updateSelection(newSelection);
				return true;
			}
			if(selection != null && newSelection != null) {
				if(selection.getId() != newSelection.getId()) {
					updateSelection(newSelection);
					return true;
				}
			}
			return false;
		}
		
		private void updateSelection(AdminEntityDTO newSelection) {
			trace("updateSelection(" + newSelection + ")");
					
			this.selection = newSelection;
    		fireEvent(new AdminLevelSelectionEvent(level.getId(), newSelection));
    		
    		for(Level child : children) {
    			child.onParentSelectionChanged(newSelection);
    		}
		}
		
		public void addChild(Level child) {
			children.add(child);
		}
		
		public void onParentSelectionChanged(AdminEntityDTO newParentSelection) {
			trace("onParentSelectionChanged(" + newParentSelection + ")");
			
			if(newParentSelection == null) {
				disable();
				
			} else {
				proxy.setParentAdminEntityId(newParentSelection.getId());
				setEnabled(true);
				
				if(selection != null && selection.getParentId() != newParentSelection.getId()) {
					setSelection(null);
				}	
			}
		}
		
		public boolean isEnabled() {
			return enabled;
		}
		
		private void disable() { 
			trace("disable()");
			
			store.removeAll();
			setEnabled(false);
			setSelection(null);			
		}
		
		private void setEnabled(boolean newState) {
			trace("setEnabled(" + newState + ")");
			if(enabled != newState) {
				this.enabled = newState;
				fireEvent(new LevelStateChangeEvent(level.getId(), newState));
			}
		}
		
		private void trace(String message) {
			Log.trace("level(" + level.getName() + ")." + message);
		}
    }
       
    private final Dispatcher dispatcher;
    private CountryDTO country;
    private List<AdminLevelDTO> levels;
    private Map<Integer, Level> levelMap;
    
    private Extents bounds = Extents.maxGeoBounds();
    private String boundsName = "";
    
    public AdminFieldSetPresenter(Dispatcher dispatcher, CountryDTO country, List<AdminLevelDTO> levels) {
    	this.dispatcher = dispatcher;
    	this.levels = Lists.newArrayList(levels);
    	this.levelMap = Maps.newHashMap();
    	
    	for (AdminLevelDTO level : levels) {
    		levelMap.put(level.getId(), new Level(level));
        }
    	for (AdminLevelDTO level : levels) {
    		if(!level.isRoot()) {
    			Level parent = levelMap.get(level.getParentLevelId());
    			if(parent != null) {
    				parent.addChild( levelMap.get(level.getId()));
    			}
    		}
    	}
    }
    
    private Level level(int id) {
    	return levelMap.get(id);
    }
    
    public List<AdminLevelDTO> getAdminLevels() {
    	return levels;
    }
    
    public ListStore<AdminEntityDTO> getStore(int levelId) {
    	return level(levelId).getStore();
    }

    
    /** 
     * Sets the selection for a given admin level:
     * <ul>
     * <li>This WILL fire a SELECTION_CHANGED event for this level
     * <li>If child selections are not consistent with this new selection, they will
     * be updated recursively and will fire events as well if their selection changes.</li>
     * </ul>
     */
    public void setSelection(int levelId, AdminEntityDTO selection) {
    	if( level(levelId).setSelection(selection) ) {
    		updateBounds();
    		fireEvent(new AdminSelectionChangedEvent());
    	}
    }
    
    /**
     * Sets the selection for all AdminLevels. A SELECTION_CHANGED event will be fired
     * for each changed level. 
     */
    public void setSelection(HasAdminEntityValues values) {
    	for(Level level : levelMap.values()) {
    		level.setSelection(values.getAdminEntity(level.getId()));
    	}
    	updateBounds();
    	fireEvent(new AdminSelectionChangedEvent());
    }
 
       
    public Map<String, AdminEntityDTO> getPropertyMap() {
        Map<String, AdminEntityDTO> map = new HashMap<String, AdminEntityDTO>();

        for (Level level : levelMap.values()) {
			map.put(level.getPropertyName(), level.getSelection());
        }
        return map;
    }
   
    private void fireEvent(BaseEvent event) {
    	fireEvent(event.getType(), event);
    }

    public Extents getBounds() {
        return bounds;
    }

    public String getBoundsName() {
        return boundsName;
    }

    private void updateBounds() {
        Extents oldBounds = bounds;
        bounds = AdminBoundsHelper.calculate(country, levels, new HasAdminEntityValues() {
            @Override
			public AdminEntityDTO getAdminEntity(int levelId) {
                return level(levelId).getSelection();
            }
        });

        if(!bounds.equals(oldBounds)) {
            boundsName = AdminBoundsHelper.name(bounds, levels, this);
            
            fireEvent(new BoundsChangedEvent(bounds, boundsName));
        }
    }

	public Collection<Integer> getAdminEntityIds() {
		List<Integer> result = Lists.newArrayList();
		
        for (Level level : levelMap.values()) {
            AdminEntityDTO entity = level.getSelection();
            if (entity != null){
            	result.add(entity.getId());
            }
        }
		
		return result;
	}
	
	@Override
	public AdminEntityDTO getAdminEntity(int levelId) {
		return level(levelId).getSelection();
	}

	public AdminEntityDTO getAdminEntity(AdminLevelDTO level) {
		return level(level.getId()).getSelection();
	}
	
	public boolean isLevelEnabled(int levelId) {
		return level(levelId).isEnabled();
	}
	
	public boolean isLevelEnabled(AdminLevelDTO level) {
		return isLevelEnabled(level.getId());
	}

}
