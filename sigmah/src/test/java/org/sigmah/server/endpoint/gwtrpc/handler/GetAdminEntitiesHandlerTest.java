/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.sigmah.server.dao.AdminDAO;
import org.sigmah.server.domain.AdminEntity;
import org.sigmah.server.domain.User;
import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.result.AdminEntityResult;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.exception.CommandException;

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
