/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.util.beanMapping;

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
        Assert.assertEquals("x1", bounds.getX1(), dto.getBounds().getX1());
        Assert.assertEquals("y1", bounds.getY1(), dto.getBounds().getY1());
        Assert.assertEquals("x2", bounds.getX2(), dto.getBounds().getX2());
        Assert.assertEquals("y2", bounds.getY2(), dto.getBounds().getY2());
    }
}
