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
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.server.endpoint.gwtrpc.handler;

import org.activityinfo.server.dao.AdminDAO;
import org.activityinfo.server.domain.AdminEntity;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.result.AdminEntityResult;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.exception.CommandException;
import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class GetAdminEntitiesHandlerTest {

    private User user;
    private AdminDAO adminDAO;
    private Mapper mapper;
    private static final int LEVEL_ID = 1;
    private static final int ACTIVITY_ID = 4;
    private static final int PARENT_ID = ACTIVITY_ID;
    private AdminDAO.Query query;

    @Test
    public void testRootLevelQuery() throws Exception {

        GetAdminEntities cmd = new GetAdminEntities(LEVEL_ID);

        expect(query.level(LEVEL_ID)).andReturn(query);
        expect(query.execute()).andReturn(nCopies(15));
        replay(query);

        AdminEntityResult result = execute(cmd);

        assertEquals(15, result.getData().size());
    }

    @Test
    public void testEmptyRootLevelQuery() throws Exception {

        GetAdminEntities cmd = new GetAdminEntities(LEVEL_ID);

        expect(query.level(LEVEL_ID)).andReturn(query);
        expect(query.execute()).andReturn(nCopies(0));
        replay(query);

        AdminEntityResult result = execute(cmd);

        assertEquals(0, result.getData().size());
    }

    @Test
    public void testChildQuery() throws Exception {

        GetAdminEntities cmd = new GetAdminEntities(LEVEL_ID, PARENT_ID);

        expect(query.level(LEVEL_ID)).andReturn(query);
        expect(query.withParentEntityId(PARENT_ID)).andReturn(query);
        expect(query.execute()).andReturn(nCopies(3));
        replay(query);

        AdminEntityResult result = execute(cmd);

        assertEquals(3, result.getData().size());
    }

    @Test
    public void testSiteQuery() throws Exception {

        int expectedCount = 3;

        GetAdminEntities cmd = new GetAdminEntities(LEVEL_ID, PARENT_ID);
        cmd.setActivityId(ACTIVITY_ID);

        expect(query.level(LEVEL_ID)).andReturn(query);
        expect(query.withParentEntityId(PARENT_ID)).andReturn(query);
        expect(query.withSitesOfActivityId(ACTIVITY_ID)).andReturn(query);
        expect(query.execute()).andReturn(nCopies(expectedCount));
        replay(query);

        AdminEntityResult result = execute(cmd);

        assertEquals(expectedCount, result.getData().size());

    }

    @Before
    public void setupUser() {
        user = new User();
        user.setId(LEVEL_ID);
        user.setEmail("alex@bertram");
    }

    @Before
    public void setupMapper() {
        mapper = createMock(Mapper.class);
        expect(mapper.map(isA(AdminEntity.class), eq(AdminEntityDTO.class)))
                .andReturn(new AdminEntityDTO())
                .anyTimes();
        replay(mapper);
    }

    @Before
    public void setupAdminDAO() {
        adminDAO = createMock(AdminDAO.class);
        expect(adminDAO.query()).andReturn(query);
        replay(adminDAO);
    }

    @Before
    public void createQueryMock() {
        query = createMock(AdminDAO.Query.class);
    }

    public AdminEntityResult execute(GetAdminEntities cmd) throws CommandException {
        GetAdminEntitiesHandler handler = new GetAdminEntitiesHandler(adminDAO, mapper);
        return (AdminEntityResult) handler.execute(cmd, user);
    }

    private List<AdminEntity> nCopies(int entitiesToReturn) {
        return Collections.nCopies(entitiesToReturn, new AdminEntity());
    }

}
