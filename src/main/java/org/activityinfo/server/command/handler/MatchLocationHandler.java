package org.activityinfo.server.command.handler;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import org.activityinfo.client.local.command.handler.KeyGenerator;
import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.database.hibernate.entity.AdminLevel;
import org.activityinfo.server.database.hibernate.entity.Location;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.util.Jaro;
import org.activityinfo.shared.command.MatchLocation;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.exception.CommandException;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class MatchLocationHandler implements CommandHandler<MatchLocation> {

    private EntityManager em;
    private Jaro jaro = new Jaro();
    
    @Inject
    public MatchLocationHandler(EntityManager em) {
        super();
        this.em = em;
    }

    @Override
    public CommandResult execute(MatchLocation cmd, User user)
        throws CommandException {
           
        Map<Integer, AdminEntity> matched = Maps.newHashMap();
        
        // now try and match against the text
        for(Integer levelId : cmd.getAdminLevels().keySet()) {
            matchEntity(matched, cmd.getAdminLevels(), em.find(AdminLevel.class, levelId));
        }
        
        Location matchedLocation = matchLocation(cmd.getName(), matched.values());

        LocationDTO location = new LocationDTO();

        if(matchedLocation == null) {
            // create a new location object
            
            location.setId(new KeyGenerator().generateInt());
            location.setName(cmd.getName());
            location.setLatitude(cmd.getLatitude());
            location.setLongitude(cmd.getLongitude());
            
            for(AdminEntity entity : matched.values()) {
                AdminEntityDTO dto = new AdminEntityDTO();
                dto.setId(entity.getId());
                dto.setName(entity.getName());
                dto.setLevelId(entity.getLevel().getId());
                location.setAdminEntity(entity.getLevel().getId(), dto);
            }
        } else {
            LocationDTO
        }

        return location;
    }
        
    /* try to match to an existing location, based on administrative entity
     * membership and approximate name matc
     */
    private Location matchLocation(String name, Collection<AdminEntity> entities) {
        
        // find the set of locations that is present in all matched admin
        // entities
        Set<Location> locations = null;
        for(AdminEntity entity : entities) {
            if(locations == null) {
                locations = Sets.newHashSet(entity.getLocations());
            } else {
                locations.retainAll(entity.getLocations());
            }
        }
        
        // now find the best matching location by name among this set
        Location bestMatch = null;
        double bestScore = 0;
        for(Location location : locations) {
            double score = similarity(location.getName(), name);
            if(score > bestScore) {
                bestScore  = score;
                bestMatch = location;
            }
        }
        
        return bestMatch;   
    }

    private void matchEntity(Map<Integer, AdminEntity> matched,
        Map<Integer,String> toMatch,
        AdminLevel level) {
     
        // match parent level first
        if(level.getParent() != null && !matched.containsKey(level.getParentId())) {
            matchEntity(matched, toMatch, level.getParent());
        }
        
        // match by name
        String name = toMatch.get(level.getId());
        if(!Strings.isNullOrEmpty(name)) {
            AdminEntity parent = null;
            AdminEntity matchedEntity = null;
            if(level.getParent() != null && matched.containsKey(level.getParentId())) {
                parent = matched.get(level.getParentId());
                matchedEntity = match(name, parent.getChildren());
            } else {
                matchedEntity = match(name, level.getEntities());
            }
            
            if(matchedEntity != null) {
                matched.put(level.getId(), matchedEntity);
                
                // the import may provide child entities without their parents,
                // so just be sure to add parents here as well
                addParent(matched, matchedEntity);
            }
        }
    }

    private void addParent(Map<Integer, AdminEntity> matched, AdminEntity child) {
        if(child.getParent() != null) {
            matched.put(child.getParent().getLevel().getId(), child.getParent());
            addParent(matched, child.getParent());
        }   
    }

    private AdminEntity match(String name, Set<AdminEntity> entities) {
        // look for an exact match first
        for(AdminEntity entity : entities) {
            if(entity.getName().equalsIgnoreCase(name)) {
                return entity;
            }
        }
        
        // look for the best approximate match
        double bestFit = 0;
        AdminEntity bestEntity = null;
        for(AdminEntity entity : entities) {
            double similarity = similarity(name, entity.getName());
            if(similarity > bestFit) {
                bestFit = similarity;
                bestEntity = entity;
            }
        }
        return bestEntity;
    }   

    protected float similarity(String s1, String s2) {
        return jaro.getSimilarity(s1.toLowerCase(), s2.toLowerCase());
    }
}
