

package org.activityinfo.server.util.beanMapping;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import junit.framework.Assert;

import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.database.hibernate.entity.AdminLevel;
import org.activityinfo.server.database.hibernate.entity.Bounds;
import org.activityinfo.server.util.beanMapping.BeanMappingModule;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.dozer.Mapper;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@Modules({BeanMappingModule.class})
public class AdminEntityMappingTest {

    @Inject
    Mapper mapper;

    @Test
    public void testBounds() {
        Bounds bounds = new Bounds();
        bounds.setX1(1.0);
        bounds.setY1(2.0);
        bounds.setX2(3.0);
        bounds.setY2(4.0);

        AdminLevel level = new AdminLevel();
        level.setId(81);
        level.setName("My Level");

        AdminEntity parent = new AdminEntity();
        parent.setId(93);

        AdminEntity entity = new AdminEntity();
        entity.setLevel(level);
        entity.setParent(parent);
        entity.setBounds(bounds);

        AdminEntityDTO dto = mapper.map(entity, AdminEntityDTO.class);

        Assert.assertEquals("parentId", parent.getId(), dto.getParentId().intValue());
        Assert.assertEquals("levelId", level.getId(), dto.getLevelId());

        Assert.assertNotNull("bounds", dto.getBounds());
        Assert.assertEquals("x1", bounds.getX1(), dto.getBounds().getMinLon());
        Assert.assertEquals("y1", bounds.getY1(), dto.getBounds().getMinLat());
        Assert.assertEquals("x2", bounds.getX2(), dto.getBounds().getMaxLon());
        Assert.assertEquals("y2", bounds.getY2(), dto.getBounds().getMaxLat());
    }
}
